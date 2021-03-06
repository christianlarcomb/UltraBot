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

package com.ultra.bot.commands.tools;

import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.utilities.embeds.Basic;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class Guild extends Command {

    public Guild() {
        this.name = "guild";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"g", "server"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Guild", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        CmdArgGetter cmdArgGetter = new CmdArgGetter();
        String sub_command = cmdArgGetter.getArgs(event)[1];
        if(sub_command.equalsIgnoreCase("info")){

            event.reply(new Basic("Guild Info",
                    "People Count\n" + event.getGuild().getMembers().size() +
                            "\n\n" +

                            "Roles Count\n" + event.getGuild().getRoles().size() +
                            "\n\n" +

                            "Text-Channels Count\n" + event.getGuild().getTextChannels().size() +
                            "\n\n" +

                            "Voice-Channels Count\n" + event.getGuild().getVoiceChannels().size() +
                            "\n\n" +

                            "Guild Creation Date\n" + event.getGuild().getCreationTime().toString() +
                            "\n\n" +

                            "Default Channel\n" + event.getGuild().getDefaultChannel() +
                            "\n\n" +

                            "Guild Owner\n" + event.getGuild().getOwner().getAsMention() +
                            "\n\n" +

                            "Guild Region\n" + event.getGuild().getRegionRaw()

                    , event.getSelfUser().getAvatarUrl(), event.getGuild())
                    .getBasic().build());

        }

    }
}
