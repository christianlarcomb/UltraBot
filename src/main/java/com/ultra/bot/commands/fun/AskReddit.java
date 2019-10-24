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
import com.ultra.bot.utilities.data.ScrapeRedditPosts;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class AskReddit extends Command {

    public AskReddit() {
        this.name = "askreddit";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{"askred", "askr"};
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "AskReddit", false))
            return;

        /** Command Availability **/
        if (!new Checks().SetAvailability(event, false, "AskReddit"))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        ScrapeRedditPosts.RedditScraper(event, "https://www.reddit.com/r/comedyheaven/");

    }
}
