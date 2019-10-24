package com.ultra.bot.frameworks.verification;

import com.ultra.bot.frameworks.database.JoinLeaveDataSync;
import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.utilities.data.MetaData;
import com.ultra.bot.utilities.embeds.Dunced;
import com.ultra.bot.utilities.embeds.ErrorEmbed;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;

/*
This check is now a fundamental requirement for commands that require role attainment.
 */

public class Checks extends ListenerAdapter {

    public boolean SetupCompCheck(CommandEvent event) {

        String retrieved_role_list = event.getGuild().getRoles().toString();

        if (!(retrieved_role_list.contains("R:*") && retrieved_role_list.contains("R:adm") && retrieved_role_list.contains("R:mod") && retrieved_role_list.contains("R:hel"))) {

            event.reply(new ErrorEmbed("Role Error", "**"+ MetaData.bot_name.toUpperCase() +"** requires your server to have four important roles:\n\n" +
                    "``*``   - for OP's\n" +
                    "``adm`` - for Admins\n " +
                    "``mod`` - for Mods\n " +
                    "``hel`` - for Helpers.", event.getGuild()).getError().build());

            return false;
        } else {
            return true;
        }
    }

    public boolean DunceCheck(CommandEvent event) {


        try {

            MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
            Document user_db_found = (Document) mongoCollection.find(new Document("User", event.getMember().getUser().getId())).first();
            //System.out.println(user_db_found);

            boolean check = user_db_found.getBoolean("Global_Dunce");

            //System.out.println("Dunce check reached");

            if (check) event.reply(new Dunced("Great news!", ":tada: Welcome to dunce town!! :tada: \n\n" +
                    "**SO!**\n" +
                    "You might be wondering right about now what commands CAN you use lol. Well, none. :stuck_out_tongue_closed_eyes:\n\n" +
                    "Better luck next time!", event.getGuild()).getError().build());

            return check;

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

            return false;
        }

    }

    public boolean FeaturesAndCommandsEnabledCheck(CommandEvent event, String FeatureOrCommandToCheck, Boolean isFeature) {

        if (isFeature) {

            // If we're checking a feature
            try {

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document features_db_found = (Document) mongoCollection.find(new Document("Features", FeatureOrCommandToCheck)).first();

                boolean check = features_db_found.getBoolean("Enabled");

                //
                if (!check)
                    event.reply(":x: **Whoops.** This feature is not enabled!");

                return check;

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } else {

            // If we're checking a command
            try {

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document command_db_found = (Document) mongoCollection.find(new Document("Commands", FeatureOrCommandToCheck)).first();

                boolean check = command_db_found.getBoolean("Enabled");

                //
                if (!check)
                    event.reply(":x: **Whoops.** This command is not enabled!");

                return check;

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();

                new JoinLeaveDataSync().CommandDataManagement(event);
            }

        }

        return false;
    }

    public boolean FeaturesAndCommandsEnabledCheck(GuildMemberJoinEvent event, String FeatureOrCommandToCheck, Boolean isFeature) {

        if (isFeature) {

            try {

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document features_db_found = (Document) mongoCollection.find(new Document("Features", FeatureOrCommandToCheck)).first();

                boolean check;
                try{
                    check = features_db_found.getBoolean("Enabled");
                } catch (NullPointerException e){
                    check = false;
                }

                //
                if (!check) {
                    //event.reply(":x: **Whoops.** This feature is not enabled!");
                }

                System.out.println("It's returning: "+check);

                return check;

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } else {

            try {

                MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
                Document command_db_found = (Document) mongoCollection.find(new Document("Commands", FeatureOrCommandToCheck)).first();

                boolean check;
                try{
                    check = command_db_found.getBoolean("Enabled");
                } catch (NullPointerException e){
                    check = false;
                }

                //
                if (!check)
                    //event.reply(":x: **Whoops.** This command is not enabled!");

                return check;

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public boolean SetAvailability(CommandEvent commandEvent, boolean isIt, String command_entered){

        if(!isIt){
            commandEvent.reply("Woah woah slow down there! The **"+command_entered+"** command is not available at the moment!");
            return false;
        } else {
            return true;
        }

    }
}
