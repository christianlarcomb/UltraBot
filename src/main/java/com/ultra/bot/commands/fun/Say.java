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
import com.ultra.bot.main.Main;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class Say extends Command {

    public Say() {
        this.name = "say";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Availability **/
        if (!new Checks().SetAvailability(event, true, "Say"))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        // Replacement prep!
        final Member event_member = event.getMember();
        final Member guild_owner = event.getGuild().getOwner();
        final Member self_user_bot = event.getGuild().getSelfMember();
        final Guild guild = event.getGuild();

        // Deleting the command message
        new PurgeMsgs().purgeMessages(event, 1);

        String say_this = event.getMessage().getContentRaw().substring(6)
                .replace("[guild_owner_name]", guild_owner.getUser().getName())
                .replace("[guild_owner_mention]", guild_owner.getAsMention())
                .replace("[event_user_name]", event_member.getUser().getName())
                .replace("[event_user_mention]", event_member.getAsMention())
                .replace("[self_user_name]", self_user_bot.getUser().getName())
                .replace("[self_user_mention]", self_user_bot.getAsMention())
                .replace("[guild_member_count]", Integer.toString(guild.getMembers().size()))
                .replace("[bot_guild_count]", Integer.toString(Main.jda.getGuilds().size()))
                .replace("[bot_member_count]", Integer.toString(Main.jda.getUsers().size()));

        event.reply(say_this);


    }
}
