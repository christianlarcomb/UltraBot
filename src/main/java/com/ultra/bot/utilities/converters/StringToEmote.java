package com.ultra.bot.utilities.converters;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Emote;

public class StringToEmote {

    public String stringToEmote(String string, CommandEvent event) {

        // Season Rewards (Whole Ranks - Non-Div)
        Emote gc_emote,
        c1_emote, c2_emote, c3_emote,
        d1_emote, d2_emote, d3_emote,
        p1_emote, p2_emote, p3_emote,
        g1_emote, g2_emote, g3_emote,
        s1_emote, s2_emote, s3_emote,
        b1_emote, b2_emote, b3_emote,
        unranked_emote;

        try {
            //System.out.println("GATE 1");
            //
            gc_emote = event.getGuild().getEmotesByName("gc", true).get(0);
            //
            c3_emote = event.getGuild().getEmotesByName("c3", true).get(0);
            c2_emote = event.getGuild().getEmotesByName("c2", true).get(0);
            c1_emote = event.getGuild().getEmotesByName("c1", true).get(0);
            //
            d3_emote = event.getGuild().getEmotesByName("d3", true).get(0);
            d2_emote = event.getGuild().getEmotesByName("d2", true).get(0);
            d1_emote = event.getGuild().getEmotesByName("d1", true).get(0);
            //
            p3_emote = event.getGuild().getEmotesByName("p3", true).get(0);
            p2_emote = event.getGuild().getEmotesByName("p2", true).get(0);
            p1_emote = event.getGuild().getEmotesByName("p1", true).get(0);

            g3_emote = event.getGuild().getEmotesByName("g3", true).get(0);
            g2_emote = event.getGuild().getEmotesByName("g2", true).get(0);
            g1_emote = event.getGuild().getEmotesByName("g1", true).get(0);

            s3_emote = event.getGuild().getEmotesByName("s3", true).get(0);
            s2_emote = event.getGuild().getEmotesByName("s2", true).get(0);
            s1_emote = event.getGuild().getEmotesByName("s1", true).get(0);

            b3_emote = event.getGuild().getEmotesByName("b3", true).get(0);
            b2_emote = event.getGuild().getEmotesByName("b2", true).get(0);
            b1_emote = event.getGuild().getEmotesByName("b1", true).get(0);

            unranked_emote = event.getGuild().getEmotesByName("unranked", true).get(0);

            //System.out.println("GATE 2");

            String thing_to_return = string;

            if (string.contains("Grand Champion")) {
                //thing_to_return = "<:grand_champion:585677587364446254>";
                thing_to_return = gc_emote.getAsMention();
                //System.out.println(gc_emote.getAsMention());

            } else if (string.contains("Champion III")) {
                //thing_to_return = "<:champion_one:585677637339316225>";
                thing_to_return = c3_emote.getAsMention();
                //System.out.println(c3_emote.getAsMention());

            } else if (string.contains("Champion II")) {
                //thing_to_return = "<:champion_one:585677637339316225>";
                thing_to_return = c2_emote.getAsMention();
                //System.out.println(c2_emote.getAsMention());

            } else if (string.contains("Champion I") || string.contains("Champion")) {
                //thing_to_return = "<:champion_one:585677637339316225>";
                thing_to_return = c1_emote.getAsMention();
                //System.out.println(c1_emote.getAsMention());


            } else if (string.contains("Diamond III")) {
                //thing_to_return = "<:diamond_one:585674792326397953>";
                thing_to_return = d3_emote.getAsMention();
                //System.out.println(d3_emote.getAsMention());

            } else if (string.contains("Diamond II")) {
                //thing_to_return = "<:diamond_one:585674792326397953>";
                thing_to_return = d2_emote.getAsMention();
                //System.out.println(d2_emote.getAsMention());

            } else if (string.contains("Diamond I") || string.contains("Diamond")) {
                //thing_to_return = "<:diamond_one:585674792326397953>";
                thing_to_return = d1_emote.getAsMention();
                //System.out.println(d1_emote.getAsMention());


            } else if (string.contains("Platinum III")) {
                //thing_to_return = "<:platinum_one:585677745586176000>";
                thing_to_return = p3_emote.getAsMention();
                //System.out.println(p3_emote.getAsMention());

            } else if (string.contains("Platinum II")) {
                //thing_to_return = "<:platinum_one:585677745586176000>";
                thing_to_return = p2_emote.getAsMention();
                //System.out.println(p2_emote.getAsMention());

            } else if (string.contains("Platinum I") || string.contains("Platinum")) {
                //thing_to_return = "<:platinum_one:585677745586176000>";
                thing_to_return = p1_emote.getAsMention();
                //System.out.println(p1_emote.getAsMention());


            } else if (string.contains("Gold III")) {
                //thing_to_return = "<:gold_one:585677718323068928>";
                thing_to_return = g3_emote.getAsMention();
                //System.out.println(g3_emote.getAsMention());

            } else if (string.contains("Gold II")) {
                //thing_to_return = "<:gold_one:585677718323068928>";
                thing_to_return = g2_emote.getAsMention();
                //System.out.println(g2_emote.getAsMention());

            } else if (string.contains("Gold I") || string.contains("Gold")) {
                //thing_to_return = "<:gold_one:585677718323068928>";
                thing_to_return = g1_emote.getAsMention();
                //System.out.println(g1_emote.getAsMention());


            } else if (string.contains("Silver III")) {
                //thing_to_return = "<:silver_one:585677758919606272>";
                thing_to_return = s3_emote.getAsMention();
                //System.out.println(s3_emote.getAsMention());

            } else if (string.contains("Silver II")) {
                //thing_to_return = "<:silver_one:585677758919606272>";
                thing_to_return = s2_emote.getAsMention();
                //System.out.println(s2_emote.getAsMention());

            } else if (string.contains("Silver I") || string.contains("Silver")) {
                //thing_to_return = "<:silver_one:585677758919606272>";
                thing_to_return = s1_emote.getAsMention();
                //System.out.println(s1_emote.getAsMention());


            } else if (string.contains("Bronze III")) {
                //thing_to_return = "<:bronze_one:585677772727386113>";
                thing_to_return = b3_emote.getAsMention();
                //System.out.println(b3_emote.getAsMention());

            } else if (string.contains("Bronze II")) {
                //thing_to_return = "<:bronze_one:585677772727386113>";
                thing_to_return = b2_emote.getAsMention();
                //System.out.println(b2_emote.getAsMention());

            } else if (string.contains("Bronze I") || string.contains("Bronze")) {
                //thing_to_return = "<:bronze_one:585677772727386113>";
                thing_to_return = b1_emote.getAsMention();
                //System.out.println(b1_emote.getAsMention());

            } else if (string.contains("Unranked")) {
                //thing_to_return = "<:bronze_one:585677772727386113>";
                thing_to_return = unranked_emote.getAsMention();
                //System.out.println(b1_emote.getAsMention());
            }

            return thing_to_return;

        } catch (IndexOutOfBoundsException e) {
            event.getGuild().getDefaultChannel().sendMessage(":x: **Whoops!** You're missing needed emotes! ``b1, s1, g1, p1, c1, gc");
            e.printStackTrace();
        }

        return "null";

    }
}
