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
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.Basic;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class Version extends Command {

    public Version() {
        this.name = "version";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"v","ver"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        event.reply(new Basic(MetaData.bot_name + " " + (int) MetaData.version,
                        "v." + MetaData.version + "  " + MetaData.v_status.toUpperCase()
                                + "\n\n" +

                                "``Patch Notes``" +
                                "\n\n" +

                                "``v1.71719``\n" +
                                "- **Edited** Cmds, Help, About, Version commands to be ENABLED by default (as requested by DiscordBots.com)\n" +
                                "\n\n" +

                                "``v1.71519``\n" +
                                "- **Fixed** All Command Bot Permissions. (Required Admin for no reason before)\n" +
                                "- **Fixed** ``Currency`` toggle and check" +
                                "- **Removed** Unnecessary toggles" +
                                "\n\n" +

                                "``v1.71419``\n" +
                                "- **Fixed** Misnamed command responses. (Thx Luke)\n" +
                                "- **Updated** ``u version``" +
                                "\n\n" +

                                "``v1.71219``\n" +
                                "- **Added** Inquiry Commands (``u? welcomemessages``, ``u? joinroles``, ``u? currency``, etc.)\n" +
                                "- **Updated** the ``u cmds`` command to look 100x cooler.\n" +
                                "- **Lift Off..** Preparing for **Alpha release!**" +
                                "\n\n" +

                                "``v1.70819``\n" +
                                "- **Added** Currency configurablity.\n" +
                                "- **Added** Welcome Message configurablity.\n" +
                                "- **Added** \'u! list\' and \'u! toggle\' commands.\n" +
                                "- **Updated** Currency (u$), Config (u!), Inquire (u?) commands prefix.\n" +
                                "- **Fixed** Plethora of minor bugs!"

                , event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

    }
}
