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

package com.ultra.bot.frameworks.notifications;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class BotJoinMsg extends ListenerAdapter {

    // MEMBER JOIN EVENT
    public void onGuildJoin(GuildJoinEvent event) {

        //System.out.println("GuildJoinMessage is firing!");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Hello! I'm your personal **Ultra Bot**!\n\n" +
                "To get started, send ``u help``. Quick note, all standard commands use the prefix ``u`` and configuration commands use the prefix ``u!``.\n\n" +
                "Interested in autoposting pictures, memes, currency notifications, and more? I'll have that functionality one you pay the additional features fee. (not possible yet)\n\n" +
                "**#1 Have fun and...**");
        embedBuilder.setFooter("Enjoy all of the FREE features :)", "https://i.imgur.com/ZCnsO2x.png");
        embedBuilder.setColor(Color.darkGray);

        event.getGuild().getDefaultChannel().sendMessage(embedBuilder.build()).queue();

    }

}
