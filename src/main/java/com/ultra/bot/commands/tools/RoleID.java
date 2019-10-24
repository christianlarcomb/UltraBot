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
import com.ultra.bot.utilities.converters.TagToRole;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

public class RoleID extends Command {

    private CmdArgGetter cmdArgGetter = new CmdArgGetter();
    private TagToRole tagToRole = new TagToRole();

    public RoleID(){
        this.name = "roleid";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.cooldown = 3;
    }

    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "RoleID", false))
            return;

        /** Setup Checker **/
        if(!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        Role adminRole = event.getGuild().getRolesByName("adm", true).get(0);
        Role OPRole = event.getGuild().getRolesByName("*", true).get(0);

        if (event.getMember().getRoles().contains(adminRole) || event.getMember().getRoles().contains(OPRole)) {

            String roletoView = cmdArgGetter.getArgs(event)[1];
            Role role = tagToRole.tagToRole(roletoView);

            event.reply(role.getId());

            System.out.println("ITS WORKING");

        } else {
            event.reply(new ErrorEmbed("Error", "Invalid Permissions.", event.getGuild()).getError().build());
        }


    }
}
