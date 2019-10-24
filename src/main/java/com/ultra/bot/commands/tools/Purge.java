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

import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

public class Purge extends Command {

    private PurgeMsgs purgeMsgs = new PurgeMsgs();

    public Purge() {
        this.name = "purge";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.cooldown = 3;
    }

    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Purge", false))
            return;

        /** Setup Checker **/
        if(!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // Class
        CmdArgGetter cf = new CmdArgGetter();

        if (!event.getMember().getUser().isBot()) {

            Role OPRole = event.getGuild().getRolesByName("*", true).get(0);
            Role adminRole = event.getGuild().getRolesByName("adm", true).get(0);
            Role modRole = event.getGuild().getRolesByName("mod", true).get(0);

            if (event.getMember().getRoles().contains(adminRole) || event.getMember().getRoles().contains(OPRole) || event.getMember().getRoles().contains(modRole)) {

                try{

                    // Getting the number of chats to delete from a string
                    int chatNum = Integer.parseInt(cf.getArgs(event)[1]);

                    // DELETING THE COMMAND (Keeps the interactive space clean)
                    purgeMsgs.purgeMessages(event, chatNum + 1);

                }catch(NumberFormatException e){

                    // Getting the number of chats to delete from a string
                    int chatNum = Integer.parseInt(cf.getArgs(event)[2]);

                    // DELETING THE COMMAND (Keeps the interactive space clean)
                    purgeMsgs.purgeMessages(event, chatNum + 1);

                }

            } else {
                event.reply(new ErrorEmbed("ERROR", "Invalid Permissions!", event.getGuild()).getError().build());
            }

        }
    }
}
