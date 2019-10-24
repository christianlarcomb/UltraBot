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

package com.ultra.bot.commands.config;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.data.BulkMessages;
import com.ultra.bot.utilities.data.EmoteArrays;
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Configuration extends Command {

    /*
    To configure a feature:
    --------------------------------------------------
    u! WelcomeMessage <list, add, remove, enable, disable> <#> <content>
        >> Guild Info <<
            [guild_owner_name]
            [guild_owner_mention]
            [guild_name]    <- add this!
        >> Event User Info <<
            [event_user_name]
            [event_user_mention]
        >> Bot User Info
            [self_user_name]
            [self_user_mention]
        >> Counts <<
            [guild_member_count]
            [bot_member_count]
            [bot_guild_count]
        >> Thumbnails <<
            [guild_owner_thumbnail]
            [event_user_thumbnail]
            [self_user_thumbnail]

    ----------------------[ DONE ]-------------------------
    u! Toggle <all, Command, Feature> <cmd, ftr, cmd name>
    u! Currency <setup, reset>
    u! WelcomeMessage <set, reset> <#>
    u! JoinRoles <set, reset> <#>

    --------------------[ NOT DONE ]-----------------------
    u! AutoCmds
    u! RewardRoles

    */


    private final EventWaiter waiter;
    private PurgeMsgs purgeMsgs = new PurgeMsgs();

    private HashMap<Guild, Integer> toggle_count = new HashMap<>();

    public Configuration(EventWaiter waiter) {
        this.name = "!";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.waiter = waiter;
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /* Setup Checker */
        if (!new Checks().SetupCompCheck(event))
            return;

        /* Dunce Checker */
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        EmoteArrays em = new EmoteArrays();
        BulkMessages bulkMessages = new BulkMessages();

        // CHECKING IF THE USER IS A BOT
        if (!event.getMember().getUser().isBot()) {

            CmdArgGetter cmdArgGetter = new CmdArgGetter();


            Role OPRole = event.getGuild().getRolesByName("*", true).get(0);

            if (event.getMember().getRoles().contains(OPRole)) {

                try {


                    // Getting the Guilds Collection
                    MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

                    String first_arg = cmdArgGetter.getArgs(event)[1];

                    if (first_arg.equalsIgnoreCase("welcomemessage") || first_arg.equalsIgnoreCase("wm"))
                    {

                        // Getting the WelcomeMessages DB docs
                        Document wm_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first();

                        // Try the second_arg
                        try {
                            if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("set"))
                            {

                                int msg_num = Integer.parseInt(cmdArgGetter.getArgs(event)[3]);
                                // Checking if the message number is too high
                                if (msg_num > 3 || msg_num <= 0) {
                                    event.reply(":x: **Whoops!** You've selected an invalid number! \n``Please select a number 1 through 5!``");
                                    return;
                                }

                                event.reply(":one: Please enter the Message #" + msg_num + " Text:" +
                                        "\n\n" +
                                        "if nothing, enter the `-` mark.");

                                /******** WAITER #2 [Getting Description for ] ********/
                                waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()), e -> {

                                            // PURGE: Old message
                                            purgeMsgs.purgeMessages(event, 2);


                                            event.reply(":two: Please enter the Welcome Message #" + msg_num + " Thumbnail URL:" +
                                                    "\n\n" +
                                                    "if nothing, enter the `-` mark.");


                                            /******** WAITER #2 [Getting Description for ] ********/
                                            waiter.waitForEvent(GuildMessageReceivedEvent.class, e2 -> e2.getAuthor().equals(event.getAuthor()), e2 -> {

                                                        // PURGE: Old message
                                                        purgeMsgs.purgeMessages(event, 2);

                                                        event.reply(":three: Please enter the Welcome Message #" + msg_num + " Image URL:" +
                                                                "\n\n" +
                                                                "if nothing, enter the `-` mark.");


                                                        /******** WAITER #2 [Getting Description for ] ********/
                                                        waiter.waitForEvent(GuildMessageReceivedEvent.class, e3 -> e3.getAuthor().equals(event.getAuthor()), e3 -> {

                                                                    // PURGE: Old message
                                                                    purgeMsgs.purgeMessages(event, 2);


                                                                    event.reply(":four: Please tag the Text-Channel that's going to be used:" +
                                                                            "\n\n" +
                                                                            "If using the default text-channel, enter the `-` mark.");

                                                                    /******** WAITER #2 [Getting Description for ] ********/
                                                                    waiter.waitForEvent(GuildMessageReceivedEvent.class, e4 -> e4.getAuthor().equals(event.getAuthor()), e4 -> {

                                                                                // PURGE: Old message
                                                                                purgeMsgs.purgeMessages(event, 2);

                                                                                String msg_text, msg_thumbnail, msg_image, msg_channel;
                                                                                msg_text = e.getMessage().getContentRaw().replace("-", "");
                                                                                msg_thumbnail = e2.getMessage().getContentRaw().replace("-", "");
                                                                                msg_image = e3.getMessage().getContentRaw().replace("-", "");
                                                                                msg_channel = e4.getMessage().getContentRaw().replace("-", "");

                                                                                /* The Details That Will Be Set */
                                                                                Document doc_to_insert = new Document("Features", "WelcomeMessage");
                                                                                doc_to_insert.put("TextChannel", msg_channel);
                                                                                doc_to_insert.put("Message_" + msg_num + "_Text", msg_text);
                                                                                doc_to_insert.put("Message_" + msg_num + "_Thumbnail", msg_thumbnail);
                                                                                doc_to_insert.put("Message_" + msg_num + "_Image", msg_image);

                                                                                Bson updateoperation = new Document("$set", doc_to_insert);
                                                                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(wm_doc_found, updateoperation);

                                                                                event.reply(":white_check_mark: **Alright!** Welcome Message #" + msg_num + " is good to go!");


                                                                            }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                                            new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build())
                                                                    );


                                                                }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                                new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build())
                                                        );


                                                    }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                    new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build())
                                            );


                                        }, 60, TimeUnit.SECONDS, () -> event.reply(
                                        new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build())
                                );


                            }
                            else if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("reset"))
                            {

                                int msg_num = Integer.parseInt(cmdArgGetter.getArgs(event)[3]);
                                // Checking if the message number is too high
                                if (msg_num > 3 || msg_num <= 0) {
                                    event.reply(":x: **Whoops!** You've selected an invalid number! \n``Please select a number 1 through 5!``");
                                    return;
                                }

                                Document doc_to_insert = new Document("Features", "WelcomeMessage");
                                doc_to_insert.put("Message_" + msg_num + "_Text", "");
                                doc_to_insert.put("Message_" + msg_num + "_Thumbnail", "");
                                doc_to_insert.put("Message_" + msg_num + "_Image", "");

                                Bson updateoperation = new Document("$set", doc_to_insert);
                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(wm_doc_found, updateoperation);

                                event.reply(":white_check_mark: **All done!** Welcome Message #" + msg_num + " has been reset!");

                            }
                            else if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("shortcuts")) {

                                event.reply(":book: **All Shortcuts!**" +
                                        "\n\n" +

                                        "**Guild Info**" +
                                        "\n" +
                                        "``[guild_owner_name]``" +
                                        "\n" +
                                        "``[guild_owner_mention]``" +
                                        "\n" +
                                        "``[guild_name]``" + // ADD THIS !!!!
                                        "\n\n" +

                                        "**Event User Info**" +
                                        "\n" +
                                        "``[event_user_name]``" +
                                        "\n" +
                                        "``[event_user_mention]``" +
                                        "\n\n" +

                                        "**Bot User Info**" +
                                        "\n" +
                                        "``[self_user_name]``" +
                                        "\n" +
                                        "``[self_user_mention]``" +
                                        "\n\n" +

                                        "**Counts**" +
                                        "\n" +
                                        "``[guild_member_count]``" +
                                        "\n" +
                                        "``[bot_member_count]``" +
                                        "\n" +
                                        "``[bot_guild_count]``" +
                                        "\n\n" +

                                        "**Images**" +
                                        "\n" +
                                        "``[guild_owner_thumbnail]``" +
                                        "\n" +
                                        "``[event_user_thumbnail]``" +
                                        "\n" +
                                        "``[self_user_thumbnail]``"
                                );

                                /*

        >> Counts <<
            [guild_member_count]
            [bot_member_count]
            [bot_guild_count]
        >> Thumbnails <<
            [guild_owner_thumbnail]
            [event_user_thumbnail]
            [self_user_thumbnail]

    ----------------------[ DONE ]-------------------------
    u! Toggle <all, Command, Feature> <cmd, ftr, cmd name>
    u! Currency <setup, reset>
    u! WelcomeMessage <set, reset> <#>
    u! JoinRoles <set, reset> <#>

    --------------------[ NOT DONE ]-----------------------
    u! AutoCmds
    u! RewardRoles

    */

                            }

                        } catch (Exception e) {
                            event.reply("Second arg not found!");
                            e.printStackTrace();
                        }

                    }
                    else if (first_arg.equalsIgnoreCase("currency"))
                    {

                        // Getting the WelcomeMessages DB docs
                        Document wm_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();

                        // Try the second_arg
                        try {

                            if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("setup"))
                            {


                                event.reply(":one: What is the name of the **currency** in **plural form**?" +
                                        "\n\n" +
                                        "if default `Coins`, enter the `-` mark.");

                                /******** WAITER #1 ********/
                                waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()), e -> {

                                            // PURGE: Old message
                                            purgeMsgs.purgeMessages(event, 2);

                                            event.reply(":two: Will the currency be **tradable** among one another in the server?");


                                            // ADDING THE EMOTES FOR SELECTION
                                            List<Message> messageOne = event.getChannel().getHistory().retrievePast(1).complete();
                                            for (int i = 0; i < em.getOptions().size(); i++)
                                                messageOne.get(0).addReaction(em.getOptions().get(i)).queue();

                                            /******** WAITER #2 ********/
                                            waiter.waitForEvent(GuildMessageReactionAddEvent.class, e2 -> e2.getMember().equals(event.getMember()), e2 -> {


                                                        // PURGE: Old message
                                                        purgeMsgs.purgeMessages(event, 2);

                                                        event.reply(":three: How many ``" + e.getMessage().getContentRaw() + "`` will be earned from **invites**?");


                                                        /******** WAITER #3 ********/
                                                        waiter.waitForEvent(GuildMessageReceivedEvent.class, e3 -> e3.getAuthor().equals(event.getAuthor()), e3 -> {

                                                                    // PURGE: Old message
                                                                    purgeMsgs.purgeMessages(event, 2);

                                                                    event.reply(":three: How many ``" + e.getMessage().getContentRaw() + "`` will be earned from **voice chatting**?");


                                                                    /******** WAITER #4 ********/
                                                                    waiter.waitForEvent(GuildMessageReceivedEvent.class, e4 -> e4.getAuthor().equals(event.getAuthor()), e4 -> {

                                                                                // PURGE: Old message
                                                                                purgeMsgs.purgeMessages(event, 2);

                                                                                event.reply(":three: How many ``" + e.getMessage().getContentRaw() + "`` will be earned from **messaging**?");


                                                                                /******** WAITER #5 ********/
                                                                                waiter.waitForEvent(GuildMessageReceivedEvent.class, e5 -> e5.getAuthor().equals(event.getAuthor()), e5 -> {

                                                                                            // PURGE: Old message
                                                                                            purgeMsgs.purgeMessages(event, 2);

                                                                                            // Getting the selected button
                                                                                            boolean tradable;
                                                                                            if (e2.getReactionEmote().getName().contains("\uD83C\uDDFE")) {
                                                                                                tradable = true;
                                                                                            } else {
                                                                                                tradable = false;
                                                                                            }

                                                                                            String currency_name = e.getMessage().getContentRaw();
                                                                                            int
                                                                                                    invites = Integer.parseInt(e3.getMessage().getContentRaw()),
                                                                                                    voice_chat = Integer.parseInt(e4.getMessage().getContentRaw()),
                                                                                                    messaging = Integer.parseInt(e5.getMessage().getContentRaw());


                                                                                            /* The Details That Will Be Set */
                                                                                            Document doc_to_insert = new Document("Features", "Currency");
                                                                                            doc_to_insert.put("Tradable", tradable);
                                                                                            doc_to_insert.put("Currency_Name", currency_name);
                                                                                            doc_to_insert.put("Amt_Earned_Inviting", invites);
                                                                                            doc_to_insert.put("Amt_Earned_VoiceChannel", voice_chat);
                                                                                            doc_to_insert.put("Amt_Earned_TextChannel", messaging);

                                                                                            Bson updateoperation = new Document("$set", doc_to_insert);
                                                                                            MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(wm_doc_found, updateoperation);


                                                                                            event.reply(":white_check_mark: **Success!** The ``"+currency_name+"`` Currency is all setup!");


                                                                                        }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                                                        new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 60 seconds", event.getGuild()).getError().build())
                                                                                );


                                                                            }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                                            new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 60 seconds", event.getGuild()).getError().build())
                                                                    );


                                                                }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                                new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 60 seconds", event.getGuild()).getError().build())
                                                        );


                                                    }, 60, TimeUnit.SECONDS, () -> event.reply(
                                                    new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 60 seconds", event.getGuild()).getError().build())
                                            );


                                        }, 60, TimeUnit.SECONDS, () -> event.reply(
                                        new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 60 seconds", event.getGuild()).getError().build())
                                );


                            }
                            else if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("reset")) {

                                /* The Details That Will Be Set */
                                Document doc_to_insert = new Document("Features", "Currency");
                                doc_to_insert.put("Tradable", false);
                                doc_to_insert.put("Currency_Name", "Coins");
                                doc_to_insert.put("Amt_Earned_Inviting", 1);
                                doc_to_insert.put("Amt_Earned_VoiceChannel", 1);
                                doc_to_insert.put("Amt_Earned_TextChannel", 1);

                                Bson updateoperation = new Document("$set", doc_to_insert);
                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(wm_doc_found, updateoperation);

                                event.reply(":white_check_mark: **All done!** All Currency configurations have been reset!");

                            }

                        } catch (Exception e) {
                            event.reply(":x: **Whoops!** Don't forget what you're doing with the Currency: \n``setup`` or ``reset``.");
                            //e.printStackTrace();
                        }

                    }
                    else if (first_arg.equalsIgnoreCase("joinroles"))
                    {

                        try{

                            if(cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("set")){

                                Document jroles_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first();

                                int role_to_set = Integer.parseInt(cmdArgGetter.getArgs(event)[3]);
                                String role_tagged = cmdArgGetter.getArgs(event)[4];

                                // Checking
                                if(role_to_set > MetaData.Join_Roles_Count || role_to_set < 0){
                                    event.reply(":x: **Whoops!** You've entered an incorrect number!");
                                    return;
                                }


                                /* The Details That Will Be Set */
                                Document doc_to_insert = new Document("Features", "JoinRoles");
                                doc_to_insert.put("Role_"+role_to_set, role_tagged);

                                Bson updateoperation = new Document("$set", doc_to_insert);
                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(jroles_doc_found, updateoperation);

                                event.reply(":white_check_mark: **All done!** **Role #"+role_to_set+"** has been set!");

                            } else if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("reset")) {

                                Document jroles_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first();

                                int role_to_reset = Integer.parseInt(cmdArgGetter.getArgs(event)[3]);

                                // Checking
                                if(role_to_reset > MetaData.Join_Roles_Count || role_to_reset < 0){
                                    event.reply(":x: **Whoops!** You've entered an incorrect number!");
                                    return;
                                }


                                /* The Details That Will Be Set */
                                Document doc_to_insert = new Document("Features", "JoinRoles");
                                doc_to_insert.put("Role_"+role_to_reset, "");

                                Bson updateoperation = new Document("$set", doc_to_insert);
                                MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(jroles_doc_found, updateoperation);

                                event.reply(":white_check_mark: **All done!** **Role #"+role_to_reset+"** has been reset!");

                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(":x: **Uh oh!** Not enough arguements!");
                        }

                    }
                    else if (first_arg.equalsIgnoreCase("autocmds"))
                    {
                    }
                    else if (first_arg.equalsIgnoreCase("rewardroles"))
                    {
                    }
                    else if (first_arg.equalsIgnoreCase("toggle"))
                    {

                        try {

                            if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("all")) {

                                /* All Feature Docs */
                                Document wm_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first(),
                                        autocmd_doc_found = (Document) mongoCollection.find(new Document("Features", "AutoCommand")).first(),
                                        joinroles_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first(),
                                        //rewardroles_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first(),
                                        currency_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first(),
                                        memcntchan_doc_found = (Document) mongoCollection.find(new Document("Features", "MemberCountChannel")).first();


                                /* All Command Docs */
                                Document coins_doc_found = (Document) mongoCollection.find(new Document("Commands", "Coins")).first(),
                                        shop_doc_found = (Document) mongoCollection.find(new Document("Commands", "Shop")).first(),
                                        askreddit_doc_found = (Document) mongoCollection.find(new Document("Commands", "AskReddit")).first(),
                                        cancer_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cancer")).first(),
                                        cats_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cats")).first(),
                                        coinflip_doc_found = (Document) mongoCollection.find(new Document("Commands", "CoinFlip")).first(),
                                        dogs_doc_found = (Document) mongoCollection.find(new Document("Commands", "Dogs")).first(),
                                        magic8ball_doc_found = (Document) mongoCollection.find(new Document("Commands", "Magic8Ball")).first(),
                                        meme_doc_found = (Document) mongoCollection.find(new Document("Commands", "Meme")).first(),
                                        rolldice_doc_found = (Document) mongoCollection.find(new Document("Commands", "RollDice")).first(),
                                        suckmy_doc_found = (Document) mongoCollection.find(new Document("Commands", "SuckMy")).first(),
                                        guessthat_doc_found = (Document) mongoCollection.find(new Document("Commands", "GuessThat")).first(),
                                        rocketleague_doc_found = (Document) mongoCollection.find(new Document("Commands", "RocketLeague")).first(),
                                        about_doc_found = (Document) mongoCollection.find(new Document("Commands", "About")).first(),
                                        cmds_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cmds")).first(),
                                        help_doc_found = (Document) mongoCollection.find(new Document("Commands", "Help")).first(),
                                        dunce_doc_found = (Document) mongoCollection.find(new Document("Commands", "Dunce")).first(),
                                        undunce_doc_found = (Document) mongoCollection.find(new Document("Commands", "Undunce")).first(),
                                        emoteid_doc_found = (Document) mongoCollection.find(new Document("Commands", "EmoteID")).first(),
                                        guild_doc_found = (Document) mongoCollection.find(new Document("Commands", "Guild")).first(),
                                        mongo_doc_found = (Document) mongoCollection.find(new Document("Commands", "Mongo")).first(),
                                        purge_doc_found = (Document) mongoCollection.find(new Document("Commands", "Purge")).first(),
                                        roleid_doc_found = (Document) mongoCollection.find(new Document("Commands", "RoleID")).first(),
                                        version_doc_found = (Document) mongoCollection.find(new Document("Commands", "Version")).first(),
                                        suggest_doc_found = (Document) mongoCollection.find(new Document("Commands", "Suggest")).first(),
                                        users_doc_found = (Document) mongoCollection.find(new Document("Commands", "Users")).first();

                                // Putting all documents into a list
                                List<Document> all_ftr_docs = new ArrayList<>();
                                List<Document> all_cmd_docs = new ArrayList<>();
                                all_ftr_docs.add(wm_doc_found);
                                all_ftr_docs.add(autocmd_doc_found);
                                all_ftr_docs.add(joinroles_doc_found);
                                all_ftr_docs.add(currency_doc_found);
                                all_ftr_docs.add(memcntchan_doc_found);

                                all_cmd_docs.add(coins_doc_found);
                                all_cmd_docs.add(shop_doc_found);
                                all_cmd_docs.add(askreddit_doc_found);
                                all_cmd_docs.add(cancer_doc_found);
                                all_cmd_docs.add(cats_doc_found);
                                all_cmd_docs.add(coinflip_doc_found);
                                all_cmd_docs.add(dogs_doc_found);
                                all_cmd_docs.add(magic8ball_doc_found);
                                all_cmd_docs.add(meme_doc_found);
                                all_cmd_docs.add(rolldice_doc_found);
                                all_cmd_docs.add(suckmy_doc_found);
                                all_cmd_docs.add(guessthat_doc_found);
                                all_cmd_docs.add(rocketleague_doc_found);
                                all_cmd_docs.add(about_doc_found);
                                all_cmd_docs.add(cmds_doc_found);
                                all_cmd_docs.add(help_doc_found);
                                all_cmd_docs.add(dunce_doc_found);
                                all_cmd_docs.add(undunce_doc_found);
                                all_cmd_docs.add(emoteid_doc_found);
                                all_cmd_docs.add(guild_doc_found);
                                all_cmd_docs.add(mongo_doc_found);
                                all_cmd_docs.add(purge_doc_found);
                                all_cmd_docs.add(roleid_doc_found);
                                all_cmd_docs.add(version_doc_found);
                                all_cmd_docs.add(suggest_doc_found);
                                all_cmd_docs.add(users_doc_found);

                                Document update_doc = new Document();

                                // Setting value of 1 if absent
                                toggle_count.putIfAbsent(event.getGuild(), 0);

                                String toggle_status;
                                if (toggle_count.get(event.getGuild()) % 2 == 0) {
                                    update_doc.put("Enabled", true);
                                    toggle_count.put(event.getGuild(), toggle_count.get(event.getGuild()) + 1);
                                    toggle_status = "enabled";
                                } else {
                                    update_doc.put("Enabled", false);
                                    toggle_count.put(event.getGuild(), toggle_count.get(event.getGuild()) + 1);
                                    toggle_status = "disabled";
                                }

                                Bson updateoperation = new Document("$set", update_doc);

                                try {

                                    if (cmdArgGetter.getArgs(event)[3].equalsIgnoreCase("commands")) {

                                        // Updating all docs?
                                        for (Document document_found : all_cmd_docs) {
                                            MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(document_found, updateoperation);
                                        }

                                        event.reply(":white_check_mark: **Done!** All **Commands** have been **" + toggle_status.toUpperCase() + "**!");

                                        return;


                                    } else if (cmdArgGetter.getArgs(event)[3].equalsIgnoreCase("features")) {

                                        // Updating all docs?
                                        for (Document document_found : all_ftr_docs) {
                                            MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(document_found, updateoperation);
                                        }

                                        event.reply(":white_check_mark: **Done!** All **Features** have been **" + toggle_status.toUpperCase() + "**!");

                                        return;

                                    }

                                } catch (Exception e) {

                                    // Updating all docs?
                                    for (Document document_found : all_ftr_docs) {
                                        MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(document_found, updateoperation);
                                    }

                                    // Updating all docs?
                                    for (Document document_found : all_cmd_docs) {
                                        MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(document_found, updateoperation);
                                    }

                                    event.reply(":white_check_mark: **Done!** All **Commands** and **Features** have been **" + toggle_status.toUpperCase() + "**!");

                                    return;
                                }
                            }


                            // Necessary Variables
                            String ft_or_cmd_to_toggle = cmdArgGetter.getArgs(event)[2];
                            String ft_or_cmd_checker;

                            // Retrieving Necessary Documents
                            Document ft_doc_found = (Document) mongoCollection.find(new Document("Features", ft_or_cmd_to_toggle)).first();
                            Document cmd_doc_found = (Document) mongoCollection.find(new Document("Commands", ft_or_cmd_to_toggle)).first();

                            // Initializing a Document
                            Document toggler_doc_found;

                            // Checking if the Command or Feature Exists in the DB
                            if (ft_doc_found != null) {
                                ft_or_cmd_checker = "Features";
                                toggler_doc_found = ft_doc_found;
                            } else if (cmd_doc_found != null) {
                                ft_or_cmd_checker = "Commands";
                                toggler_doc_found = cmd_doc_found;
                            } else {
                                event.reply("**Uh oh... I couldn't find that Command or Feature!**");
                                return;
                            }

                            // Creating the Document to Insert into the DB
                            Document doc_to_insert = new Document(ft_or_cmd_checker, ft_or_cmd_to_toggle);

                            boolean enabled_check = toggler_doc_found.getBoolean("Enabled");
                            if (enabled_check) {
                                doc_to_insert.put("Enabled", false);
                                event.reply(":white_check_mark: **Done!** The **" + ft_or_cmd_to_toggle + "** " + ft_or_cmd_checker.replace("s", "") + " has been **DISABLED**.");
                            } else {
                                doc_to_insert.put("Enabled", true);
                                event.reply(":white_check_mark: **Done!** The **" + ft_or_cmd_to_toggle + "** " + ft_or_cmd_checker.replace("s", "") + " has been **ENABLED**.");
                            }
                            // UPDATING THE COINS IN THE DB
                            Bson updateoperation = new Document("$set", doc_to_insert);
                            MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId()).updateOne(toggler_doc_found, updateoperation);

                        } catch (Exception e) {
                            event.reply(":x: **Welp.** Not enough details were entered.");
                            //e.printStackTrace();
                        }

                    }
                    else if (first_arg.equalsIgnoreCase("list"))
                    {

                        /* All Feature Docs */
                        Document wm_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first(),
                                //autocmd_doc_found = (Document) mongoCollection.find(new Document("Features", "AutoCommand")).first(),
                                joinroles_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first(),
                                //rewardroles_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first(),
                                currency_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();
                                //memcntchan_doc_found = (Document) mongoCollection.find(new Document("Features", "MemberCountChannel")).first();


                        /* All Command Docs */
                        Document coins_doc_found = (Document) mongoCollection.find(new Document("Commands", "Coins")).first(),
                                shop_doc_found = (Document) mongoCollection.find(new Document("Commands", "Shop")).first(),
                                askreddit_doc_found = (Document) mongoCollection.find(new Document("Commands", "AskReddit")).first(),
                                cancer_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cancer")).first(),
                                cats_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cats")).first(),
                                coinflip_doc_found = (Document) mongoCollection.find(new Document("Commands", "CoinFlip")).first(),
                                dogs_doc_found = (Document) mongoCollection.find(new Document("Commands", "Dogs")).first(),
                                magic8ball_doc_found = (Document) mongoCollection.find(new Document("Commands", "Magic8Ball")).first(),
                                meme_doc_found = (Document) mongoCollection.find(new Document("Commands", "Meme")).first(),
                                rolldice_doc_found = (Document) mongoCollection.find(new Document("Commands", "RollDice")).first(),
                                //suckmy_doc_found = (Document) mongoCollection.find(new Document("Commands", "SuckMy")).first(),
                                guessthat_doc_found = (Document) mongoCollection.find(new Document("Commands", "GuessThat")).first(),
                                rocketleague_doc_found = (Document) mongoCollection.find(new Document("Commands", "RocketLeague")).first(),
                                about_doc_found = (Document) mongoCollection.find(new Document("Commands", "About")).first(),
                                cmds_doc_found = (Document) mongoCollection.find(new Document("Commands", "Cmds")).first(),
                                help_doc_found = (Document) mongoCollection.find(new Document("Commands", "Help")).first(),
                                dunce_doc_found = (Document) mongoCollection.find(new Document("Commands", "Dunce")).first(),
                                undunce_doc_found = (Document) mongoCollection.find(new Document("Commands", "Undunce")).first(),
                                emoteid_doc_found = (Document) mongoCollection.find(new Document("Commands", "EmoteID")).first(),
                                guild_doc_found = (Document) mongoCollection.find(new Document("Commands", "Guild")).first(),
                                mongo_doc_found = (Document) mongoCollection.find(new Document("Commands", "Mongo")).first(),
                                purge_doc_found = (Document) mongoCollection.find(new Document("Commands", "Purge")).first(),
                                roleid_doc_found = (Document) mongoCollection.find(new Document("Commands", "RoleID")).first(),
                                version_doc_found = (Document) mongoCollection.find(new Document("Commands", "Version")).first(),
                                suggest_doc_found = (Document) mongoCollection.find(new Document("Commands", "Suggest")).first(),
                                users_doc_found = (Document) mongoCollection.find(new Document("Commands", "Users")).first();

                        /* Features Toggle Check */
                        boolean wm_enabled = wm_doc_found.getBoolean("Enabled"),
                                //autocmd_enabled = autocmd_doc_found.getBoolean("Enabled"),
                                joinroles_enabled = joinroles_doc_found.getBoolean("Enabled"),
                                //rewardroles_enabled = rewardroles_doc_found..getBoolean("Enabled"),
                                currency_enabled = currency_doc_found.getBoolean("Enabled");
                                //memcntchan_enabled = memcntchan_doc_found.getBoolean("Enabled");

                        /* Commands Toggle Check */
                        boolean coins_enabled = coins_doc_found.getBoolean("Enabled"),
                                shop_enabled = shop_doc_found.getBoolean("Enabled"),
                                askreddit_enabled = askreddit_doc_found.getBoolean("Enabled"),
                                cancer_enabled = cancer_doc_found.getBoolean("Enabled"),
                                cats_enabled = cats_doc_found.getBoolean("Enabled"),
                                coinflip_enabled = coinflip_doc_found.getBoolean("Enabled"),
                                dogs_enabled = dogs_doc_found.getBoolean("Enabled"),
                                magic8ball_enabled = magic8ball_doc_found.getBoolean("Enabled"),
                                meme_enabled = meme_doc_found.getBoolean("Enabled"),
                                rolldice_enabled = rolldice_doc_found.getBoolean("Enabled"),
                                //suckmy_enabled = suckmy_doc_found.getBoolean("Enabled"),
                                guessthat_enabled = guessthat_doc_found.getBoolean("Enabled"),
                                rocketleague_enabled = rocketleague_doc_found.getBoolean("Enabled"),
                                about_enabled = about_doc_found.getBoolean("Enabled"),
                                cmds_enabled = cmds_doc_found.getBoolean("Enabled"),
                                help_enabled = help_doc_found.getBoolean("Enabled"),
                                dunce_enabled = dunce_doc_found.getBoolean("Enabled"),
                                undunce_enabled = undunce_doc_found.getBoolean("Enabled"),
                                emoteid_enabled = emoteid_doc_found.getBoolean("Enabled"),
                                guild_enabled = guild_doc_found.getBoolean("Enabled"),
                                mongo_enabled = mongo_doc_found.getBoolean("Enabled"),
                                purge_enabled = purge_doc_found.getBoolean("Enabled"),
                                roleid_enabled = roleid_doc_found.getBoolean("Enabled"),
                                version_enabled = version_doc_found.getBoolean("Enabled"),
                                suggest_enabled = suggest_doc_found.getBoolean("Enabled"),
                                users_enabled = users_doc_found.getBoolean("Enabled");

                        String description =
                                "FEATURES" +
                                        "\n—————\n\n" +
                                        wm_enabled + " WelcomeMessage" +
                                        "\n\n" +
                                        //autocmd_enabled + " AutoCommands" +
                                        //"\n\n" +
                                        joinroles_enabled + " JoinRoles" +
                                        "\n\n" +
                                        currency_enabled + " Currency" +
                                        "\n\n" +
                                        //memcntchan_enabled + " Member Count Channel" +
                                        //"\n\n" +

                                        "COMMANDS" +
                                        "\n———————\n\n" +
                                        //coins_enabled + " Coins" +
                                        //"\n\n" +
                                        //shop_enabled + " Shop" +
                                        //"\n\n" +
                                        askreddit_enabled + " AskReddit" +
                                        "\n\n" +
                                        cancer_enabled + " Cancer" +
                                        "\n\n" +
                                        cats_enabled + " Cats" +
                                        "\n\n" +
                                        coinflip_enabled + " CoinFlip" +
                                        "\n\n" +
                                        dogs_enabled + " Dogs" +
                                        "\n\n" +
                                        magic8ball_enabled + " Magic8Ball" +
                                        "\n\n" +
                                        meme_enabled + " Meme" +
                                        "\n\n" +
                                        rolldice_enabled + " RollDice" +
                                        "\n\n" +
                                        //suckmy_enabled + " SuckMy" +
                                        //"\n\n" +
                                        guessthat_enabled + " GuessThat" +
                                        "\n\n" +
                                        rocketleague_enabled + " RocketLeague" +
                                        "\n\n" +
                                        about_enabled + " About" +
                                        "\n\n" +
                                        cmds_enabled + " Cmds" +
                                        "\n\n" +
                                        help_enabled + " Help" +
                                        "\n\n" +
                                        dunce_enabled + " Dunce" +
                                        "\n\n" +
                                        undunce_enabled + " Undunce" +
                                        "\n\n" +
                                        emoteid_enabled + " EmoteID" +
                                        "\n\n" +
                                        guild_enabled + " Guild" +
                                        "\n\n" +
                                        mongo_enabled + " Mongo" +
                                        "\n\n" +
                                        purge_enabled + " Purge" +
                                        "\n\n" +
                                        roleid_enabled + " RoleID" +
                                        "\n\n" +
                                        version_enabled + " Version" +
                                        "\n\n" +
                                        suggest_enabled + " Suggest" +
                                        "\n\n" +
                                        users_enabled + " Users" +
                                        "\n\n";

                        event.reply(new Basic("Toggle Panel",
                                description
                                        .replace("true", ":white_check_mark:")
                                        .replace("false", ":x:")
                                , event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    event.reply(":x: **Oops!** Check out ``u cmds``.");
                }

            } else {
                event.reply(new ErrorEmbed("Error", "Invalid Permissions!", event.getGuild()).getError().build());
            }

        }
    }
}
