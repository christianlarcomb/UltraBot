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

package com.ultra.bot.frameworks.rolemanagement;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.mongodb.client.MongoCollection;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class RoleAdd extends ListenerAdapter {

    // Defining important classes and objects
    //EmoteArrays em = new EmoteArrays();

    // EMOTES -> COLLEGE
    public Map<String, String> emoteToRocketRank = new HashMap<>();
    {
        // Universities are Alphabetical
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_a:"), "Notifications");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_b:"), "DM Friendly");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_c:"), "Competitive Player");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_d:"), "Non-Competitive Player");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_e:"), "Grand Champion");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_f:"), "Champion");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_g:"), "Diamond");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_h:"), "Platinum");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_i:"), "Gold");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_j:"), "Silver");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_k:"), "Bronze");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_l:"), "Solos");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_m:"), "Duos");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_n:"), "Threes");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_o:"), "Fours");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_p:"), "Dropshot");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_q:"), "Hoops");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_r:"), "Hockey");
        emoteToRocketRank.put(EmojiParser.parseToUnicode(":regional_indicator_symbol_s:"), "Rumble");

    }

    //
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {

        if (!event.getUser().isBot()) {

            if(event.getChannel().getName().equals("role-selector")) {

                // This is not functioning yet.
                if (emoteToRocketRank.containsKey(EmojiParser.parseToUnicode(event.getReactionEmote().getName()))) {

                    String reactionEmote = event.getReactionEmote().getName();
                    Role selectedRole = event.getGuild().getRolesByName(emoteToRocketRank.get(reactionEmote), true).get(0);

                    event.getGuild().getController().addRolesToMember(event.getMember(), selectedRole).complete();

                }

            } else if (event.getChannel().getName().equals("shop")) {

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document user_found = (Document) mongoCollection.find(new Document("User", event.getMember().getUser().getId())).first();

                String reactionEmote = event.getReactionEmote().getName();
                Role selectedRole = event.getGuild().getRolesByName(emoteToRocketRank.get(reactionEmote), true).get(0);

                event.getGuild().getController().addRolesToMember(event.getMember(), selectedRole).complete();

            }
        }
    }

    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {

        if (!event.getUser().isBot()) {

            if(event.getChannel().getName().equals("role-selector")) {

                // This is not functioning yet.
                if (emoteToRocketRank.containsKey(EmojiParser.parseToUnicode(event.getReactionEmote().getName()))) {

                    String reactionEmote = event.getReactionEmote().getName();
                    Role selectedRole = event.getGuild().getRolesByName(emoteToRocketRank.get(reactionEmote), true).get(0);

                    event.getGuild().getController().removeRolesFromMember(event.getMember(), selectedRole).complete();

                }

            }
        }
    }


}
