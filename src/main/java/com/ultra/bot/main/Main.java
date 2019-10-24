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

package com.ultra.bot.main;

import com.ultra.bot.commands.config.Configuration;
import com.ultra.bot.commands.config.Inquiring;
import com.ultra.bot.commands.economy.Coins;
import com.ultra.bot.commands.economy.Shop;
import com.ultra.bot.commands.fun.*;
import com.ultra.bot.commands.gaming.GuessThat;
import com.ultra.bot.commands.gaming.RocketLeague;
import com.ultra.bot.commands.general.About;
import com.ultra.bot.commands.general.Cmds;
import com.ultra.bot.commands.general.Help;
import com.ultra.bot.commands.moderation.Dunce;
import com.ultra.bot.commands.moderation.UnDunce;
import com.ultra.bot.commands.tools.*;
import com.ultra.bot.commands.config.rolSelBox;
import com.ultra.bot.commands.user.*;
import com.ultra.bot.commands.tools.*;
import com.ultra.bot.frameworks.economy.InviteReward;
import com.ultra.bot.frameworks.database.UpdateGuildSync;
import com.ultra.bot.frameworks.database.JoinLeaveDataSync;
import com.ultra.bot.frameworks.economy.MessagingReward;
import com.ultra.bot.frameworks.rolemanagement.JoinRoles;
import com.ultra.bot.frameworks.notifications.BotJoinMsg;
import com.ultra.bot.frameworks.rolemanagement.RewardRoles;
import com.ultra.bot.frameworks.rolemanagement.RoleAdd;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.frameworks.database.MongoConnect;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.ultra.bot.frameworks.notifications.MemberJoin;
import com.ultra.bot.commands.fun.*;
import com.ultra.bot.commands.user.Suggest;
import com.ultra.bot.commands.user.Users;
import net.dv8tion.jda.core.entities.Game;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {

    // Instantiating JDA
    public static JDA jda;

    public static void main(String[] args) throws Exception {

        /********** JDA BUILDER **********************/
        jda = new JDABuilder(AccountType.BOT)
                // Sets the token
                .setToken(new MetaData().botToken)
                .setGame(Game.listening("requests"))
                //What the bot is doing when live
                .buildBlocking();
        /*********************************************/

        /** Initializing Event Waiter **/
        EventWaiter waiter = new EventWaiter();

        /** COMMAND BUILDER **/
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setHelpWord(MetaData.bot_prefix + " help");
        builder.setOwnerId("266692782318419978");
        // SETTING THE COMMAND PREFIX
        builder.setPrefix(MetaData.bot_prefix);

        /*********************************************/

        /** Config Commands **/
        builder.addCommand(new Configuration(waiter));
        builder.addCommand(new Inquiring());
        builder.addCommand(new rolSelBox());

        /** Economy Commands **/
        builder.addCommand(new Coins());
        builder.addCommand(new Shop(waiter));

        /** Fun Commands **/
        builder.addCommand(new CoinFlip());
        builder.addCommand(new Magic8Ball());
        builder.addCommand(new RollDice());
        builder.addCommand(new SuckMy());
        builder.addCommand(new Meme());
        builder.addCommand(new Cancer());
        builder.addCommand(new AskReddit());
        builder.addCommand(new Dogs());
        builder.addCommand(new Cats());
        builder.addCommand(new Say());

        /** Gaming Commands **/
        builder.addCommand(new GuessThat(waiter));
        builder.addCommand(new RocketLeague());

        /** General Commands **/
        builder.addCommand(new About());
        builder.addCommand(new Cmds());
        builder.addCommand(new Help());

        /** Meta Commands **/

        /** Moderation Commands **/
        builder.addCommand(new Dunce());
        builder.addCommand(new UnDunce());

        /** Tools Commands **/
        builder.addCommand(new Mongo());
        builder.addCommand(new Guild());
        builder.addCommand(new Purge());
        builder.addCommand(new RoleID());
        builder.addCommand(new EmoteID());
        builder.addCommand(new Version());

        /** User Commands **/
        builder.addCommand(new Suggest(waiter));
        builder.addCommand(new Users());


        /*********************************************/

        // Building the CommandClient
        CommandClient client = builder.build();
        /*************************************/

        // REGISTERING EVENTS WITH JDA
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new RoleAdd());
        jda.addEventListener(new Checks());
        jda.addEventListener(new InviteReward());
        jda.addEventListener(new MessagingReward());
        jda.addEventListener(new JoinLeaveDataSync());
        jda.addEventListener(new UpdateGuildSync());
        jda.addEventListener(new RewardRoles());
        jda.addEventListener(new BotJoinMsg());
        jda.addEventListener(new MongoConnect());
        jda.addEventListener(new JoinRoles());
        jda.addEventListener(waiter);
        jda.addEventListener(client);

    }
}
