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
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class CoinFlip extends Command {

    public CoinFlip() {
        this.name = "coinflip";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "CoinFlip", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;


        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.darkGray);

        int random_num = ThreadLocalRandom.current().nextInt(0, 2);
        switch (random_num) {
            case 0:
                embedBuilder.setThumbnail("https://i.imgur.com/LFyAeFC.png");
                event.reply("HEADS!");
                break;
            case 1:
                embedBuilder.setThumbnail("https://i.imgur.com/udJdYBt.png");
                event.reply("TAILS!");
        }

        event.reply(embedBuilder.build());

    }
}
