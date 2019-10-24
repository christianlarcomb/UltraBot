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
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import org.bson.Document;

import java.awt.*;

public class Inquiring extends Command {


    public Inquiring() {
        this.name = "?";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
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


        // CHECKING IF THE USER IS A BOT
        if (!event.getMember().getUser().isBot()) {

            CmdArgGetter cmdArgGetter = new CmdArgGetter();


            Role OPRole = event.getGuild().getRolesByName("*", true).get(0);

            if (event.getMember().getRoles().contains(OPRole)) {

                try {


                    // Getting the Guilds Collection
                    MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

                    // Getting the users first entered Arg.
                    String first_arg = cmdArgGetter.getArgs(event)[1];

                    if (first_arg.equalsIgnoreCase("welcomemessage") || first_arg.equalsIgnoreCase("wm")) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setAuthor("Welcome Message Inquiry");
                        embedBuilder.setColor(Color.DARK_GRAY);

                        String set_desc = ":grey_exclamation: **Just a reminder!**" +
                                "\n" +
                                "You can set the Welcome Message(s) by using:" +
                                "\n" +
                                "``u! welcomemessage set <#>``." +
                                "\n\n";

                        // Getting the WelcomeMessages DB docs
                        Document wm_doc_found = (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first();

                        // Looping through all configurations
                        for (int i = 1; i <= MetaData.Welcome_Messages_Count; i++) {

                            String thumbnail = wm_doc_found.getString("Message_" + i + "_Thumbnail");
                            String picture = wm_doc_found.getString("Message_" + i + "_Image");
                            String text = wm_doc_found.getString("Message_" + i + "_Text");

                            if (thumbnail.isEmpty() && picture.isEmpty() && text.isEmpty()) {
                                set_desc +=
                                        "**:speech_balloon: MESSAGE #" + i + "**" +
                                                "\n\n" +

                                                "``< Not Setup >``" +
                                                "\n\n";
                            } else {
                                set_desc +=
                                        "**:speech_balloon: MESSAGE #" + i + "**" +
                                                "\n\n";

                                if (!thumbnail.isEmpty()) {
                                    set_desc +=
                                            "**THUMBNAIL**" +
                                                    "\n" +
                                                    "**⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤**" +
                                                    "\n" +
                                                    thumbnail +
                                                    "\n\n";
                                }

                                if (!picture.isEmpty()) {
                                    set_desc +=
                                            "**IMAGE**" +
                                                    "\n" +
                                                    "**⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤**" +
                                                    "\n" +
                                                    picture +
                                                    "\n\n";
                                }

                                if (!text.isEmpty()) {
                                    set_desc +=
                                            "**TEXT**" +
                                                    "\n" +
                                                    "**⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤**" +
                                                    "\n" +
                                                    text
                                                    + "\n\n";
                                }

                            }

                        }

                        embedBuilder.setDescription(set_desc);

                        event.reply(embedBuilder.build());

                    } else if (first_arg.equalsIgnoreCase("currency") || first_arg.equalsIgnoreCase("$")) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(Color.DARK_GRAY);
                        embedBuilder.setAuthor("Currency Inquiry");

                        // Getting the WelcomeMessages DB docs
                        Document curr_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();
                        String currency_name, embed_desc, tradable;
                        int inv_amt, voice_amt, text_amt;

                        currency_name = curr_doc_found.getString("Currency_Name");
                        inv_amt = curr_doc_found.getInteger("Amt_Earned_Inviting");
                        voice_amt = curr_doc_found.getInteger("Amt_Earned_VoiceChannel");
                        text_amt = curr_doc_found.getInteger("Amt_Earned_TextChannel");
                        tradable = curr_doc_found.getBoolean("Tradable").toString().replace("true", ":white_check_mark:")
                                .replace("false", ":x:");

                        embed_desc =
                                ":moneybag: **Currency Name**" +
                                        "\n" +
                                        currency_name +
                                        "\n\n" +

                                        ":mailbox_with_mail: **Earned From Inviting**" +
                                        "\n" +
                                        inv_amt +
                                        "\n\n" +

                                        ":mega: **Earned From Voice Chatting (per hr)**" +
                                        "\n" +
                                        voice_amt +
                                        "\n\n" +

                                        ":speech_balloon: **Earned From Messaging**" +
                                        "\n" +
                                        text_amt +
                                        "\n\n" +

                                        ":handshake: **Tradable**" +
                                        "\n" +
                                        tradable +
                                        "\n\n";

                        embedBuilder.setDescription(embed_desc);

                        event.reply(embedBuilder.build());


                    } else if (first_arg.equalsIgnoreCase("joinroles")) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(Color.DARK_GRAY);
                        embedBuilder.setAuthor("Join Roles Inquiry");

                        // Getting the WelcomeMessages DB docs
                        Document jroles_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first();

                        String embed_desc = "";

                        for (int i = 1; i < MetaData.Join_Roles_Count; i++) {

                            String role_found = jroles_doc_found.getString("Role_" + i)
                                    .replace("<", "")
                                    .replace("@&", "")
                                    .replace(">", "");

                            System.out.println();

                            embed_desc +=
                                    "**JOIN ROLE #" + i + "**" +
                                            "\n" +
                                            "**⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤**" +
                                            "\n";

                            try {

                                Role role = event.getGuild().getRoleById(role_found);

                                embed_desc +=
                                        "**As Mention** \n" + role.getAsMention() +
                                                "\n\n" +
                                                "**Name** \n" + role.getName() +
                                                "\n\n" +
                                                "**Role Code** \n``" + jroles_doc_found.getString("Role_" + i) + "``"
                                + "\n\n";


                            } catch (IllegalArgumentException e) {

                                embed_desc +=
                                        "Not Setup" +
                                                "\n\n";
                            }


                        }

                        embedBuilder.setDescription(embed_desc);
                        event.reply(embedBuilder.build());


                    } else if (first_arg.equalsIgnoreCase("autocmds")) {
                    } else if (first_arg.equalsIgnoreCase("rewardroles")) {
                    } else if (first_arg.equalsIgnoreCase("toggle")) {


                    } else if (first_arg.equalsIgnoreCase("list")) {

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

                        /* Features Toggle Check */
                        boolean wm_enabled = wm_doc_found.getBoolean("Enabled"),
                                autocmd_enabled = autocmd_doc_found.getBoolean("Enabled"),
                                joinroles_enabled = joinroles_doc_found.getBoolean("Enabled"),
                                //rewardroles_enabled = rewardroles_doc_found..getBoolean("Enabled"),
                                currency_enabled = currency_doc_found.getBoolean("Enabled"),
                                memcntchan_enabled = memcntchan_doc_found.getBoolean("Enabled");

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
                                suckmy_enabled = suckmy_doc_found.getBoolean("Enabled"),
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
                                        coins_enabled + " Coins" +
                                        "\n\n" +
                                        shop_enabled + " Shop" +
                                        "\n\n" +
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
                    event.reply(":x: **Oops!** Check out ``u?``.");
                }

            } else {
                event.reply(new ErrorEmbed("Error", "Invalid Permissions!", event.getGuild()).getError().build());
            }

        }
    }
}
