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

package com.ultra.bot.frameworks.notifications;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.main.Main;
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MemberJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        /** Feature Enabled Check **/
        //if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "WelcomeMessage", true))
            //return;

        try {

            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
            Document messages_doc_found =  (Document) mongoCollection.find(new Document("Features", "WelcomeMessage")).first();

            /* Making the custom Embed */
            EmbedBuilder embed_message = new EmbedBuilder();
            embed_message.setColor(Color.DARK_GRAY);

            // Replacement prep!
            final Member event_member = event.getMember();
            final Member guild_owner = event.getGuild().getOwner();
            final Member self_user_bot = event.getGuild().getSelfMember();
            final Guild guild = event.getGuild();

            // External Data Variables
            try {

                // Message Availability Finder

                ArrayList<Integer> available_msgs = new ArrayList<>();
                for(int i = 1; i <= MetaData.Welcome_Messages_Count; i++)
                {
                    if(!messages_doc_found.getString("Message_"+i+"_Text").equalsIgnoreCase("") ||
                            !messages_doc_found.getString("Message_"+i+"_Thumbnail").equalsIgnoreCase("") ||
                            !messages_doc_found.getString("Message_"+i+"_Image").equalsIgnoreCase(""))
                    {
                        available_msgs.add(i);
                        //System.out.println("Message CONSIDERED available: "+i);
                    }

                }

                // Which Message
                //System.out.println("Amount of available Messages: " + available_msgs.size());
                int Random_Num = ThreadLocalRandom.current().nextInt(0, available_msgs.size());
                //System.out.println("Random number is: " + Random_Num);
                int Msg_Selected = available_msgs.get(Random_Num);

                /* MESSAGE TEXT PROCESS */
                try {
                    final String message_text = messages_doc_found.getString("Message_" + Msg_Selected + "_Text")
                            .replace("[guild_owner_name]", guild_owner.getUser().getName())
                            .replace("[guild_owner_mention]", guild_owner.getAsMention())
                            .replace("[event_user_name]", event_member.getUser().getName())
                            .replace("[event_user_mention]", event_member.getAsMention())
                            .replace("[self_user_name]", self_user_bot.getUser().getName())
                            .replace("[self_user_mention]", self_user_bot.getAsMention())
                            .replace("[guild_member_count]", Integer.toString(guild.getMembers().size()))
                            .replace("[bot_guild_count]", Integer.toString(Main.jda.getGuilds().size()))
                            .replace("[bot_member_count]", Integer.toString(Main.jda.getUsers().size()));

                    // Sending the message text
                    if (messages_doc_found.getString("TextChannel").isEmpty() || messages_doc_found.getString("TextChannel") == null) {
                        guild.getDefaultChannel().sendMessage(message_text).queue();
                    } else {
                        String text_channel_id = messages_doc_found.getString("TextChannel")
                                .replace("<", "")
                                .replace("#", "")
                                .replace(">", "");

                        guild.getTextChannelById(text_channel_id).sendMessage(message_text).queue();
                    }

                } catch (Exception e) { e.printStackTrace(); }


                /* MESSAGE THUMBNAIL PROCESS */
                try {
                    String message_thumbnail = messages_doc_found.getString("Message_" + Msg_Selected + "_Thumbnail")
                            .replace("[event_user_thumbnail]", event_member.getUser().getAvatarUrl());

                    embed_message.setThumbnail(message_thumbnail);
                } catch (Exception e) { /**e.printStackTrace();**/}

                final String message_image = messages_doc_found.getString("Message_" + Msg_Selected + "_Image");


                if (messages_doc_found.getString("TextChannel").isEmpty() || messages_doc_found.getString("TextChannel") == null) {

                    guild.getDefaultChannel().sendMessage("Please select a **Text-Channel** for your configured **Welcome Messages** to appear in!").queue();

                } else {
                    String text_channel_id = messages_doc_found.getString("TextChannel")
                            .replace("<", "")
                            .replace("#", "")
                            .replace(">", "");

                    try {
                        guild.getTextChannelById(text_channel_id).sendMessage(embed_message.build()).queue();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        //System.out.println("THIS IS BEING TRIGGERED?");
                    }
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }


        } catch (HierarchyException e) {

            event.getGuild().getTextChannelsByName("welcome", true).get(0).sendMessage(new ErrorEmbed("Error", "Hierarchy Error!\n" +
                    "Make sure the bot is above the roles it is trying to assign!", event.getGuild()).getError().build()).queue();
        }

    }
}


