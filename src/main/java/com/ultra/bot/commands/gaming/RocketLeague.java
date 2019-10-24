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

package com.ultra.bot.commands.gaming;

import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.SearchAdjuster;
import com.ultra.bot.utilities.converters.StringToEmote;
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.data.ScrapeRedditPosts;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;

public class RocketLeague extends Command {

    public RocketLeague() {
        this.name = "rocketleague";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"rl", "rocket"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "RocketLeague", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        try {

            CmdArgGetter cmdArgGetter = new CmdArgGetter();
            String sub_command = cmdArgGetter.getArgs(event)[1];

            if (sub_command.equals("item")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "rocketleague -> item"))
                    return;

                if (cmdArgGetter.getArgs(event)[2].equalsIgnoreCase("list")) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Rocket League's Items List Portal", "https://rltracker.pro/prices");
                    embedBuilder.setThumbnail("https://rltracker.pro/assets/rl_logo-69351bcb9a7c88a286c1d11070e82f87553b08f08e95eadcae5dbd28e1b9adcc.png");
                    embedBuilder.setFooter("Warning: Have fun!", "http://www.muskegonwindowtinting.com/wp-content/uploads/2017/03/warning-icon.png");

                    event.reply(("**Unfortunately**, __Discord__ is not capable of handling all this bots epicness. Therefor..."));
                    event.reply(embedBuilder.build());
                    return;
                }

                try {

                    /** SEARCH ADJUSTMENT AND FIXING **/
                    // Getting the item searched by removing the commands from the raw message
                    System.out.println(event.getMessage().getContentRaw());
                    String search = event.getMessage().getContentRaw().toLowerCase()
                            .replace("rocketleague", "")
                            .replace("rocket", "")
                            .replace("rl", "")
                            .replace("item", "")
                            .substring(MetaData.bot_prefix.length())
                            .replace(" ", "");
                    System.out.println(search);

                    //System.out.println(search);
                    SearchAdjuster searchAdjuster = new SearchAdjuster();
                    search = searchAdjuster.getSearch(search);
                    // *********** //
                    //System.out.println("Searched: " + search);

                    // Connecting to the website using JSoup THROWING ERROR ON RASPBERRI PI FOR SOME GAY REASON
                    Document document = Jsoup.connect("https://rltracker.pro/prices").get();

                    // Going through all of the item elements
                    for (Element row : document.select("div.col-xl-1.col-lg-2.col-sm-3.col-xs-4.col-price.active")) {

                        try {

                            // Creating a searchable item string
                            String item_name_searchable = row.select("div.price_single a").text().toLowerCase().replace(" ", "");

                            //System.out.println("Item Searchable: " + item_name_searchable);

                            // If the searched item matches one of the items (elements)
                            if (search.equalsIgnoreCase(item_name_searchable)) {

                                String item_id = row.select("div.price_single div.footer").attr("data-id");
                                String IN_graph_indicator = row.select("div.price_single a").text();

                                // Connecting to the website using JSoup
                                Document key_document = Jsoup.connect("https://rltracker.pro/prices/" + item_id).get();

                                /** Getting Key Data **/
                                Element alert_box = key_document.select("div.alert.alert-success center strong").first();
                                String key_amt_range = "n/a";
                                try {
                                    key_amt_range = alert_box.text();
                                } catch (NullPointerException e) {
                                }
                                /************************/

                                //System.out.println("Keys: " + key_amt_range);

                                /** Getting Item Image **/
                                String item_img = "https://i.imgur.com/JcPAUXf.png";
                                // Getting all of the imgs
                                Elements imgs = row.select("div.price_single").select("div.cock_and_img img");
                                // Should only output the image
                                try {
                                    //System.out.println(imgs.get(1).attr("src"));
                                    item_img = "https://rltracker.pro" + imgs.get(1).attr("src");
                                } catch (IndexOutOfBoundsException e) {
                                    //System.out.println(imgs.get(0).attr("src"));
                                    item_img = "https://rltracker.pro" + imgs.get(0).attr("src");
                                }
                                /************************/

                                String img_string_fixed = IN_graph_indicator.replace(" ", "");
                                String graph = "https://rltracker.pro/assets/bot/" + img_string_fixed + ".png?time=1559664338";

                                event.reply(new Basic(IN_graph_indicator, "Value: " + key_amt_range + "\n\n" +
                                        "**Links**\n" +
                                        "[Trading](https://rltracker.pro/trades \"Trade your items!\") " +
                                        "| [Price Graph](https://rltracker.pro/prices/" + item_id + " \"See this items price graph!\")  " +
                                        "| [Full Price List](https://rltracker.pro/prices \"See all item prices!\") \n\n" +
                                        "**Warning**\n" +
                                        "This feature is powered by __RLTracker.pro__, item values are calculated by numerous sources and may be inaccurate to current market values."
                                        , item_img, graph, event.getGuild()).getBasic().build());

                            }

                        } catch (IllegalArgumentException e) {
                            event.reply(new ErrorEmbed("Error", "Issue checking the name?", event.getGuild()).getError().build());
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (sub_command.equals("user")) {

                /** Command Availability **/
                if (!new Checks().SetAvailability(event, true, "rocketleague -> user"))
                    return;

                //System.out.println(event.getMessage().getContentRaw());
                String search = event.getMessage().getContentRaw().toLowerCase()
                        .replace("rocketleague", "")
                        .replace("rocket", "")
                        .replace("rl", "")
                        .replace("user", "")
                        .substring(MetaData.bot_prefix.length())
                        .replace(" ", "");
                System.out.println(search);

                // May the fun begin!
                try {

                    /** ELEMENTS TRYING TO FILL **/
                    StringToEmote stringToEmote = new StringToEmote();
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    /** CONNECTION INFO **/
                    Document document = Jsoup.connect("https://rocketleague.tracker.network/profile/steam/" + search).get();
                    String usable_link = "https://rocketleague.tracker.network/profile/steam/" + search;
                    Element profile_element = document.select("div.trn-profile").first();

                    /** CORE INFO  **/
                    String users_ign = profile_element.select("header.card.profile-header").select("div.top-bit").select("div.info").select("h1.name").text();
                    String platform = profile_element.select("header.card.profile-header").select("div.top-bit").select("div.info h1 i").attr("title");
                    int current_season_number = Integer.parseInt(profile_element
                            .select("div.profile-main")
                            .select("div.aside ul")
                            .select("li.active a").text().replace("Season ", ""));

                    try {
                        /** USER INFO **/
                        /**** SENDING THE INFO ****/
                        embedBuilder.setColor(new Color(8, 118, 195));
                        embedBuilder.setThumbnail("http://vignette1.wikia.nocookie.net/rocketleague/images/f/f6/Rocketleague-logo.png/revision/latest?cb=20161207070401");
                        embedBuilder.setTitle(users_ign, usable_link);
                        embedBuilder.setAuthor("SEASON " + current_season_number);
                        embedBuilder.addField("PLATFORM", platform.toUpperCase(), true);

                    } catch (NullPointerException e) {
                        event.reply(":x: **Whoops!** Steam Community ID not found.");
                        return;
                    }


                    /*********  RANKING INFO  ***********/

                    /**  REWARD INFO [DONE] **/
                    try {
                        String reward_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items tbody tr td").get(1)
                                .select("small").text();
                        String reward_wins = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items tbody tr td").get(2).text();

                        String reward_rank_emote = stringToEmote.stringToEmote(reward_rank, event);
                        embedBuilder.addField("REWARD RANK", reward_rank_emote + " ``" + reward_rank + "``\nWins: " + reward_wins, true);

                    } catch (Exception e) {
                    }


                    // UNRANKED
                    try {
                        String unranked_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                .select("tbody tr").get(0)
                                .select("td").get(3).text();

                        Emote unranked_emote = event.getGuild().getEmotesByName("unranked", true).get(0);
                        embedBuilder.addField("UNRANKED", unranked_emote.getAsMention() + "\nMMR: " + unranked_mmr, true);

                    } catch (Exception e) {
                    }


                    // 1v1 - SOLOS
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_1_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(1)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace(" IV", "")
                                .replace(" III", "")
                                .replace(" II", "")
                                .replace(" I", "")
                                ;


                        String rank_1_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(1)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_1_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(1)
                                //
                                .select("td").get(3).text();
                        String rank_1_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(1)
                                //
                                .select("td").get(5).text();

                        String solos_rank_emote = stringToEmote.stringToEmote(rank_1_rank, event);
                        embedBuilder.addField(rank_1_name.toUpperCase(), solos_rank_emote + " \n``" + rank_1_rank + "``\nMMR: " + rank_1_mmr + "\nGames: " + rank_1_games, true);

                    } catch (Exception e) {
                    }


                    // 2v2 - DUOS
                    try {
                        // NEEDS THE NAME ELEMENTS
                        String rank_2_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(2)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace(" IV", "")
                                .replace(" III", "")
                                .replace(" II", "")
                                .replace(" I", "")
                                ;

                        String rank_2_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(2)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_2_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(2)
                                //
                                .select("td").get(3).text();
                        String rank_2_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(2)
                                //
                                .select("td").get(5).text();

                        String duos_rank_emote = stringToEmote.stringToEmote(rank_2_rank, event);
                        embedBuilder.addField(rank_2_name.toUpperCase(), duos_rank_emote + " \n``" + rank_2_rank + "``\nMMR: " + rank_2_mmr + "\nGames: " + rank_2_games, true);

                    } catch (Exception e) {
                    }


                    // 3v3 - THREES SOLO
                    try {
                        // NEEDS THE NAME ELEMENTS
                        String rank_3_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(3)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace(" IV", "")
                                .replace(" III", "")
                                .replace(" II", "")
                                .replace(" I", "")
                                ;

                        String rank_3_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(3)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_3_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(3)
                                //
                                .select("td").get(3).text();
                        String rank_3_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(3)
                                //
                                .select("td").get(5).text();

                        String threes_rank_emote = stringToEmote.stringToEmote(rank_3_rank, event);
                        embedBuilder.addField(rank_3_name.toUpperCase(), threes_rank_emote + " \n``" + rank_3_rank + "``\nMMR: " + rank_3_mmr + "\nGames: " + rank_3_games, true);

                    } catch (Exception e) {
                    }

                    // 3v3 - STANDARD
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_4_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(4)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace(" IV", "")
                                .replace(" III", "")
                                .replace(" II", "")
                                .replace(" I", "")
                                ;

                        String rank_4_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(4)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_4_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(4)
                                //
                                .select("td").get(3).text();
                        String rank_4_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(4)
                                //
                                .select("td").get(5).text();

                        String threes_reg_rank_emote = stringToEmote.stringToEmote(rank_4_rank, event);
                        embedBuilder.addField(rank_4_name.toUpperCase(), threes_reg_rank_emote + " \n``" + rank_4_rank + "``\nMMR: " + rank_4_mmr + "\nGames: " + rank_4_games, true);

                    } catch (Exception e) {
                    }


                    // HOOPS
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_5_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(5)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace("IV", "")
                                .replace("III", "")
                                .replace("II", "")
                                .replace("I", "")
                                ;

                        String rank_5_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(5)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_5_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(5)
                                //
                                .select("td").get(3).text();
                        String rank_5_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(5)
                                //
                                .select("td").get(5).text();

                        String hoops_rank_emote = stringToEmote.stringToEmote(rank_5_rank, event);
                        embedBuilder.addField(rank_5_name.toUpperCase(), hoops_rank_emote + " \n``" + rank_5_rank + "``\nMMR: " + rank_5_mmr + "\nGames: " + rank_5_games, true);

                    } catch (Exception e) {
                    }

                    // RUMBLE
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_6_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(6)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace("IV", "")
                                .replace("III", "")
                                .replace("II", "")
                                .replace("I", "")
                                ;

                        String rank_6_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(6)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_6_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(6)
                                //
                                .select("td").get(3).text();
                        String rank_6_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(6)
                                //
                                .select("td").get(5).text();

                        String rumble_rank_emote = stringToEmote.stringToEmote(rank_6_rank, event);
                        embedBuilder.addField(rank_6_name.toUpperCase(), rumble_rank_emote + " \n``" + rank_6_rank + "``\nMMR: " + rank_6_mmr + "\nGames: " + rank_6_games, true);

                    } catch (Exception e) {
                    }

                    // DROPSHOT
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_7_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(7)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace("IV", "")
                                .replace("III", "")
                                .replace("II", "")
                                .replace("I", "")
                                ;

                        String rank_7_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(7)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_7_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(7)
                                //
                                .select("td").get(3).text();
                        String rank_7_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(7)
                                //
                                .select("td").get(5).text();

                        String dropshot_rank_emote = stringToEmote.stringToEmote(rank_7_rank, event);
                        embedBuilder.addField(rank_7_name.toUpperCase(), dropshot_rank_emote + " \n``" + rank_7_rank + "``\nMMR: " + rank_7_mmr + "\nGames: " + rank_7_games, true);

                    } catch (Exception e) {
                    }


                    // SNOWDAY
                    try {

                        // NEEDS THE NAME ELEMENTS
                        String rank_8_name = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(8)
                                //
                                .select("td").get(1).text()
                                // Fixing it up
                                .toUpperCase()
                                .replace("BRONZE", "")
                                .replace("SILVER", "")
                                .replace("GOLD", "")
                                .replace("PLATINUM", "")
                                .replace("DIAMOND", "")
                                .replace("CHAMPION", "")
                                .replace("GRAND CHAMPION", "")
                                .replace("DIVISION", "")
                                .replace("UNRANKED", "")
                                .replace("RANKED", "")

                                .replace("IV", "")
                                .replace("III", "")
                                .replace("II", "")
                                .replace("I", "")
                                ;

                        String rank_8_rank = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(8)
                                //
                                .select("td").get(1)
                                .select("small").text();
                        String rank_8_mmr = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // Below Row is responsible for each Game-mode
                                .select("tbody tr").get(8)
                                //
                                .select("td").get(3).text();
                        String rank_8_games = profile_element
                                .select("div.profile-main")
                                .select("div.aside")
                                .select("div.card-table-container")
                                .select("div.season-table")
                                .select("table.card-table.items").get(1)
                                // BELOW Row is responsible for each Game-mode
                                .select("tbody tr").get(8)
                                //
                                .select("td").get(5).text();

                       String snowday_rank_emote = stringToEmote.stringToEmote(rank_8_rank, event);
                       embedBuilder.addField(rank_8_name.toUpperCase(), snowday_rank_emote + " \n``" + rank_8_rank + "``\nMMR: " + rank_8_mmr + "\nGames: " + rank_8_games, true);

                    } catch (Exception e) {  }


                    // PLAYSTYLE
                    try {

                        String goals_shots = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(1).text();

                        String wins = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(2).text();

                        String goals = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(3).text();

                        String saves = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(4).text();

                        String shots = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(5).text();

                        String mvps = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(6).text();

                        String assists = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(7).text();

                        String mvp_and_win = profile_element
                                .select("div.profile-main")
                                .select("div.content")
                                .select("div.card")
                                .select("div.stats-large")
                                .select("div.stat").get(8).text();

                        embedBuilder.addField("PERFORMANCE",
                                "``" + goals_shots +
                                        "\n" +
                                        wins +
                                        "\n" +
                                        goals +
                                        "\n" +
                                        saves +
                                        "\n" +
                                        shots +
                                        "\n" +
                                        mvps +
                                        "\n" +
                                        assists +
                                        "\n" +
                                        mvp_and_win + "``", true);

                    } catch (Exception e) {
                    }

                    event.reply(embedBuilder.build());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (sub_command.equals("reddit")) {

                ScrapeRedditPosts.RedditScraper(event, "https://www.reddit.com/r/RocketLeague/");

            }

        } catch (IllegalArgumentException e) {
            event.reply("Not enough arguments were entered!");
            e.printStackTrace();
        }
    }
}
