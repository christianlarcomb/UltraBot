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

package com.ultra.bot.commands.tools;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Mongo extends Command {

    public Mongo() {
        this.name = "mongo";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Mongo", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;


        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        CmdArgGetter cmdArgGetter = new CmdArgGetter();

        try {

            if (cmdArgGetter.getArgs(event)[1].equals("status")) {

                // Check if its an OP
                Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
                if (event.getMember().getRoles().contains(OPRole)) {

                    String data_status;

                    if (MongoConnect.getMongoClient().getDB("BotData").collectionExists(event.getGuild().getId())) {
                        data_status = ":white_check_mark:";
                    } else {
                        data_status = ":x:";
                    }

                    event.reply(new Basic("Mongo Connection Data",

                            "**Discord Guild Data Status**\n" +
                                    data_status + "\n\n" +

                                    "**Local Threshhold**\n" +
                                    MongoConnect.getMongoClient().getMongoClientOptions().getLocalThreshold() + "\n\n" +

                                    "**Connection Timeout**\n" +
                                    MongoConnect.getMongoClient().getMongoClientOptions().getConnectTimeout() + "\n\n" +

                                    "**Connections Per Host**\n" +
                                    MongoConnect.getMongoClient().getMongoClientOptions().getConnectionsPerHost() + "\n\n" +

                                    "**DB Heartbeat Frequency**\n" +
                                    MongoConnect.getMongoClient().getMongoClientOptions().getHeartbeatFrequency() + "\n\n" +

                                    "**Read Concern**\n" +
                                    MongoConnect.getMongoClient().getDatabase("BotData").getReadConcern().toString() + "\n\n"

                            , "https://webassets.mongodb.com/_com_assets/cms/mongodb_logo1-76twgcu2dm.png", event.getGuild()).getBasic().build());

                    /** Closing Connection **/
                    //MongoConnect.closingDatabase();
                } else {
                    event.reply(new ErrorEmbed("Error", "Invalid permissions.", event.getGuild()).getError().build());
                }

            } else if (cmdArgGetter.getArgs(event)[1].equals("upload")) {

                // Check if its an OP
                Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
                if (event.getMember().getRoles().contains(OPRole)) {

                    // Looping through all users
                    for (int i = 0; i < event.getGuild().getMembers().size(); i++) {

                        User user = event.getGuild().getMembers().get(i).getUser();

                        // CACHED_MONGO >> FINDING: The document Config with the identifier Guild
                        Document user_doc = (Document) MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).find(new Document("User", user.getId())).first();

                        /** CHECKING USER DOCS **/

                        if (user_doc == null) {

                            Document unfound_user_doc = new Document("User", user.getId());

                            unfound_user_doc.append("Discord_Tag", user.getName() + "#" + user.getDiscriminator());
                            unfound_user_doc.append("Avatar_URL", user.getAvatarUrl());
                            unfound_user_doc.append("Acc_Creation_Date", user.getCreationTime().toLocalDate().toString());
                            unfound_user_doc.append("Global_Ban", false);
                            unfound_user_doc.append("Global_Dunce", false);
                            unfound_user_doc.append("Coins", 0);
                            unfound_user_doc.append("Coins_From_Invites", 0);
                            unfound_user_doc.append("Coins_From_Messages", 0);
                            unfound_user_doc.append("Coins_Given", 0);
                            unfound_user_doc.append("Coins_Received", 0);
                            unfound_user_doc.append("Coins_Roll_Lost", 0);
                            unfound_user_doc.append("Coins_Roll_Earned", 0);
                            unfound_user_doc.append("Coins_Removed", 0);
                            unfound_user_doc.append("Invite_Count", 0);

                            // INSERT: User Doc to DB
                            MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).insertOne(unfound_user_doc);


                            event.reply("Wo there! " + user + " was not found in the db! Now you're added and all set.");

                        } else {

                            Document found_user_doc = new Document("User", user.getId());
                            boolean user_updated = false;

                            if (!user_doc.containsKey("Discord_Tag")) {
                                found_user_doc.append("Discord_Tag", user.getName() + "#" + user.getDiscriminator());
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Avatar_URL")) {
                                found_user_doc.append("Avatar_URL", user.getAvatarUrl());
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Acc_Creation_Date")) {
                                found_user_doc.append("Acc_Creation_Date", user.getCreationTime().toLocalDate().toString());
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Global_Ban")) {
                                found_user_doc.append("Global_Ban", false);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Global_Dunce")) {
                                found_user_doc.append("Global_Dunce", false);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins")) {
                                found_user_doc.append("Coins", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_From_Invites")) {
                                found_user_doc.append("Coins_From_Invites", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_From_Messages")) {
                                found_user_doc.append("Coins_From_Messages", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_Given")) {
                                found_user_doc.append("Coins_Given", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_Received")) {
                                found_user_doc.append("Coins_Received", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_Roll_Lost")) {
                                found_user_doc.append("Coins_Roll_Lost", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_Roll_Earned")) {
                                found_user_doc.append("Coins_Roll_Earned", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Coins_Removed")) {
                                found_user_doc.append("Coins_Removed", 0);
                                user_updated = true;
                            }
                            if (!user_doc.containsKey("Invite_Count")) {
                                found_user_doc.append("Invite_Count", 0);
                                user_updated = true;
                            }

                            if (user_updated) {
                                Document mongo_user_found = (Document) MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).find(new Document("User", user.getId())).first();
                                Bson user_update_operation = new Document("$set", found_user_doc);
                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(mongo_user_found, user_update_operation);

                                event.reply("Hmm.. " + user + " was found in the db but was missing some things.. \n\nLet's fix em up and get them on their way... \n\n**All set!**");
                                //System.out.println("USER WAS FOUND ~ INSERTED BITS OF DATA");
                            } else {
                                //System.out.println("USER WAS FOUND ~ USER HAS ALL DATA ELEMENTS!");
                            }

                        }
                    }

                } else {
                    event.reply(new ErrorEmbed("Error", "Invalid permissions.", event.getGuild()).getError().build());
                }
            }

        } catch (ArrayIndexOutOfBoundsException e){
            event.reply(":x: **Oops!** Not enough details!\n" +
                    "```u mongo status   //  Connection summary to the Mongo Database.\n" +
                       "u mongo upload   //  Verify and Upload necessary documents to the Database.```");
        }
    }
}
