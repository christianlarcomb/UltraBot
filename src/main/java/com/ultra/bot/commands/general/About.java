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

package com.ultra.bot.commands.general;

import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.Basic;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class About extends Command {

    public About() {
        this.name = "about";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"info"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "About", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        event.reply(new Basic("About ~~"+ MetaData.bot_name+"~~.",
                "\n" +
                        "**LINKS**\n\n" +
                        ":earth_americas: [Website](https://www.google.com \""+MetaData.bot_name+" bots Homepage\")\n" +
                        ":inbox_tray: [Invite](https://discordapp.com/api/oauth2/authorize?client_id=578277176290443264&permissions=285600832&scope=bot \"Invite **"+MetaData.bot_name+"** bot\")" +
                        "\n\n" +

                        "**PURPOSE**\n" +
                        "**"+MetaData.bot_name+"** was made by **Chace** because he was bored of all other bots available /shrug. It occurred to him that he'd rather make a legendary all in one bot that could cover any and all circumstances." +
                        "\n\n" +

                        "**SUMMARY**\n" +
                        MetaData.bot_name+" quite literally does it all, and if it doesn't, it will. To think it's just for kicks..." +
                        "\n\n" +

                        "**CREATOR INFO**\n" +
                        "- - - **Chace** - - -\n\n" +
                        "LinkedIn\n" +
                        "Facebook\n" +
                        "\n\n"

                , event.getSelfUser().getAvatarUrl(), event.getGuild()).getBasic().build());

    }
}
