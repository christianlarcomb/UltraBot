/*
 * MIT License
 *
 * Copyright (c) 2018 Chace (Christian C. Larcomb)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ultra.bot.commands.moderation;

import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.converters.TagToUser;
import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;
import org.bson.conversions.Bson;

public class UnDunce extends Command {

    /* CLASS USES MONGODB */

    public UnDunce() {
        this.name = "undunce";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MANAGE_ROLES};
        this.cooldown = 30;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Undunce", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        /** Channel Cleanup **/
        new PurgeMsgs().purgeMessages(event, 1);

        Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
        Role AdminRole = event.getGuild().getRolesByName("adm", true).get(0);
        Role ModRole = event.getGuild().getRolesByName("mod", true).get(0);


        if (event.getMember().getRoles().contains(OPRole) || event.getMember().getRoles().contains(AdminRole) || event.getMember().getRoles().contains(ModRole)) {

            try {

                CmdArgGetter cag = new CmdArgGetter();
                TagToUser tagToUser = new TagToUser();

                User tagged_user = tagToUser.tagToUser(cag.getArgs(event)[1]);

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document tagged_doc_found = (Document) mongoCollection.find(new Document("User", tagged_user.getId())).first();

                boolean globally_dunced = tagged_doc_found.getBoolean("Global_Dunce");

                if (globally_dunced) {

                    int tagged_role_heirarchy = event.getGuild().getMember(tagged_user).getRoles().get(0).getPositionRaw();
                    int self_role_heirarchy = event.getMember().getRoles().get(0).getPositionRaw();

                    /** SOME IMPORTANT CHECKS **/
                    if(tagged_user == event.getMember().getUser()){ event.reply("**Wait a minute...** You cannot undunce yourself (duh)!"); return; }
                    else if (tagged_user.isBot()){ event.reply("**What?** You cannot undunce a bot!"); return; }
                    else if (self_role_heirarchy < tagged_role_heirarchy){ event.reply("**Hold up!** You cannot undunce someone with a higher rank than you!"); return; }

                    Document doc_to_insert = new Document("User", tagged_user.getId());
                    doc_to_insert.append("Global_Dunce", false);

                    // OUTPUT: The proper information
                    event.reply("**" + event.getMember().getUser().getName() + "** has effectively un-dunced **" + tagged_user.getName() + "**");

                    // INSERTING: Doc to MongoDB
                    Bson updateoperation = new Document("$set", doc_to_insert);
                    mongoCollection.updateOne(tagged_doc_found, updateoperation);


                    /** Closing Connection **/
                    //MongoConnect.closingDatabase();

                } else {
                    event.reply("Oops **" + event.getMember().getUser().getName() + "**! " + tagged_user.getName() + " is not Dunced. \n" +
                            "I can not undo what has not been done.");
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } else {
            event.reply(new ErrorEmbed("Error", "Invalid permissions.", event.getGuild()).getError().build());
        }
    }
}

