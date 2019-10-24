package com.ultra.bot.frameworks.database;

import com.ultra.bot.utilities.data.MetaData;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MongoConnect extends ListenerAdapter {

    private static String password = new MetaData().credentials[1];
    private static String uri = "mongodb+srv://chrislarcomb:"+password+"@botdata-ofqqb.mongodb.net/test?retryWrites=true&w=majority";
    private static MongoClientURI clientURI = new MongoClientURI(uri);
    private static MongoClient mongoClient = new MongoClient(clientURI);

    /***** ESTABLISHING CONNECTION *****/

    public static MongoClient getMongoClient(){ return mongoClient; }

    public static MongoDatabase getMongoDatabase(){ return getMongoClient().getDatabase("BotData"); }
}
