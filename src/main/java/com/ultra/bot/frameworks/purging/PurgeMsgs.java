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

package com.ultra.bot.frameworks.purging;

import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PurgeMsgs {

    public void purgeMessages(CommandEvent event, int purgeAmt) {

        // Making a clean variable
        MessageChannel channel = event.getChannel();

        if(purgeAmt > 100){

            channel.sendMessage(new ErrorEmbed("Error", "You may not purge 100 messages or more!", event.getGuild()).getError().build()).queue();

        } else if (purgeAmt < 0){

            channel.sendMessage(new ErrorEmbed("Error", "You may not purge less than 0 messages!", event.getGuild()).getError().build()).queue();

        } else {

            // GETTING MESSAGES
            List<Message> messages = event.getChannel().getHistory().retrievePast(purgeAmt).complete();

            // PURGING THE GOTTEN MESSAGES
            channel.purgeMessages(messages);
        }

    }

    public void purgeMessages(GuildMessageReceivedEvent event, int purgeAmt) {

        // Making a clean variable
        MessageChannel channel = event.getChannel();

        if(purgeAmt > 100){

            channel.sendMessage(new ErrorEmbed("Error", "You may not purge 100 messages or more!", event.getGuild()).getError().build()).queue();

        } else if (purgeAmt < 0){

            channel.sendMessage(new ErrorEmbed("Error", "You may not purge less than 0 messages!", event.getGuild()).getError().build()).queue();

        } else {

            // GETTING MESSAGES
            List<Message> messages = event.getChannel().getHistory().retrievePast(purgeAmt).complete();

            // PURGING THE GOTTEN MESSAGES
            channel.purgeMessages(messages);
        }

    }

}
