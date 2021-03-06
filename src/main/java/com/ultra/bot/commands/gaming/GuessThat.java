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
import com.ultra.bot.utilities.data.EmoteArrays;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*

!!! GuessThatGame !!!

 */

public class GuessThat extends Command {

    private final EventWaiter waiter;
    private PurgeMsgs purgeMsgs = new PurgeMsgs();

    public GuessThat(EventWaiter waiter) {
        this.name = "guessthat";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.waiter = waiter;
        this.cooldown = 3;
        this.aliases = new String[]{"gt"};
    }

    private String title;
    private String description;

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "GuessThat", false))
            return;

        /** Command Availability **/
        if(!new Checks().SetAvailability(event, false, "guessthat"))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // CHECKING IF THE USER IS A BOT
        if (!event.getMember().getUser().isBot()) {

            // REPLACE WITH INBOX ICON
            String iconURL = event.getGuild().getIconUrl();

            MessageChannel channel = event.getChannel();

            // DELETING THE COMMAND (Keeps the interactive space clean)
            purgeMsgs.purgeMessages(event, 1);

            // Creating a document DiscordMember in the collection Users [MAKE SURE THE DOC NAME IS "DiscordID" as the document found relies on it!]

            // BEGINNING THE REGISTRATION PROCESS ************************************************
            channel.sendMessage(new Basic("'GuessThat' Game Setup!", "Let's get **started**!\n\n" +
                    "Which Guess Game are we playing?", iconURL, event.getGuild()).getBasic().build()).queue();

            EmoteArrays em = new EmoteArrays();

            // ADDING THE EMOTES FOR SELECTION
            List<Message> messageOne = event.getChannel().getHistory().retrievePast(1).complete();
            for (int i = 0; i < em.getOptions().size(); i++)
                messageOne.get(0).addReaction(em.getVote().get(i)).queue();

            /******** WAITER #1 [Getting Game Type!] ********/
            waiter.waitForEvent(GuildMessageReactionAddEvent.class, e -> e.getMember().equals(event.getMember()), e -> {

                        // PURGE: Old message
                        List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
                        channel.purgeMessages(messages);
                        /****/



                        // PROMPTING THE NEXT QUESTION FOR THE NEXT WAITER (#2)
                        channel.sendMessage(new Basic("Suggestion Box    |    ● ● ○ ", "Enter a suggestion description:", iconURL, event.getGuild()).getBasic().build()).queue();

                        /******** WAITER #2 [Getting Description for ] ********/
                        waiter.waitForEvent(GuildMessageReceivedEvent.class, e2 -> e2.getAuthor().equals(event.getAuthor()), e2 -> {

                                    // PURGE: Old message
                                    List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
                                    channel.purgeMessages(messages2);
                                    /****/

                                    // GETTING THE TITLE THEN SETTING THE CLASS VARIABLE TO IT
                                    this.description = e2.getMessage().getContentRaw();

                                    // PROMPTING THE NEXT QUESTION FOR THE NEXT WAITER (#2)
                                    channel.sendMessage(new Basic("Suggestion Box    |    ● ● ● ", "Can users vote on this box?", iconURL, event.getGuild()).getBasic().build()).queue();

                                    //EmoteArrays em = new EmoteArrays();

                                    // ADDING THE EMOTES FOR SELECTION
                                    //List<Message> messageOne = event.getChannel().getHistory().retrievePast(1).complete();
                                    //for (int i = 0; i < em.getOptions().size(); i++)
                                    //    messageOne.get(0).addReaction(em.getOptions().get(i)).queue();

                                    /******** WAITER #3 [Getting Student ID] ********/
                                    waiter.waitForEvent(GuildMessageReactionAddEvent.class, e3 -> e3.getMember().equals(event.getMember()), e3 -> {

                                                // PURGE: Old message
                                                List<Message> messages3 = event.getChannel().getHistory().retrievePast(1).complete();
                                                channel.purgeMessages(messages3);
                                                /****/

                                                event.reply(new Basic(this.title, this.description, event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

                                                // Forced to do this weirdly and via variables because JDA is acting weird?
                                                String reactionEmote1 = e3.getReactionEmote().getName();
                                                String reactionEmote2 = em.getOptions().get(0);

                                                // If they user pressed Y
                                                if (reactionEmote1.equals(reactionEmote2)) {

                                                    // ADDING THE EMOTES FOR SELECTION
                                                    List<Message> messageTwo = event.getChannel().getHistory().retrievePast(1).complete();
                                                    for (int i = 0; i < em.getVote().size(); i++)
                                                        messageTwo.get(0).addReaction(em.getVote().get(i)).queue();

                                                }
                                                // If no, do nothing.

                                            }, 30, TimeUnit.SECONDS, () -> channel.sendMessage(
                                            new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build()).queue()
                                    );

                                }, 30, TimeUnit.SECONDS, () -> channel.sendMessage(
                                new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build()).queue()
                        );

                    }, 30, TimeUnit.SECONDS, () -> channel.sendMessage(
                    new ErrorEmbed("TIMEOUT ERROR", "Users took longer than 30 seconds", event.getGuild()).getError().build()).queue()
            );

        } else {
            event.reply(new ErrorEmbed("Error", "Invalid Permissions!", event.getGuild()).getError().build());
        }

    }

}
