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

package com.ultra.bot.commands.fun;

import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.CmdArgGetter;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class RollDice extends Command {

    public RollDice() {
        this.name = "rolldice";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "RollDice", false))
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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.darkGray);

        if(sub_command.equalsIgnoreCase("6")) {

            int random_num = ThreadLocalRandom.current().nextInt(0, 6);
            switch (random_num) {
                case 0:
                    embedBuilder.setThumbnail("https://i.imgur.com/Kg0bWXZ.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **ONE**");
                    break;
                case 1:
                    embedBuilder.setThumbnail("https://i.imgur.com/lqlhqJ3.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **TWO**");
                    break;
                case 2:
                    embedBuilder.setThumbnail("https://i.imgur.com/fgSstDv.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **THREE**");
                    break;
                case 3:
                    embedBuilder.setThumbnail("https://i.imgur.com/sROv2Rd.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **FOUR**");
                    break;
                case 4:
                    embedBuilder.setThumbnail("https://i.imgur.com/ZOZGEbD.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **FIVE**");
                    break;
                case 5:
                    embedBuilder.setThumbnail("https://i.imgur.com/3wobFYb.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **SIX**");
                    break;
            }

            event.reply(embedBuilder.build());

        } else if (sub_command.equalsIgnoreCase("8")) {

            int random_num = ThreadLocalRandom.current().nextInt(0, 8);
            switch (random_num) {
                case 0:
                    embedBuilder.setThumbnail("https://i.imgur.com/tKyohua.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **ONE**");
                    break;
                case 1:
                    embedBuilder.setThumbnail("https://i.imgur.com/JO0t2rm.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **TWO**");
                    break;
                case 2:
                    embedBuilder.setThumbnail("https://i.imgur.com/vurKf1p.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **THREE**");
                    break;
                case 3:
                    embedBuilder.setThumbnail("https://i.imgur.com/dfzwLTm.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **FOUR**");
                    break;
                case 4:
                    embedBuilder.setThumbnail("https://i.imgur.com/JPRmZpr.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **FIVE**");
                    break;
                case 5:
                    embedBuilder.setThumbnail("https://i.imgur.com/po9zfyg.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **SIX**");
                    break;
                case 6:
                    embedBuilder.setThumbnail("https://i.imgur.com/WYQH0Bu.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **SEVEN**");
                    break;
                case 7:
                    embedBuilder.setThumbnail("https://i.imgur.com/x8eLabh.png");
                    event.reply("**"+event.getMember().getAsMention()+"** rolled a... **EIGHT**");
                    break;
            }

            event.reply(embedBuilder.build());

        } else {
            event.reply("Huh? I only know how to roll a **6** or **8** sided dice!");
        }

    }
}
