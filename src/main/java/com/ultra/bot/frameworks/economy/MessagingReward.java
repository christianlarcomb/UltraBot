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
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.util.HashMap;

public class MessagingReward extends ListenerAdapter {

    HashMap<User, Integer> message_count = new HashMap<>();

    private int Coin_Multiplyer = MetaData.Coin_Multiplyer;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {

            // GET: Message sender
            User message_sender = event.getChannel().getHistory().retrievePast(1).complete().get(0).getAuthor();

            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
            Document curr_doc_found = (Document) mongoCollection.find(new Document("Features", "Currency")).first();
            int txt_amt = curr_doc_found.getInteger("Amt_Earned_TextChannel");

            // CHECK: If Map is null, if so, give it 1.
            if (message_count.get(message_sender) == null) {
                message_count.put(message_sender, 1);
            } else {

                // VAR: sent messages & setting sent_mes + 1
                int sent_messages = message_count.get(message_sender);
                message_count.put(message_sender, sent_messages + 1);

                //System.out.println("Message Count: " + sent_messages);

                // GETTING: Remainder in increments of 100
                int number_mod = sent_messages % 100;
                if (number_mod == 0 && sent_messages != 0) {

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(Color.DARK_GRAY);
                    embedBuilder.setImage("https://i.imgur.com/RoxMzGa.png");

                    // SEND COIN RECEIVED MESSAGES
                    event.getGuild().getTextChannelsByName("logs", true).get(0).
                            sendMessage(message_sender.getAsMention()).queue();
                    event.getGuild().getTextChannelsByName("logs", true).get(0).
                            sendMessage(embedBuilder.build()).queue();


                    Document user_found = (Document) mongoCollection.find(new Document("User", message_sender.getId())).first();
                    Document doc_to_insert = new Document("User", message_sender.getId());

                    // System.out.println("Gate 3");

                    int coin_total_calc = user_found.getInteger("Coins") + (txt_amt * Coin_Multiplyer);
                    int coin_inv_calc = user_found.getInteger("Coins_From_Messages") + (txt_amt * Coin_Multiplyer);

                    // System.out.println("Gate 4");

                    doc_to_insert.append("Coins", coin_total_calc);
                    doc_to_insert.append("Coins_From_Messages", coin_inv_calc);

                    // UPDATING THE COINS IN THE DB
                    Bson updateoperation = new Document("$set", doc_to_insert);
                    mongoCollection.updateOne(user_found, updateoperation);

                    // System.out.println(user.getName() + " has invited "+ event.getGuild().getInvites().complete().get(i).getUses() + "people.");

                    /** Closing Connection **/
                    //MongoConnect.closingDatabase();

                }
            }
        }
    }
}
