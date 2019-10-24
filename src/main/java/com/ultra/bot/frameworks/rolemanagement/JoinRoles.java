package com.ultra.bot.frameworks.rolemanagement;

import com.ultra.bot.frameworks.database.MongoConnect;
import com.ultra.bot.frameworks.verification.Checks;
import com.ultra.bot.utilities.data.MetaData;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;

public class JoinRoles extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        System.out.println("GATE 1");

        /** Feature Enabled Check **/
        if (!new Checks().FeaturesAndCommandsEnabledCheck(event, "JoinRoles", true))
            return;

        System.out.println("GATE 2");

        MongoCollection mongoCollection = MongoConnect.getMongoDatabase().getCollection(event.getGuild().getId());
        Document rank_doc_found = (Document) mongoCollection.find(new Document("Features", "JoinRoles")).first();

        Collection<Role> role_collection = new ArrayList<>();

        //System.out.println(rank_doc_found);

        for (int i = 1; i <= MetaData.Join_Roles_Count; i++) {
            // Trying to give first role

            //System.out.println("This is firing!");

            try {
                // Getting the Role from DB

                String role_id_from_mongo = rank_doc_found.getString("Role_"+i)
                        .replace("<", "")
                        .replace("@&", "")
                        .replace(">" , "");

                //System.out.println("Role ID to be given ["+role_id_from_mongo+"]");

                Role role = event.getGuild().getRoleById(role_id_from_mongo);

                // Giving the Role
                role_collection.add(role);


            } catch (Exception e) {
                //event.getGuild().getDefaultChannel().sendMessage
                       // (":x: **Whoops!** There has been a Hierarchy Error! Please move " + MetaData.bot_name + "'s Role above the Roles to be given in order to work!");
                //e.printStackTrace();
            }

        }

        // Giving the list of roles to the user
        try {
            event.getGuild().getController().addRolesToMember(event.getMember(), role_collection).complete();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Might be no roles to give?");
        }

    }

}
