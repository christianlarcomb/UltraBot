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

package com.ultra.bot.commands.user;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.converters.TagToUser;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;

public class Users extends Command {

    /* This command pairs with the InvitationReward Framework */

    public Users() {
        this.name = "user";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.aliases = new String[]{"users"};
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Users", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        /** Channel Cleanup **/
        new PurgeMsgs().purgeMessages(event, 1);


        try {

            CmdArgGetter cag = new CmdArgGetter();

            // Try to find an arg, if none, catch it and go forward.
            TagToUser tagToUser = new TagToUser();
            User tagged_user = tagToUser.tagToUser(cag.getArgs(event)[1]);

            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
            Document coins_found = (Document) mongoCollection.find(new Document("User", tagged_user.getId())).first();
            Document curr_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();

            String curr_name = curr_doc_found.getString("Currency_Name");


            //System.out.println("Test 2 Fired");

            String total_coins = coins_found.get("Coins").toString();

            //System.out.println("Test 3 Fired");
            String coins_from_invites = coins_found.get("Coins_From_Invites").toString();
            String coins_from_messages = coins_found.get("Coins_From_Messages").toString();
            String coins_given = coins_found.get("Coins_Given").toString();
            String coins_received = coins_found.get("Coins_Received").toString();
            String coins_roll_lost = coins_found.get("Coins_Roll_Lost").toString();
            String coins_roll_earned = coins_found.get("Coins_Roll_Earned").toString();
            String coins_removed = coins_found.get("Coins_Removed").toString();
            //System.out.println("Test 4 Fired");
            event.reply(new Basic(tagged_user.getName() + "'s Piggy Bank",
                    "**SUMMARY**\n\n" +
                            ":moneybag: **" + total_coins + " "+curr_name+"** in total.\n\n" +
                            ":door: **" + coins_from_invites + " "+curr_name+"** are from invites.\n\n" +
                            ":speech_balloon: **" + coins_from_messages + " "+curr_name+"** are from messages.\n\n" +
                            ":gift: **" + coins_given + " "+curr_name+"** have been sent by you.\n\n" +
                            ":sunglasses: **" + coins_received + " "+curr_name+"** have been given to you.\n\n" +
                            ":grimacing: **" + coins_roll_lost + " "+curr_name+"** have been rolled away.\n\n" +
                            ":money_mouth: **" + coins_roll_earned + " "+curr_name+"** have been earned by rolling.\n\n" +
                            ":cop:  **" + coins_removed + " "+curr_name+"** have been removed.\n\n"
                    ,
                    event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

        } catch (ArrayIndexOutOfBoundsException e) {

            //System.out.println("No Arg was input");

            event.reply(new ErrorEmbed("Error", "Not enough arguments.", event.getGuild()).getError().build());

        }
    }
}


