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

package com.ultra.bot.frameworks.economy;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.utilities.data.MetaData;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.util.HashMap;

public class InviteReward extends ListenerAdapter {

    private int Coin_Multiplyer = MetaData.Coin_Multiplyer;

    private HashMap<User, Boolean> previously_joined_user = new HashMap<>();
    private HashMap<User, Integer> previously_joined_user_count = new HashMap<>();

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {


        // CHECK: User joined before, if so, ignore reward system.
        if (previously_joined_user.get(event.getUser()) == null) {

            // Adding the user to the Previously Joined User list to prevent rejoins.
            previously_joined_user.put(event.getUser(), true);

            /*--------------------------- LOOP THROUGH ALL INVITES ---------------------------*/
            for (int i = 0; i < event.getGuild().getInvites().complete().size(); i++) {

                User inviter = event.getGuild().getInvites().complete().get(i).getInviter();

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document inviter_doc_found = (Document) mongoCollection.find(new Document("User", inviter.getId())).first();

                if (inviter_doc_found != null) {

                    int invite_usage = event.getGuild().getInvites().complete().get(i).getUses();
                    int mongo_invite_usage = inviter_doc_found.getInteger("Invite_Count");

                    // CHECK: Guild Invite Count > Whats in the CACHED DB
                    if (invite_usage > mongo_invite_usage) {

                        Document curr_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();
                        int currency = curr_doc_found.getInteger("Amt_Earned_Inviting");

                        try {

                            // BUILDING: Embed Message
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(Color.DARK_GRAY);

                            // +3 Coins -> Implement Array of Images based on coin count
                            embedBuilder.setImage("https://i.imgur.com/7l5f0tL.png");

                            // SEND COIN RECEIVED MESSAGES
                            event.getGuild().getTextChannelsByName("logs", true).get(0).
                                    sendMessage(event.getGuild().getInvites().complete().get(i).getInviter().getAsMention()).queue();
                            event.getGuild().getTextChannelsByName("logs", true).get(0).
                                    sendMessage(embedBuilder.build()).queue();

                            /** CREATE & APPEND DOC **/
                            Document doc_to_insert = new Document("User", inviter.getId());
                            doc_to_insert.append("Invite_Count", invite_usage);
                            doc_to_insert.append("Coins_From_Invites", inviter_doc_found.getInteger("Coins_From_Invites") + (currency * Coin_Multiplyer));
                            doc_to_insert.append("Coins", inviter_doc_found.getInteger("Coins") + (currency * Coin_Multiplyer));

                            /** UPDATING MONGODB **/
                            // MONGO: Getting core documents and class.
                            Document user_found = (Document) mongoCollection.find(new Document("User", event.getGuild().getInvites().complete().get(i).getInviter().getId())).first();
                            Bson updateoperation = new Document("$set", doc_to_insert);
                            mongoCollection.updateOne(user_found, updateoperation);


                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            System.out.println("Throwing the exception still!");
                        }

                    }

                } else {
                    System.out.println("Inviter is no longer in the cache !!");
                }
            }

        } else {

            int count, threshold;
            if(previously_joined_user_count.get(event.getUser()) == null){
                count = 1;
                previously_joined_user_count.put(event.getUser(), 1);
            } else {
                count = previously_joined_user_count.get(event.getUser()) + 1;
                previously_joined_user_count.put(event.getUser(), count);
            }

            try{
                threshold = count % 5;
                if(threshold == 0){
                    event.getGuild().getTextChannelsByName("logs", true).get(0).sendMessage(event.getUser().getName() + " has re-joined "+count+" times! Not giving rewards!").queue();
                }
            } catch (Exception e) { System.out.println(event.getUser().getName() + " has already joined "+count+" times before! Not giving rewards!\n\n" + "+ log channel is not working!"); }
        }
    }
}
