package com.ultra.bot.frameworks.database;

import com.mongodb.client.MongoCollection;

import net.dv8tion.jda.core.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.bson.Document;
import org.bson.conversions.Bson;

public class UpdateGuildSync extends ListenerAdapter {

    public void onGuildUpdateName(GuildUpdateNameEvent event) {

        System.out.println("Guild Name Updated!");

        // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

        // MONGO >> FINDING: The document Config with the identifier Guild
        Document guildConfigDoc = (Document) mongoCollection.find(new Document("Config", "Guild")).first();

        /********* GUILD DATA DOCUMENT *********/
        Document found_guild_doc = new Document("Config", "Guild");

        found_guild_doc.append("Guild_Name", event.getGuild().getName());
        //System.out.println("Guild Name Appended");

        Bson update_operation = new Document("$set", found_guild_doc);
        mongoCollection.updateOne(guildConfigDoc, update_operation);
        //System.out.println("GUILD DOC FOUND ~ INSERTED BITS OF DATA TO DB");

    }

    public void onGuildUpdateIcon(GuildUpdateIconEvent event) {

        // MONGO >> GETTING: The mongo collection just created. (Is this creating it?)
        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());

        // MONGO >> FINDING: The document Config with the identifier Guild
        Document guildConfigDoc = (Document) mongoCollection.find(new Document("Config", "Guild")).first();

        Document found_guild_doc = new Document("Config", "Guild");

        found_guild_doc.append("Icon_URL", event.getGuild().getIconUrl());

        Bson update_operation = new Document("$set", found_guild_doc);
        mongoCollection.updateOne(guildConfigDoc, update_operation);

    }

}
