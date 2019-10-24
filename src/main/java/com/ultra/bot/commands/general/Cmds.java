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

import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.frameworks.purging.PurgeMsgs;
import com.ultra.bot.utilities.data.MetaData;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.util.ArrayList;

public class Cmds extends Command {

    public Cmds() {
        this.name = "cmds";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE};
        this.guildOnly = false;
        this.cooldown = 3;
        this.aliases = new String[]{
                "commands",
                "command",
                "cmd"
        };
    }

    @Override
    public void execute(CommandEvent event) {

        /** Command Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "Cmds", false))
            return;

        /** Setup Checker **/
        if (!new Checks().SetupCompCheck(event))
            return;

        /** Dunce Checker **/
        if (new Checks().DunceCheck(event))
            return;

        /************ ROLE SETTING ***********/
        /** Discord Administration Roles **/
        Role opRole = event.getGuild().getRolesByName("*", false).get(0);
        Role adminRole = event.getGuild().getRolesByName("adm", false).get(0);
        Role moderatorRole = event.getGuild().getRolesByName("mod", false).get(0);
        Role helperRole = event.getGuild().getRolesByName("hel", false).get(0);

        // Discord Role Checks
        boolean op_role_check = event.getMember().getRoles().contains(opRole);
        boolean admin_role_check = event.getMember().getRoles().contains(adminRole);
        boolean mod_role_check = event.getMember().getRoles().contains(moderatorRole);
        boolean helper_role_check = event.getMember().getRoles().contains(helperRole);
        /*************************************/

        // DELETING THE COMMAND (Keeps the interactive space clean)
        new PurgeMsgs().purgeMessages(event, 1);

        EmbedBuilder config_embed = new EmbedBuilder();
        EmbedBuilder inquiry_embed = new EmbedBuilder();
        EmbedBuilder currency_embed = new EmbedBuilder();
        EmbedBuilder general_embed = new EmbedBuilder();
        EmbedBuilder moderation_embed = new EmbedBuilder();
        EmbedBuilder tools_embed = new EmbedBuilder();
        EmbedBuilder user_embed = new EmbedBuilder();
        EmbedBuilder gaming_embed = new EmbedBuilder();
        EmbedBuilder fun_embed = new EmbedBuilder();

        // Setting Each Colors
        config_embed.setColor    (new Color(102,117,126));
        inquiry_embed.setColor   (new Color(59,135,188));
        currency_embed.setColor  (new Color(251,213,146));
        general_embed.setColor   (new Color(216,157,134));
        moderation_embed.setColor(new Color(85,172,230));
        tools_embed.setColor     (new Color(244,143,53));
        user_embed.setColor      (new Color(255,203,100));
        gaming_embed.setColor    (new Color(41,47,51));
        fun_embed.setColor       (new Color(255,203,100));

        // Checking if the user is a bot
        if (!event.getMember().getUser().isBot()) {

            // IF OP
            if (op_role_check) {

                // Send the embedded message
                event.getChannel().sendMessage("Here are some commands **" + event.getMember().getAsMention() + "**. :sunglasses:").queue();

                config_embed.setDescription(":gear: **Config**\n" +
                        "``" + MetaData.bot_prefix + "!`` -> *Configuration Commands List*\n" +
                        "``" + MetaData.bot_prefix + "! list`` -> *lists toggled items*\n" +
                        "``" + MetaData.bot_prefix + "! toggle <All, CmdName> (Features, Commands)`` -> *toggle Cmds / Ftrs*\n" +
                        "``" + MetaData.bot_prefix + "! joinroles <set, reset> <#> <@role>`` -> *setup join roles*\n" +
                        "``" + MetaData.bot_prefix + "! welcomemessage <set, reset, shortcuts> <#>`` -> *Welcome Message configuration*\n" +
                        "``" + MetaData.bot_prefix + "! currency <setup, reset>`` -> *currency configuration*\n" +
                        "\n");

                inquiry_embed.setDescription(":information_source: **Inquiry**\n" +
                        "``" + MetaData.bot_prefix + "?`` -> *Inquiry Commands List*\n" +
                        "``" + MetaData.bot_prefix + "? welcomemessage`` -> *Welcome Message(s) Info*\n" +
                        "``" + MetaData.bot_prefix + "? joinroles`` -> *JoinRoles Info*\n" +
                        "``" + MetaData.bot_prefix + "? currency`` -> *Currency Info*\n" +
                        "\n");

                currency_embed.setDescription(":moneybag: **Currency**\n" +
                        "``" + MetaData.bot_prefix + "$`` -> *Coin Balance*\n" +
                        "``" + MetaData.bot_prefix + "$ summary`` -> *coins earned summary*\n" +
                        "``" + MetaData.bot_prefix + "$ give @user #amt`` -> *gives coins*\n" +
                        "``" + MetaData.bot_prefix + "$ remove @user #amt`` -> *removes coins*\n" +
                        "``" + MetaData.bot_prefix + "$ guide`` -> *coin earning guide*\n" +
                        "``" + MetaData.bot_prefix + "$ invite`` -> *coin earning by inviting*\n" +
                        "\n");

                general_embed.setDescription(":notebook_with_decorative_cover: **General**\n" +
                        "``" + MetaData.bot_prefix + " about`` -> *learn about "+MetaData.bot_name.toUpperCase()+"*\n" +
                        "``" + MetaData.bot_prefix + " cmds`` -> *shows a this list*\n" +
                        "``" + MetaData.bot_prefix + " help`` -> get some help using this bot\n" +
                        "\n");

                moderation_embed.setDescription(":shield: **Moderation**\n" +
                        "``" + MetaData.bot_prefix + " dunce @user`` -> *blocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "``" + MetaData.bot_prefix + " undunce @user`` -> *unblocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "\n");

                tools_embed.setDescription(":tools: **Tools**\n" +
                        "``" + MetaData.bot_prefix + " purge #amount`` -> *deletes channel messages*\n" +
                        "``" + MetaData.bot_prefix + " roleid @role`` -> *shows a role's id*\n" +
                        "``" + MetaData.bot_prefix + " version`` -> *shows the bots current version*\n" +
                        "``" + MetaData.bot_prefix + " mongo status`` -> *shows the databases connection status*\n" +
                        "``" + MetaData.bot_prefix + " mongo upload`` -> *uploads & corrects a guild in the database*\n" +
                        "\n");

                user_embed.setDescription(":sunglasses: **User**\n" +
                        "``" + MetaData.bot_prefix + " suggest`` -> *creates a suggestion box*\n" +
                        "``" + MetaData.bot_prefix + " user @user`` -> *shows summary of a user*\n" +
                        "\n");

                gaming_embed.setDescription(
                        ":video_game: **Gaming**\n" +
                                "``" + MetaData.bot_prefix + " rocketleague user <steam community id>`` -> *shows users rocketleague stats*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague item <rocketleague item name>`` -> *shows the rocketleague item*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague reddit`` -> *shows a random rocketleague post from reddit*\n" +
                                "\n");

                fun_embed.setDescription(":laughing: **Fun**\n" +
                        "``" + MetaData.bot_prefix + " askreddit`` -> *gets a question from reddit*\n" +
                        "``" + MetaData.bot_prefix + " cancer`` -> *random cancerous memes*\n" +
                        "``" + MetaData.bot_prefix + " cats`` -> *aww cats*\n" +
                        "``" + MetaData.bot_prefix + " dogs`` -> *aww doges*\n" +
                        "``" + MetaData.bot_prefix + " coinflip`` -> *flip a coin*\n" +
                        "``" + MetaData.bot_prefix + " magic8ball`` -> *find your fortune*\n" +
                        "``" + MetaData.bot_prefix + " meme`` -> *random meme from the webs*\n" +
                        "``" + MetaData.bot_prefix + " rolldice <6, 8>`` -> *roll a 6 or 8 sides die*\n" +
                        "\n");


                ArrayList<EmbedBuilder> embeds_list = new ArrayList<>();
                embeds_list.add(config_embed);
                embeds_list.add(inquiry_embed);
                embeds_list.add(currency_embed);
                embeds_list.add(general_embed);
                embeds_list.add(moderation_embed);
                embeds_list.add(tools_embed);
                embeds_list.add(user_embed);
                embeds_list.add(gaming_embed);
                embeds_list.add(fun_embed);

                for(EmbedBuilder embeds : embeds_list){
                    event.getChannel().sendMessage(embeds.build()).queue();
                }

            } else if (admin_role_check) {                                                            // IF Admin has the Tutor Role

                // Send the embedded message
                event.getChannel().sendMessage("Here are some commands **" + event.getMember().getAsMention() + "**. :sunglasses:").queue();

                inquiry_embed.setDescription(":information_source: **Inquiry**\n" +
                        "``" + MetaData.bot_prefix + "?`` -> *Inquiry Commands List*\n" +
                        "``" + MetaData.bot_prefix + "? welcomemessage`` -> *Welcome Message(s) Info*\n" +
                        "``" + MetaData.bot_prefix + "? joinroles`` -> *JoinRoles Info*\n" +
                        "``" + MetaData.bot_prefix + "? currency`` -> *Currency Info*\n" +
                        "\n");

                currency_embed.setDescription(":moneybag: **Currency**\n" +
                        "``" + MetaData.bot_prefix + "$`` -> *Coin Balance*\n" +
                        "``" + MetaData.bot_prefix + "$ summary`` -> *coins earned summary*\n" +
                        "``" + MetaData.bot_prefix + "$ give @user #amt`` -> *gives coins*\n" +
                        "``" + MetaData.bot_prefix + "$ remove @user #amt`` -> *removes coins*\n" +
                        "``" + MetaData.bot_prefix + "$ guide`` -> *coin earning guide*\n" +
                        "``" + MetaData.bot_prefix + "$ invite`` -> *coin earning by inviting*\n" +
                        "\n");

                general_embed.setDescription(":notebook_with_decorative_cover: **General**\n" +
                        "``" + MetaData.bot_prefix + " about`` -> *learn about "+MetaData.bot_name.toUpperCase()+"*\n" +
                        "``" + MetaData.bot_prefix + " cmds`` -> *shows a this list*\n" +
                        "``" + MetaData.bot_prefix + " help`` -> get some help using this bot\n" +
                        "\n");

                moderation_embed.setDescription(":shield: **Moderation**\n" +
                        "``" + MetaData.bot_prefix + " dunce @user`` -> *blocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "``" + MetaData.bot_prefix + " undunce @user`` -> *unblocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "\n");

                tools_embed.setDescription(":tools: **Tools**\n" +
                        "``" + MetaData.bot_prefix + " purge #amount`` -> *deletes channel messages*\n" +
                        "``" + MetaData.bot_prefix + " roleid @role`` -> *shows a role's id*\n" +
                        "``" + MetaData.bot_prefix + " version`` -> *shows the bots current version*\n" +
                        "``" + MetaData.bot_prefix + " mongo status`` -> *shows the databases connection status*\n" +
                        "``" + MetaData.bot_prefix + " mongo upload`` -> *uploads & corrects a guild in the database*\n" +
                        "\n");

                user_embed.setDescription(":sunglasses: **User**\n" +
                        "``" + MetaData.bot_prefix + " suggest`` -> *creates a suggestion box*\n" +
                        "``" + MetaData.bot_prefix + " user @user`` -> *shows summary of a user*\n" +
                        "\n");

                gaming_embed.setDescription(
                        ":video_game: **Gaming**\n" +
                                "``" + MetaData.bot_prefix + " rocketleague user <steam community id>`` -> *shows users rocketleague stats*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague item <rocketleague item name>`` -> *shows the rocketleague item*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague reddit`` -> *shows a random rocketleague post from reddit*\n" +
                                "\n");

                fun_embed.setDescription(":laughing: **Fun**\n" +
                        "``" + MetaData.bot_prefix + " askreddit`` -> *gets a question from reddit*\n" +
                        "``" + MetaData.bot_prefix + " cancer`` -> *random cancerous memes*\n" +
                        "``" + MetaData.bot_prefix + " cats`` -> *aww cats*\n" +
                        "``" + MetaData.bot_prefix + " dogs`` -> *aww doges*\n" +
                        "``" + MetaData.bot_prefix + " coinflip`` -> *flip a coin*\n" +
                        "``" + MetaData.bot_prefix + " magic8ball`` -> *find your fortune*\n" +
                        "``" + MetaData.bot_prefix + " meme`` -> *random meme from the webs*\n" +
                        "``" + MetaData.bot_prefix + " rolldice <6, 8>`` -> *roll a 6 or 8 sides die*\n" +
                        "\n");


                ArrayList<EmbedBuilder> embeds_list = new ArrayList<>();
                embeds_list.add(inquiry_embed);
                embeds_list.add(currency_embed);
                embeds_list.add(general_embed);
                embeds_list.add(moderation_embed);
                embeds_list.add(tools_embed);
                embeds_list.add(user_embed);
                embeds_list.add(gaming_embed);
                embeds_list.add(fun_embed);

                for(EmbedBuilder embeds : embeds_list){
                    event.getChannel().sendMessage(embeds.build()).queue();
                }

            } else if (mod_role_check) {                                                              // IF user has the Moderator Role

                // Send the embedded message
                event.getChannel().sendMessage("Here are some commands **" + event.getMember().getAsMention() + "**. :sunglasses:").queue();

                currency_embed.setDescription(":moneybag: **Currency**\n" +
                        "``" + MetaData.bot_prefix + "$`` -> *Coin Balance*\n" +
                        "``" + MetaData.bot_prefix + "$ summary`` -> *coins earned summary*\n" +
                        "``" + MetaData.bot_prefix + "$ give @user #amt`` -> *gives coins*\n" +
                        "``" + MetaData.bot_prefix + "$ remove @user #amt`` -> *removes coins*\n" +
                        "``" + MetaData.bot_prefix + "$ guide`` -> *coin earning guide*\n" +
                        "``" + MetaData.bot_prefix + "$ invite`` -> *coin earning by inviting*\n" +
                        "\n");

                general_embed.setDescription(":notebook_with_decorative_cover: **General**\n" +
                        "``" + MetaData.bot_prefix + " about`` -> *learn about "+MetaData.bot_name.toUpperCase()+"*\n" +
                        "``" + MetaData.bot_prefix + " cmds`` -> *shows a this list*\n" +
                        "``" + MetaData.bot_prefix + " help`` -> get some help using this bot\n" +
                        "\n");

                moderation_embed.setDescription(":shield: **Moderation**\n" +
                        "``" + MetaData.bot_prefix + " dunce @user`` -> *blocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "``" + MetaData.bot_prefix + " undunce @user`` -> *unblocks user from using "+MetaData.bot_name.toUpperCase()+" commands*\n" +
                        "\n");

                tools_embed.setDescription(":tools: **Tools**\n" +
                        "``" + MetaData.bot_prefix + " purge #amount`` -> *deletes channel messages*\n" +
                        "``" + MetaData.bot_prefix + " roleid @role`` -> *shows a role's id*\n" +
                        "``" + MetaData.bot_prefix + " version`` -> *shows the bots current version*\n" +
                        "``" + MetaData.bot_prefix + " mongo status`` -> *shows the databases connection status*\n" +
                        "``" + MetaData.bot_prefix + " mongo upload`` -> *uploads & corrects a guild in the database*\n" +
                        "\n");

                user_embed.setDescription(":sunglasses: **User**\n" +
                        "``" + MetaData.bot_prefix + " suggest`` -> *creates a suggestion box*\n" +
                        "``" + MetaData.bot_prefix + " user @user`` -> *shows summary of a user*\n" +
                        "\n");

                gaming_embed.setDescription(
                        ":video_game: **Gaming**\n" +
                                "``" + MetaData.bot_prefix + " rocketleague user <steam community id>`` -> *shows users rocketleague stats*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague item <rocketleague item name>`` -> *shows the rocketleague item*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague reddit`` -> *shows a random rocketleague post from reddit*\n" +
                                "\n");

                fun_embed.setDescription(":laughing: **Fun**\n" +
                        "``" + MetaData.bot_prefix + " askreddit`` -> *gets a question from reddit*\n" +
                        "``" + MetaData.bot_prefix + " cancer`` -> *random cancerous memes*\n" +
                        "``" + MetaData.bot_prefix + " cats`` -> *aww cats*\n" +
                        "``" + MetaData.bot_prefix + " dogs`` -> *aww doges*\n" +
                        "``" + MetaData.bot_prefix + " coinflip`` -> *flip a coin*\n" +
                        "``" + MetaData.bot_prefix + " magic8ball`` -> *find your fortune*\n" +
                        "``" + MetaData.bot_prefix + " meme`` -> *random meme from the webs*\n" +
                        "``" + MetaData.bot_prefix + " rolldice <6, 8>`` -> *roll a 6 or 8 sides die*\n" +
                        "\n");


                ArrayList<EmbedBuilder> embeds_list = new ArrayList<>();
                embeds_list.add(currency_embed);
                embeds_list.add(general_embed);
                embeds_list.add(moderation_embed);
                embeds_list.add(tools_embed);
                embeds_list.add(user_embed);
                embeds_list.add(gaming_embed);
                embeds_list.add(fun_embed);

                for(EmbedBuilder embeds : embeds_list){
                    event.getChannel().sendMessage(embeds.build()).queue();
                }

            } else if (helper_role_check) {                                                           // IF user has the Helper Role

                // Send the embedded message
                event.getChannel().sendMessage("Here are some commands **" + event.getMember().getAsMention() + "**. :sunglasses:").queue();

                currency_embed.setDescription(":moneybag: **Currency**\n" +
                        "``" + MetaData.bot_prefix + "$`` -> *Coin Balance*\n" +
                        "``" + MetaData.bot_prefix + "$ summary`` -> *coins earned summary*\n" +
                        "``" + MetaData.bot_prefix + "$ guide`` -> *coin earning guide*\n" +
                        "``" + MetaData.bot_prefix + "$ invite`` -> *coin earning by inviting*\n" +
                        "\n");

                general_embed.setDescription(":notebook_with_decorative_cover: **General**\n" +
                        "``" + MetaData.bot_prefix + " about`` -> *learn about "+MetaData.bot_name.toUpperCase()+"*\n" +
                        "``" + MetaData.bot_prefix + " cmds`` -> *shows a this list*\n" +
                        "``" + MetaData.bot_prefix + " help`` -> get some help using this bot\n" +
                        "\n");

                tools_embed.setDescription(":tools: **Tools**\n" +
                        "``" + MetaData.bot_prefix + " version`` -> *shows the bots current version*\n" +
                        "\n");

                user_embed.setDescription(":sunglasses: **User**\n" +
                        "``" + MetaData.bot_prefix + " user @user`` -> *shows summary of a user*\n" +
                        "\n");

                gaming_embed.setDescription(":video_game: **Gaming**\n" +
                                "``" + MetaData.bot_prefix + " rocketleague user <steam community id>`` -> *shows users rocketleague stats*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague item <rocketleague item name>`` -> *shows the rocketleague item*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague reddit`` -> *shows a random rocketleague post from reddit*\n" +
                                "\n");

                fun_embed.setDescription(":laughing: **Fun**\n" +
                        "``" + MetaData.bot_prefix + " askreddit`` -> *gets a question from reddit*\n" +
                        "``" + MetaData.bot_prefix + " cancer`` -> *random cancerous memes*\n" +
                        "``" + MetaData.bot_prefix + " cats`` -> *aww cats*\n" +
                        "``" + MetaData.bot_prefix + " dogs`` -> *aww doges*\n" +
                        "``" + MetaData.bot_prefix + " coinflip`` -> *flip a coin*\n" +
                        "``" + MetaData.bot_prefix + " magic8ball`` -> *find your fortune*\n" +
                        "``" + MetaData.bot_prefix + " meme`` -> *random meme from the webs*\n" +
                        "``" + MetaData.bot_prefix + " rolldice <6, 8>`` -> *roll a 6 or 8 sides die*\n" +
                        "\n");



                ArrayList<EmbedBuilder> embeds_list = new ArrayList<>();
                embeds_list.add(currency_embed);
                embeds_list.add(general_embed);
                embeds_list.add(tools_embed);
                embeds_list.add(user_embed);
                embeds_list.add(gaming_embed);
                embeds_list.add(fun_embed);

                for(EmbedBuilder embeds : embeds_list){
                    event.getChannel().sendMessage(embeds.build()).queue();
                }

            } else {                                                                                 // ELSE user has the Default Role

                // Send the embedded message
                event.getChannel().sendMessage("Here are some commands **" + event.getMember().getAsMention() + "**. :sunglasses:").queue();

                currency_embed.setDescription(":moneybag: **Currency**\n" +
                        "``" + MetaData.bot_prefix + "$`` -> *Coin Balance*\n" +
                        "``" + MetaData.bot_prefix + "$ summary`` -> *coins earned summary*\n" +
                        "``" + MetaData.bot_prefix + "$ guide`` -> *coin earning guide*\n" +
                        "``" + MetaData.bot_prefix + "$ invite`` -> *coin earning by inviting*\n" +
                        "\n");

                general_embed.setDescription(":notebook_with_decorative_cover: **General**\n" +
                        "``" + MetaData.bot_prefix + " about`` -> *learn about "+MetaData.bot_name.toUpperCase()+"*\n" +
                        "``" + MetaData.bot_prefix + " cmds`` -> *shows a this list*\n" +
                        "``" + MetaData.bot_prefix + " help`` -> get some help using this bot\n" +
                        "\n");

                tools_embed.setDescription(":tools: **Tools**\n" +
                        "``" + MetaData.bot_prefix + " version`` -> *shows the bots current version*\n" +
                        "\n");

                user_embed.setDescription(":sunglasses: **User**\n" +
                        "``" + MetaData.bot_prefix + " user @user`` -> *shows summary of a user*\n" +
                        "\n");

                gaming_embed.setDescription(":video_game: **Gaming**\n" +
                                "``" + MetaData.bot_prefix + " rocketleague user <steam community id>`` -> *shows users rocketleague stats*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague item <rocketleague item name>`` -> *shows the rocketleague item*\n" +
                                "``" + MetaData.bot_prefix + " rocketleague reddit`` -> *shows a random rocketleague post from reddit*\n" +
                                "\n");

                fun_embed.setDescription(":laughing: **Fun**\n" +
                        "``" + MetaData.bot_prefix + " askreddit`` -> *gets a question from reddit*\n" +
                        "``" + MetaData.bot_prefix + " cancer`` -> *random cancerous memes*\n" +
                        "``" + MetaData.bot_prefix + " cats`` -> *aww cats*\n" +
                        "``" + MetaData.bot_prefix + " dogs`` -> *aww doges*\n" +
                        "``" + MetaData.bot_prefix + " coinflip`` -> *flip a coin*\n" +
                        "``" + MetaData.bot_prefix + " magic8ball`` -> *find your fortune*\n" +
                        "``" + MetaData.bot_prefix + " meme`` -> *random meme from the webs*\n" +
                        "``" + MetaData.bot_prefix + " rolldice <6, 8>`` -> *roll a 6 or 8 sides die*\n" +
                        "\n");

                ArrayList<EmbedBuilder> embeds_list = new ArrayList<>();
                embeds_list.add(currency_embed);
                embeds_list.add(general_embed);
                embeds_list.add(tools_embed);
                embeds_list.add(user_embed);
                embeds_list.add(gaming_embed);
                embeds_list.add(fun_embed);

                for(EmbedBuilder embeds : embeds_list){
                    event.getChannel().sendMessage(embeds.build()).queue();
                }

            }

        }
    }
}
