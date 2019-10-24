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
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;

public class RewardRoles extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        if (!event.getUser().isBot()) {

            // Getting size of the invite list
            int invite_list_size = event.getGuild().getInvites().complete().size();

            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

            // LOOP: Checking all invites.
            for (int i = 0; i < invite_list_size; i++) {

                try {

                    // GET: The inviter
                    User inviter_user = event.getGuild().getInvites().complete().get(i).getInviter();
                    // Can check roles
                    Member inviter_member = event.getGuild().getMemberById(inviter_user.getId());

                    // CONFIGURING: Mongo as necessary -> finding and defining necessary documents

                    Document user_found = (Document) mongoCollection.find(new Document("User", inviter_user.getId())).first();

                    // MONGO -> GET: Users coins
                    int coins = user_found.getInteger("Coins");

                    Role role_one = event.getGuild().getRolesByName("one", true).get(0);
                    Role role_two = event.getGuild().getRolesByName("two", true).get(0);
                    Role role_three = event.getGuild().getRolesByName("three", true).get(0);
                    Role role_four = event.getGuild().getRolesByName("four", true).get(0);
                    Role role_five = event.getGuild().getRolesByName("five", true).get(0);
                    Role role_six = event.getGuild().getRolesByName("the six", true).get(0);

                    Collection<Role> role_one_coll = new ArrayList<>();
                    Collection<Role> role_two_coll = new ArrayList<>();
                    ArrayList<Role> role_three_coll = new ArrayList<>();
                    ArrayList<Role> role_four_coll = new ArrayList<>();
                    ArrayList<Role> role_five_coll = new ArrayList<>();
                    ArrayList<Role> role_six_coll = new ArrayList<>();
                    {
                        role_one_coll.add(role_one);
                        role_two_coll.add(role_two);
                        role_three_coll.add(role_three);
                        role_four_coll.add(role_four);
                        role_five_coll.add(role_five);
                        role_six_coll.add(role_six);
                    }

                    try {

                        //System.out.println("This checkpoint was reached just fine!");

                        // REACHED LEVEL 1!
                        // Coin Range (0 - 49) + Users has does not have the role One
                        if (inviter_member == null) {
                            //System.out.println("This member is no longer in the guild! Exiting!");
                            return;

                        } else if (coins <= 49
                                && !inviter_member.getRoles().contains(role_one)) {

                            //System.out.println("GATE ROLE ONE ENTERED");

                            if (inviter_member.getRoles().contains(role_two)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_one_coll, role_two_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_three)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_one_coll, role_three_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_four)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_one_coll, role_four_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_five)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_one_coll, role_five_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_six)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_one_coll, role_six_coll).queue();
                            }


                            // REACHED LEVEL 2!
                            // Coin Range (50 - 119) + Users has does not have the role Two
                        } else if (coins >= 50 && coins <= 119
                                && !inviter_member.getRoles().contains(role_two)) {

                            //System.out.println("GATE ROLE TWO ENTERED");

                            if (inviter_member.getRoles().contains(role_one)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_two_coll, role_one_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_three)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_two_coll, role_three_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_four)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_two_coll, role_four_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_five)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_two_coll, role_five_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_six)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_two_coll, role_six_coll).queue();
                            }


                            // REACHED LEVEL 3!
                            // Coin Range (50 - 119) + Users has does not have the role Three
                        } else if (coins >= 120 && coins <= 219
                                && !inviter_member.getRoles().contains(role_three)) {

                            //System.out.println("GATE ROLE THREE ENTERED");

                            if (inviter_member.getRoles().contains(role_one)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_three_coll, role_one_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_two)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_three_coll, role_two_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_four)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_three_coll, role_four_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_five)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_three_coll, role_five_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_six)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_three_coll, role_six_coll).queue();
                            }


                            // REACHED LEVEL 4!
                            // Coin Range (220 - 359) + Users has does not have the role Four
                        } else if (coins >= 220 && coins <= 359
                                && !inviter_member.getRoles().contains(role_four)) {

                            //System.out.println("GATE ROLE FOUR ENTERED");

                            if (inviter_member.getRoles().contains(role_one)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_four_coll, role_one_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_two)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_four_coll, role_two_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_three)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_four_coll, role_three_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_five)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_four_coll, role_five_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_six)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_four_coll, role_six_coll).queue();
                            }


                            // REACHED LEVEL 5!
                            // Coin Range (360 - 549) + Users has does not have the role Five
                        } else if (coins >= 360 && coins <= 549
                                && !inviter_member.getRoles().contains(role_five)) {

                            //System.out.println("GATE ROLE FIVE ENTERED");

                            if (inviter_member.getRoles().contains(role_one)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_five_coll, role_one_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_two)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_five_coll, role_two_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_three)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_five_coll, role_three_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_four)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_five_coll, role_four_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_six)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_five_coll, role_six_coll).queue();
                            }


                            // REACHED LEVEL 6! (MAX RANK!)
                            // Coin Range (550 >) + Users has does not have the role The Six
                        } else if (coins >= 550
                                && !inviter_member.getRoles().contains(role_six)) {

                            //System.out.println("GATE ROLE SIX ENTERED");

                            if (inviter_member.getRoles().contains(role_one)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_six_coll, role_one_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_two)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_six_coll, role_two_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_three)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_six_coll, role_three_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_four)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_six_coll, role_four_coll).queue();

                            } else if (inviter_member.getRoles().contains(role_five)) {
                                event.getGuild().getController().modifyMemberRoles(inviter_member, role_six_coll, role_five_coll).queue();
                            }


                        } else {
                            //System.out.println("No Role Change Needed!");
                        }

                    } catch (NullPointerException e) {
                        System.out.println("Could not role check for some reason!");
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        // RANK UPDATING SHOULD BE DONE VIA MESSAGES AS WELL!
    }
}
