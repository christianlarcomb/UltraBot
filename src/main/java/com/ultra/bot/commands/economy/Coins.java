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

package com.ultra.bot.commands.economy;

import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.converters.TagToUser;
import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import org.bson.Document;
import org.bson.conversions.Bson;

public class Coins extends Command {

    /* CLASS USES MONGODB */

    public Coins() {
        this.name = "$";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.aliases = new String[]{
                "c",
                "money",
                "cash",
                "coin",
                "racks"
        };
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Currency", true))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        /** Channel Cleanup **/
        new PurgeMsgs().purgeMessages(event, 1);


        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

        // Try to find an arg, if none, catch it and go forward.

        Document self_user_found = (Document) mongoCollection.find(new Document("User", event.getMember().getUser().getId())).first();

        Document currency_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();
        String currency_name = currency_doc_found.getString("Currency_Name");
        int inv_coin_amt = currency_doc_found.getInteger("Amt_Earned_Inviting");
        int voice_coin_amt = currency_doc_found.getInteger("Amt_Earned_VoiceChannel");
        int txt_coin_amt = currency_doc_found.getInteger("Amt_Earned_TextChannel");

        try {

            CmdArgGetter cag = new CmdArgGetter();

            String sub_command = cag.getArgs(event)[1];

            //System.out.println("Test 1 Fired");

            if (sub_command.equals("summary")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "summary"))
                    return;

                String total_coins = self_user_found.get("Coins").toString().toLowerCase();

                // System.out.println("Test 3 Fired");
                String coins_from_invites = self_user_found.get("Coins_From_Invites").toString();
                String coins_from_messages = self_user_found.get("Coins_From_Messages").toString();
                String coins_given = self_user_found.get("Coins_Given").toString();
                String coins_received = self_user_found.get("Coins_Received").toString();
                String coins_roll_lost = self_user_found.get("Coins_Roll_Lost").toString();
                String coins_roll_earned = self_user_found.get("Coins_Roll_Earned").toString();
                String coins_removed = self_user_found.get("Coins_Removed").toString();

                event.reply(new Basic(event.getMember().getUser().getName() + "'s Piggy Bank",
                        "**SUMMARY**\n\n" +
                                ":moneybag: **" + total_coins + " "+currency_name+"** in total.\n\n" +
                                ":door: **" + coins_from_invites + " "+currency_name+"** are from invites.\n\n" +
                                ":speech_balloon: **" + coins_from_messages + " "+currency_name+"** are from messages.\n\n" +
                                ":gift: **" + coins_given + " "+currency_name+"** have been sent by you.\n\n" +
                                ":sunglasses: **" + coins_received + " "+currency_name+"** have been given to you.\n\n" +
                                ":grimacing: **" + coins_roll_lost + " "+currency_name+"** have been rolled away.\n\n" +
                                ":money_mouth: **" + coins_roll_earned + " "+currency_name+"** have been earned by rolling.\n\n" +
                                ":cop:  **" + coins_removed + " "+currency_name+"** have been removed.\n\n"
                        ,
                        "http://pngimg.com/uploads/coin/coin_PNG36893.png", event.getGuild()).getBasic().build());


            } else if (sub_command.equals("remove")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "remove"))
                    return;

                Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
                if (event.getMember().getRoles().contains(OPRole)) {

                    TagToUser tagToUser = new TagToUser();
                    User tagged_user = tagToUser.tagToUser(cag.getArgs(event)[2]);

                    Document user_found = (Document) MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).find(new Document("User", tagged_user.getId())).first();
                    Document doc_to_insert = new Document("User", tagged_user.getId());

                    // MANAGING: Variables
                    int coin_difference_amt = Integer.parseInt(cag.getArgs(event)[3]);
                    int coin_total_calculated = user_found.getInteger("Coins") - coin_difference_amt;
                    int coin_removed_calculated = user_found.getInteger("Coins_Removed") + coin_difference_amt;

                    doc_to_insert.append("Coins", coin_total_calculated);
                    doc_to_insert.append("Coins_Removed", coin_removed_calculated);

                    // UPDATING THE COINS IN THE DB
                    Bson updateoperation = new Document("$set", doc_to_insert);
                    MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(user_found, updateoperation);

                    // OUTPUT: The proper information
                    String mod_name = event.getMember().getUser().getName();
                    String user_name = tagToUser.tagToUser(cag.getArgs(event)[2]).getName();

                    event.reply("**" + mod_name + "** has removed **" + coin_difference_amt + "** coins from **" + user_name + "**.");


                } else {
                    event.reply(new ErrorEmbed("Error", "Invalid permissions.", event.getGuild()).getError().build());
                }


            } else if (sub_command.equalsIgnoreCase("give")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "give"))
                    return;

                /*                                                                                              */
                //Make this a public command, if OP'd, it doesn't remove from the existing balance. etc.//
                /*                                                                                              */

                Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
                if (event.getMember().getRoles().contains(OPRole)) {

                    TagToUser tagToUser = new TagToUser();
                    User tagged_user = tagToUser.tagToUser(cag.getArgs(event)[2]);
                    User giver_user = event.getMember().getUser();

                    Document tagged_user_found = (Document) MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).find(new Document("User", tagged_user.getId())).first();
                    Document giver_user_found = (Document) MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).find(new Document("User", event.getMember().getUser().getId())).first();

                    Document receiver_doc_insert = new Document("User", tagged_user.getId());
                    Document giver_doc_insert = new Document("User", giver_user.getId());

                    // MANAGING: Variables
                    int coin_sum_amt = Integer.parseInt(cag.getArgs(event)[3]);

                    int coin__total_calculated = tagged_user_found.getInteger("Coins") + coin_sum_amt;
                    int coin_received_calculated = tagged_user_found.getInteger("Coins_Received") + coin_sum_amt;

                    int coins_given_calculated = giver_user_found.getInteger("Coins_Given") + coin_sum_amt;

                    // Appending Receiver Doc
                    receiver_doc_insert.append("Coins", coin__total_calculated);
                    receiver_doc_insert.append("Coins_Received", coin_received_calculated);

                    // Appending Giver Doc
                    giver_user_found.append("Coins_Given", coins_given_calculated);

                    // UPDATING: Receivers Coins
                    Bson updateoperation1 = new Document("$set", receiver_doc_insert);
                    MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(tagged_user_found, updateoperation1);

                    // UPDATING: Givers Coins
                    Bson updateoperation2 = new Document("$set", giver_doc_insert);
                    MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(giver_user_found, updateoperation2);

                    // OUTPUT: The proper information
                    String mod_name = event.getMember().getUser().getName();
                    String user_name = tagToUser.tagToUser(cag.getArgs(event)[2]).getName();
                    event.reply("**" + mod_name + "** has given **" + coin_sum_amt + "** coins to **" + user_name + "**.");


                } else { event.reply(new ErrorEmbed("Error", "Invalid permissions.", event.getGuild()).getError().build()); }


            } else if (sub_command.equalsIgnoreCase("guide")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "guide"))
                    return;

                event.reply(new Basic("Coin Earning Guide",
                        "Read this guide in depth and you are on your way to get shop items." +
                                "\n\n" +
                                ":white_small_square: **HOW TO EARN** :white_small_square:" +
                                "\n\n" +
                                ":sunglasses: **Invite Friends** -> **" + inv_coin_amt +" " + currency_name + "** *per invite*" +
                                "\n" +
                                "``sir coins invite`` for more info." +
                                "\n\n" +

                                ":speech_balloon: **Text Channels** -> **" + txt_coin_amt +" " + currency_name + "** *per 100 messages*" +
                                "\n\n" +

                                ":microphone2: ~~**Voice Channels** -> **" + voice_coin_amt +" " + currency_name + "** *per hour*~~\n" +
                                "(coming soon)\n\n" +

                                ":white_small_square: **BUTLER SHOP** :white_small_square:" +
                                "\n\n" +
                                "The Dynamic Shop Feature (DSF) for the Butler Bot is **almost released**! (Just fixing up some things!)\n\n" +

                                "**TO -> SHOP**\n" +
                                "All you'll need to do is either access the shop menu in one of the text channels or use the following command: \n\n" +
                                "**>**  ``sir shop``",
                        "http://pngimg.com/uploads/coin/coin_PNG36893.png", event.getGuild()).getBasic().build());

            } else if (sub_command.equalsIgnoreCase("invite")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "invite"))
                    return;

                String auth_name = event.getAuthor().getName();

                event.getChannel().sendMessage(new Basic("Invite Code Process",
                        "To create your permanent invite code which will earn you coins " + event.getAuthor().getAsMention() + ", you'll need to follow these instructions:" +
                                "\n\n" +
                                ":triangular_flag_on_post: **STEPS** :triangular_flag_on_post:\n\n" +
                                ":one: Right click __" + event.getGuild().getName() + "__'s Server Icon and select \"Invite People\" which should be highlighted in blue.\n\n" +
                                ":two: Left Click the bottom hyperlink \"Edit invite link.\" \n\n" +
                                ":three: Follow the prompt and set \"EXPIRE AFTER\" to \"Never\" and set \"MAX NUMBER OF USES\" to \"No Limit\". (ignore \"Grant temporary membership\")\n\n" +
                                "**ALL DONE!** \n" +
                                "Share your invite link as much as you would like and earn coins for " + event.getGuild().getName() + ". *Use your coins wisely when shopping!*"
                        , "http://pngimg.com/uploads/coin/coin_PNG36893.png", event.getGuild()).getBasic().build()).queue();

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            try {
                event.reply("**" + event.getMember().getAsMention() + "**, you have **" + self_user_found.get("Coins").toString() + " "+currency_name+"**.");
            } catch (NullPointerException e3) {
                e3.printStackTrace();
            }

        }

    }
}

