package com.ultra.bot.frameworks.database;

import com.ultra.bot.utilities.data.MetaData;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;

public class JoinLeaveDataSync extends ListenerAdapter {


    /**************** EVENTS ****************/

    /** BOT JOIN **/
    // Inserting Guild Doc if not found -> OR -> Fixing pre-existing document
    public void onGuildJoin(GuildJoinEvent event) {

        if (event.getGuild().getSelfMember().getUser().isBot()) {

            GuildDataManagement(event);

            UserDataManagement(event);

            CommandDataManagement(event);

            FeatureDataManagement(event);

        }
    }

    /** GUILDER MEMBER JOIN **/
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        UserDataManagement(event);
        MemberCountManagement(event);
    }

    /** GUILDER MEMBER LEAVE **/
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        MemberCountManagement(event);
    }


    /********** DOC MANAGEMENT METHODS **********/

    @SuppressWarnings("all")
    public void CommandDataManagement(GuildJoinEvent event) {

        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());


        /*************** COMMANDS DATA DOCUMENTS ***************/

        ArrayList<String> commands = new ArrayList<>();
        {
            commands.add("Version");
            commands.add("About");
            commands.add("Cmds");
            commands.add("Help");

            commands.add("Coins");
            commands.add("AskReddit");
            commands.add("Cancer");
            commands.add("Cats");
            commands.add("CoinFlip");
            commands.add("Dogs");
            commands.add("Magic8Ball");
            commands.add("Meme");
            commands.add("RollDice");
            commands.add("SuckMy");
            commands.add("GuessThat");
            commands.add("RocketLeague");
            commands.add("Dunce");
            commands.add("Undunce");
            commands.add("EmoteID");
            commands.add("Guild");
            commands.add("Mongo");
            commands.add("Purge");
            commands.add("RoleID");
            commands.add("Suggest");
            commands.add("Users");
        }

        // Looping through all commands
        for (String command : commands) {

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document cmd_doc_found = (Document) mongoCollection.find(new Document("Commands", command)).first();

            /* CHECKING IF THE USER IS NULL IN THE DATABASE*/
            if (cmd_doc_found == null) {

                // DOC: Created to be appended and inserted to DB & Update Cache
                Document unfound_cmd_doc = new Document("Commands", command);

                if(command.equalsIgnoreCase("help") || command.equalsIgnoreCase("cmds") || command.equalsIgnoreCase("about") || command.equalsIgnoreCase("version")){
                    unfound_cmd_doc.append("Enabled", true);
                } else {
                    unfound_cmd_doc.append("Enabled", false);
                }

                // INSERTING: Doc to DB
                mongoCollection.insertOne(unfound_cmd_doc);

                System.out.println("Command not found in ");

            }

        }

    }

    public void CommandDataManagement(CommandEvent event) {

        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());


        /*************** COMMANDS DATA DOCUMENTS ***************/

        ArrayList<String> commands = new ArrayList<>();
        {
            commands.add("Coins");
            //commands.add("Currency");
            commands.add("Shop");
            commands.add("AskReddit");
            commands.add("Cancer");
            commands.add("Cats");
            commands.add("CoinFlip");
            commands.add("Dogs");
            commands.add("Magic8Ball");
            commands.add("Meme");
            commands.add("RollDice");
            commands.add("SuckMy");
            commands.add("GuessThat");
            commands.add("RocketLeague");
            commands.add("About");
            commands.add("Cmds");
            commands.add("Help");
            commands.add("Dunce");
            commands.add("Undunce");
            commands.add("EmoteID");
            commands.add("Guild");
            commands.add("Mongo");
            commands.add("Purge");
            commands.add("RoleID");
            commands.add("Version");
            commands.add("Suggest");
            commands.add("Users");
        }

        // Looping through all commands
        for (String command : commands) {

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document cmd_doc_found = (Document) mongoCollection.find(new Document("Commands", command)).first();

            /* CHECKING IF THE USER IS NULL IN THE DATABASE*/
            if (cmd_doc_found == null) {

                // DOC: Created to be appended and inserted to DB & Update Cache
                Document unfound_cmd_doc = new Document("Commands", command);

                unfound_cmd_doc.append("Enabled", false);

                // INSERTING: Doc to DB
                mongoCollection.insertOne(unfound_cmd_doc);

                System.out.println("Command not found in ");

            }

        }

    }

    private void UserDataManagement(GuildMemberJoinEvent event) {

        // CHECK: Users is not a bot
        if (!event.getUser().isBot()) {


            // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

            // CACHED_MONGO >> FINDING: The document Config with the identifier Guild
            Document user_doc = (Document) mongoCollection.find(new Document("User", event.getUser().getId())).first();

            /** CHECKING USER DOCS **/

            if (user_doc == null) {

                Document unfound_user_doc = new Document("User", event.getUser().getId());

                unfound_user_doc.append("Discord_Tag", event.getUser().getName() + "#" + event.getUser().getDiscriminator());
                unfound_user_doc.append("Avatar_URL", event.getUser().getAvatarUrl());
                unfound_user_doc.append("Acc_Creation_Date", event.getUser().getCreationTime().toLocalDate().toString());
                unfound_user_doc.append("Global_Ban", false);
                unfound_user_doc.append("Global_Dunce", false);
                unfound_user_doc.append("Coins", 0);
                unfound_user_doc.append("Coins_From_Invites", 0);
                unfound_user_doc.append("Coins_From_Messages", 0);
                unfound_user_doc.append("Coins_Given", 0);
                unfound_user_doc.append("Coins_Received", 0);
                unfound_user_doc.append("Coins_Roll_Lost", 0);
                unfound_user_doc.append("Coins_Roll_Earned", 0);
                unfound_user_doc.append("Coins_Removed", 0);
                unfound_user_doc.append("Invite_Count", 0);

                // INSERT: User Doc to DB
                mongoCollection.insertOne(unfound_user_doc);

                //System.out.println("USER WAS NOT FOUND ~ INSERTED ALL DATA TO DB");

            } else {

                Document found_user_doc = new Document("User", event.getUser().getId());
                boolean user_updated = false;

                if (!user_doc.containsKey("Discord_Tag")) {
                    found_user_doc.append("Discord_Tag", event.getUser().getName() + "#" + event.getUser().getDiscriminator());
                    user_updated = true;
                }
                if (!user_doc.containsKey("Avatar_URL")) {
                    found_user_doc.append("Avatar_URL", event.getUser().getAvatarUrl());
                    user_updated = true;
                }
                if (!user_doc.containsKey("Acc_Creation_Date")) {
                    found_user_doc.append("Acc_Creation_Date", event.getUser().getCreationTime().toLocalDate().toString());
                    user_updated = true;
                }
                if (!user_doc.containsKey("Global_Ban")) {
                    found_user_doc.append("Global_Ban", false);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Global_Dunce")) {
                    found_user_doc.append("Global_Dunce", false);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins")) {
                    found_user_doc.append("Coins", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_From_Invites")) {
                    found_user_doc.append("Coins_From_Invites", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_From_Messages")) {
                    found_user_doc.append("Coins_From_Messages", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_Given")) {
                    found_user_doc.append("Coins_Given", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_Received")) {
                    found_user_doc.append("Coins_Received", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_Roll_Lost")) {
                    found_user_doc.append("Coins_Roll_Lost", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_Roll_Earned")) {
                    found_user_doc.append("Coins_Roll_Earned", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Coins_Removed")) {
                    found_user_doc.append("Coins_Removed", 0);
                    user_updated = true;
                }
                if (!user_doc.containsKey("Invite_Count")) {
                    found_user_doc.append("Invite_Count", 0);
                    user_updated = true;
                }

                if (user_updated) {
                    Document mongo_user_found = (Document) mongoCollection.find(new Document("User", event.getUser().getId())).first();
                    Bson user_update_operation = new Document("$set", found_user_doc);
                    mongoCollection.updateOne(mongo_user_found, user_update_operation);
                    //System.out.println("USER WAS FOUND ~ INSERTED BITS OF DATA");
                } else {
                    //System.out.println("USER WAS FOUND ~ USER HAS ALL DATA ELEMENTS!");
                }
            }

        }
    }
    private void UserDataManagement(GuildJoinEvent event) {

        /*************** USER DATA DOCUMENTS ***************/

        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

        for (int i = 0; i < event.getGuild().getMembers().size(); i++) {

            User user = event.getGuild().getMembers().get(i).getUser();
            Guild guild = event.getGuild();

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document user_doc_found = (Document) mongoCollection.find(new Document("User", user.getId())).first();

            /* CHECKING IF THE USER IS NULL IN THE DATABASE*/
            if (user_doc_found == null) {

                // DOC: Created to be appended and inserted to DB & Update Cache
                Document unfound_user_doc = new Document("User", event.getGuild().getMembers().get(i).getUser().getId());

                unfound_user_doc.append("Discord_Tag", user.getName() + "#" + user.getDiscriminator());
                unfound_user_doc.append("Avatar_URL", user.getAvatarUrl());
                unfound_user_doc.append("Acc_Creation_Date", user.getCreationTime().toLocalDate().toString());
                unfound_user_doc.append("Global_Ban", false);
                unfound_user_doc.append("Global_Dunce", false);
                unfound_user_doc.append("Coins", 0);
                unfound_user_doc.append("Coins_From_Invites", 0);
                unfound_user_doc.append("Coins_From_Messages", 0);
                unfound_user_doc.append("Coins_Given", 0);
                unfound_user_doc.append("Coins_Received", 0);
                unfound_user_doc.append("Coins_Roll_Lost", 0);
                unfound_user_doc.append("Coins_Roll_Earned", 0);
                unfound_user_doc.append("Coins_Removed", 0);
                unfound_user_doc.append("Invite_Count", 0);

                // INSERTING: Doc to DB
                mongoCollection.insertOne(unfound_user_doc);


                System.out.println("USER WAS NOT FOUND IN CACHE (inferring) ~ INSERTED ALL DATA TO DB & CACHE");

            } else {

                Document mongo_user_found = MongoConnect.getMongoDatabase().getCollection(guild.getId()).find(new Document("User", user.getId())).first();
                Document found_user_doc = new Document("User", event.getGuild().getMembers().get(i).getUser().getId());

                boolean updated = false;

                if (!user_doc_found.containsKey("Discord_Tag")) {
                    found_user_doc.append("Discord_Tag", user.getName() + "#" + user.getDiscriminator());
                    updated = true;
                }
                if (!user_doc_found.containsKey("Avatar_URL")) {
                    found_user_doc.append("Avatar_URL", user.getAvatarUrl());
                    updated = true;
                }
                if (!user_doc_found.containsKey("Acc_Creation_Date")) {
                    found_user_doc.append("Acc_Creation_Date", user.getCreationTime().toLocalDate().toString());
                    updated = true;
                }
                if (!user_doc_found.containsKey("Global_Ban")) {
                    found_user_doc.append("Global_Ban", false);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Global_Dunce")) {
                    found_user_doc.append("Global_Dunce", false);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins")) {
                    found_user_doc.append("Coins", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_From_Invites")) {
                    found_user_doc.append("Coins_From_Invites", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_From_Messages")) {
                    found_user_doc.append("Coins_From_Messages", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_Given")) {
                    found_user_doc.append("Coins_Given", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_Received")) {
                    found_user_doc.append("Coins_Received", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_Roll_Lost")) {
                    found_user_doc.append("Coins_Roll_Lost", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_Roll_Earned")) {
                    found_user_doc.append("Coins_Roll_Earned", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Coins_Removed")) {
                    found_user_doc.append("Coins_Removed", 0);
                    updated = true;
                }
                if (!user_doc_found.containsKey("Invite_Count")) {
                    found_user_doc.append("Invite_Count", 0);
                    updated = true;
                }

                if (updated) {
                    // INSERT: Doc to DB
                    Bson user_update_operation = new Document("$set", found_user_doc);
                    mongoCollection.updateOne(mongo_user_found, user_update_operation);

                    //System.out.println("USER WAS FOUND ~ INSERTED BITS OF DATA");
                } else {
                    //System.out.println("USER WAS FOUND ~ USER HAS ALL DATA ELEMENTS!");
                }
            }
        }

    }

    private void MemberCountManagement(GuildMemberLeaveEvent event) {

        // CHECK: Users is not a bot
        if (!event.getUser().isBot()) {

            // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

            /*************** GUILD DATA CHECK & DOC **************/

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document guildConfigDoc = (Document) mongoCollection.find(new Document("Config", "Guild")).first();
            Document found_guild_doc = new Document("Config", "Guild");

            // MEMBER COUNT CHANGES ON EACH JOIN / LEAVE
            found_guild_doc.append("Member_Count", event.getGuild().getMembers().size());
            //System.out.println("Member Count Appended");

            Bson update_operation = new Document("$set", found_guild_doc);
            mongoCollection.updateOne(guildConfigDoc, update_operation);

        }

    }
    private void MemberCountManagement(GuildMemberJoinEvent event) {

        // CHECK: Users is not a bot
        if (!event.getUser().isBot()) {

            // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

            /*************** GUILD DATA CHECK & DOC **************/

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document guildConfigDoc = (Document) mongoCollection.find(new Document("Config", "Guild")).first();
            Document found_guild_doc = new Document("Config", "Guild");

            // MEMBER COUNT CHANGES ON EACH JOIN / LEAVE
            found_guild_doc.append("Member_Count", event.getGuild().getMembers().size());
            //System.out.println("Member Count Appended");

            Bson update_operation = new Document("$set", found_guild_doc);
            mongoCollection.updateOne(guildConfigDoc, update_operation);

        }

    }

    private void GuildDataManagement(GuildJoinEvent event) {
        // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());


        /*************** GUILD DATA DOCUMENT **************/

        // MONGO >> FINDING: The document Config with the identifier Guild
        Document guildConfigDoc = (Document) mongoCollection.find(new Document("Config", "Guild")).first();

        if (guildConfigDoc == null) {

            // MONGO >> CREATING: A collection with the Guild ID as a label
            MongoConnect.getMongoDatabase().createCollection(event.getGuild().getId());

            // GETTING: The date
            Date date = new Date();

            /********* GUILD DATA DOCUMENT *********/
            Document unfound_guild_doc = new Document("Config", "Guild");

            unfound_guild_doc.append("Guild_Name", event.getGuild().getName());
            unfound_guild_doc.append("Guild_ID", event.getGuild().getId());
            unfound_guild_doc.append("Guild_Region", event.getGuild().getRegionRaw());
            unfound_guild_doc.append("Date_Joined", date.toString());
            unfound_guild_doc.append("Icon_URL", event.getGuild().getIconUrl());
            unfound_guild_doc.append("Member_Count", event.getGuild().getMembers().size());

            // INSERTING: Document to DB
            mongoCollection.insertOne(unfound_guild_doc);
            //System.out.println("GUILD NOT FOUND ~ INSERTED ALL DATA TO DB");

        } else {

            // GETTING: The date
            Date date = new Date();

            /********* GUILD DATA DOCUMENT *********/
            Document found_guild_doc = new Document("Config", "Guild");
            boolean updated = false;

            if (!guildConfigDoc.containsKey("Guild_Name")) {
                found_guild_doc.append("Guild_Name", event.getGuild().getName());
                updated = true;
                //System.out.println("Guild Name Appended");
            }

            if (!guildConfigDoc.containsKey("Guild_ID")) {
                found_guild_doc.append("Guild_ID", event.getGuild().getId());
                updated = true;
                //System.out.println("Guild ID Appended");
            }

            if (!guildConfigDoc.containsKey("Guild_Region")) {
                found_guild_doc.append("Guild_Region", event.getGuild().getRegionRaw());
                updated = true;
                //System.out.println("Guild Region Appended");
            }

            if (!guildConfigDoc.containsKey("Date_Joined")) {
                found_guild_doc.append("Date_Joined", date.toString());
                updated = true;
                //System.out.println("Date Joined Appended");
            }

            if (!guildConfigDoc.containsKey("Icon_URL")) {
                found_guild_doc.append("Icon_URL", event.getGuild().getIconUrl());
                updated = true;
                //System.out.println("Icon URL Appended");
            }

            if (!guildConfigDoc.containsKey("Member_Count")) {
                found_guild_doc.append("Member_Count", event.getGuild().getMembers().size());
                updated = true;
                //System.out.println("Member Count Appended");
            }

            // INSERTING: Document to DB
            if (updated) {
                Bson update_operation = new Document("$set", found_guild_doc);
                mongoCollection.updateOne(guildConfigDoc, update_operation);
                //System.out.println("GUILD DOC FOUND ~ INSERTED BITS OF DATA TO DB");
            } else {
                //System.out.println("GUILD DOC FOUND ~ GUILD HAS ALL DATA ELEMENTS!");
            }
        }
    }

    private void FeatureDataManagement(GuildJoinEvent event) {

        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

        /*************** FEATURES DATA DOCUMENTS ***************/

        ArrayList<String> features = new ArrayList<>();
        {
            // Disabling Currency and MemberCountChannel as of now.
            features.add("Currency");
            features.add("AutoCommand");
            features.add("WelcomeMessage");
            features.add("JoinRoles");
        }

        // Looping through all commands
        for (String feature : features) {

            // MONGO >> FINDING: The document Config with the identifier Guild
            Document cmd_doc_found = (Document) mongoCollection.find(new Document("Features", feature)).first();

            //System.out.println("PRE ADDING FEATURE DATA");

            /* CHECKING IF THE FEATURE IS NULL IN THE DATABASE */
            if (cmd_doc_found == null) {

                //System.out.println("POST ADDING FEATURE DATA");

                // DOC: Created to be appended and inserted to DB & Update Cache
                Document unfound_feature_doc = new Document("Features", feature);

                unfound_feature_doc.append("Enabled", false);

                if (feature.equalsIgnoreCase("AutoCommand")) {
                    unfound_feature_doc.append("Command_1", "");
                    unfound_feature_doc.append("Command_1_Seconds", 0);
                    unfound_feature_doc.append("Command_2", "");
                    unfound_feature_doc.append("Command_2_Time_Amt", 0);
                    unfound_feature_doc.append("Command_3", "");
                    unfound_feature_doc.append("Command_3_Time_Amt", 0);
                    unfound_feature_doc.append("Command_4", "");
                    unfound_feature_doc.append("Command_4_Time_Amt", 0);
                    unfound_feature_doc.append("Command_5", "");
                    unfound_feature_doc.append("Command_5_Time_Amt", 0);

                } else if (feature.equalsIgnoreCase("WelcomeMessage")) {

                    unfound_feature_doc.append("TextChannel", "");

                    for (int i = 1; i <= MetaData.Welcome_Messages_Count; i++) {

                        // Default Welcome Message
                        if(i == 1){
                            unfound_feature_doc.append("Message_"+i+"_Text", "Welcome, [event_user_mention]!\n" + "You are our [guild_member_count]th member!");
                            unfound_feature_doc.append("Message_"+i+"_Thumbnail", "[event_user_thumbnail]");
                            unfound_feature_doc.append("Message_"+i+"_Image", "");

                        // Configurable Extras
                        } else {
                            unfound_feature_doc.append("Message_"+i+"_Text", "");
                            unfound_feature_doc.append("Message_"+i+"_Thumbnail", "");
                            unfound_feature_doc.append("Message_"+i+"_Image", "");
                        }

                    }

                } else if (feature.equalsIgnoreCase("Currency")) {
                    unfound_feature_doc.append("Tradable", false);
                    unfound_feature_doc.append("Currency_Name", "Coins");
                    unfound_feature_doc.append("Amt_Earned_Inviting", 1);
                    unfound_feature_doc.append("Amt_Earned_VoiceChannel", 1);
                    unfound_feature_doc.append("Amt_Earned_TextChannel", 1);

                } else if (feature.equalsIgnoreCase("JoinRoles")) {

                    for (int i = 1; i <= MetaData.Join_Roles_Count; i++)
                        unfound_feature_doc.append("Role_"+i, "");

                }

                // INSERTING: Doc to DB
                mongoCollection.insertOne(unfound_feature_doc);

            }
        }
    }

}

