package com.ultra.bot.utilities.data;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class ScrapeRedditPosts {

    public static void RedditScraper(CommandEvent event, String redditLink) {

        // Necessary variables
        String likes = "", author = "", time_posted = "", post_description = "", post_text = "", post_img = "", comments = "", link = "";

        try {

            /**** SENDING THE INFO ****/
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(new Color(8, 118, 195));

            /** CONNECTION INFO **/
            Document document = Jsoup.connect(redditLink).get();
            Element profile_element = document.select("div.rpBJOHq2PR60pnwJlUyP0").first();

            int random_post = ThreadLocalRandom.current().nextInt(1, 8);

            Element post_details = profile_element.child(random_post).select("div div div").select("div").first();

            try {
                author = post_details.child(1).child(0).child(0).child(0).child(1).text();
                embedBuilder.setAuthor(author);
            } catch (Exception e) { }

            try {
                post_description = post_details.child(1).child(1).select("span").text();
                link = "https://www.reddit.com" + post_details.child(1).child(3).child(1).child(0).attr("href");
                embedBuilder.setTitle(post_description, link);
            } catch (Exception e) { }
            try {
                post_img = post_details.child(1).child(2).child(0).child(1).child(0).child(0).child(0).select("img").attr("src");
                embedBuilder.setImage(post_img);
            } catch (Exception e) { }
            try {
                try {
                    time_posted = post_details.child(1).child(0).child(0).child(0).child(3).text();
                } catch (IndexOutOfBoundsException e) {
                    time_posted = post_details.child(1).child(0).child(0).child(0).child(2).text();
                }
                likes = profile_element.child(1).select("div div div").select("div").get(2).text();
                comments = post_details.child(1).child(3).child(1).child(0).child(1).text();
                embedBuilder.setFooter("\uD83D\uDC4D " + likes + " ▪ \uD83D\uDCAC " + comments + " ▪ " + time_posted, event.getSelfUser().getAvatarUrl());
            } catch (Exception e) { }

            // If something is null, do this again!
            if(likes.equals("") || author.equals("") || time_posted.equals("") || post_img.equals("") || comments.equals("") || link.equals("")){
                RedditScraper(event, redditLink);
            } else {
                event.reply(embedBuilder.build());
            }

        } catch (Exception e) {
            event.reply(":x: Something went wrong!");
            e.printStackTrace();
        }
    }

}
