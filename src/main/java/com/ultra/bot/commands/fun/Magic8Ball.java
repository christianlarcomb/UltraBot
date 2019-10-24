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

import com.ultra.bot.frameworks.verification.Checks;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Magic8Ball extends Command {

    ArrayList<String> responses = new ArrayList<>();
    {
        // Default Positive Answers
        responses.add("It is certain");
        responses.add("Without a doubt");
        responses.add("You may rely on it");
        responses.add("Yes definitely");
        responses.add("It is decidedly so");
        responses.add("As I see it, yes");
        responses.add("Most likely");
        responses.add("Yes");
        responses.add("Outlook good");

        // Default Neutral Answers
        responses.add("Reply hazy try again");
        responses.add("Better not tell you now");
        responses.add("Ask again later");
        responses.add("Cannot predict now");
        responses.add("Concentrate and ask again");

        // Negative Answers
        responses.add("Don’t count on it");
        responses.add("Outlook not so good");
        responses.add("My sources say no");
        responses.add("Very doubtful");
        responses.add("My reply is no");
    }

    public Magic8Ball() {
        this.name = "magic8ball";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"magic8", "mag8"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Magic8Ball", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // Checking if it's not a bot
        if(!event.getMember().getUser().isBot()){

            int random_num = ThreadLocalRandom.current().nextInt(0, 18);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(responses.get(random_num));
            embedBuilder.setColor(Color.DARK_GRAY);
            embedBuilder.setThumbnail("https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.clipartkid.com%2Fimages%2F209%2Fdlnorton-eightball-clipart-by-dlnorton-on-deviantart-b8X73I-clipart.png&f=1");

            // Sending the embed and tag
            event.reply(event.getMember().getAsMention());
            event.reply(embedBuilder.build());

        }

    }
}
