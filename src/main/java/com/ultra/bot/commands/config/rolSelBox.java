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

import com.ultra.bot.utilities.data.EmoteArrays;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.data.BulkMessages;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import java.util.List;

public class rolSelBox extends Command {

    public rolSelBox() {
        this.name = "rolselbox";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Availability **/
        if(new Checks().SetAvailability(event, true, "rolselbox"))
            return;

        /** Setup Checker **/
        if(!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        EmoteArrays em = new EmoteArrays();
        BulkMessages bulkMessages = new BulkMessages();

        // CHECKING IF THE USER IS A BOT
        if (!event.getMember().getUser().isBot()) {

            Role OPRole = event.getGuild().getRolesByName("*", true).get(0);

            if (event.getMember().getRoles().contains(OPRole)) {

                event.reply(new Basic("Server Preference Roles", bulkMessages.ServerPrefMsg, event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

                // ADDING THE EMOTES FOR SELECTION
                List<Message> messageOne = event.getChannel().getHistory().retrievePast(1).complete();
                for (int i = 0; i < em.getServerPreferences().size(); i++)
                    messageOne.get(0).addReaction(em.getServerPreferences().get(i)).queue();


                event.reply(new Basic("Rocket Rank Roles", bulkMessages.rocketRankMsg, event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

                // ADDING THE EMOTES FOR SELECTION
                List<Message> messageTwo = event.getChannel().getHistory().retrievePast(1).complete();
                for (int i = 0; i < em.getRocketRankSelection().size(); i++)
                    messageTwo.get(0).addReaction(em.getRocketRankSelection().get(i)).queue();


                event.reply(new Basic("Rocket Preference Roles", bulkMessages.rocketPrefMsg, event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

                // ADDING THE EMOTES FOR SELECTION
                List<Message> messageThree = event.getChannel().getHistory().retrievePast(1).complete();
                for (int i = 0; i < em.getRocketPrefSelection().size(); i++)
                    messageThree.get(0).addReaction(em.getRocketPrefSelection().get(i)).queue();


            } else {
                event.reply(new ErrorEmbed("Error", "Invalid Permissions!", event.getGuild()).getError().build());
            }

        }
    }
}
