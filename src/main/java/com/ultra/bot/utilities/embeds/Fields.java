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

package com.ultra.bot.utilities.embeds;

import com.ultra.bot.utilities.data.MetaData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

import java.awt.*;
import java.util.Date;

public class Fields {

    Date date = new Date();

    Color embedColor = Color.darkGray;

    // 10 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  String fTitle6, String fDes6,
                  String fTitle7, String fDes7,
                  String fTitle8, String fDes8,
                  String fTitle9, String fDes9,
                  String fTitle10, String fDes10,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);
        embed.addField(fTitle6, fDes6, true);
        embed.addField(fTitle7, fDes7, true);
        embed.addField(fTitle8, fDes8, true);
        embed.addField(fTitle9, fDes9, true);
        embed.addField(fTitle10, fDes10, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 9 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  String fTitle6, String fDes6,
                  String fTitle7, String fDes7,
                  String fTitle8, String fDes8,
                  String fTitle9, String fDes9,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);
        embed.addField(fTitle6, fDes6, true);
        embed.addField(fTitle7, fDes7, true);
        embed.addField(fTitle8, fDes8, true);
        embed.addField(fTitle9, fDes9, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 8 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  String fTitle6, String fDes6,
                  String fTitle7, String fDes7,
                  String fTitle8, String fDes8,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);
        embed.addField(fTitle6, fDes6, true);
        embed.addField(fTitle7, fDes7, true);
        embed.addField(fTitle8, fDes8, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 7 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  String fTitle6, String fDes6,
                  String fTitle7, String fDes7,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);
        embed.addField(fTitle6, fDes6, true);
        embed.addField(fTitle7, fDes7, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 6 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  String fTitle6, String fDes6,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);
        embed.addField(fTitle6, fDes6, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 5 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  String fTitle5, String fDes5,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);
        embed.addField(fTitle5, fDes5, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 4 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String fTitle4, String fDes4,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);
        embed.addField(fTitle4, fDes4, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 3 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // Special fields for user lookup
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  String fTitle3, String fDes3,
                  String profileImgURL,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);
        embed.addField(fTitle3, fDes3, true);

        //Showing users profile picture
        embed.setThumbnail(profileImgURL);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 2 Objects
    public Fields(String auth,
                  String fTitle1, String fDes1,
                  String fTitle2, String fDes2,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);
        embed.addField(fTitle2, fDes2, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // 1 Objects
    public Fields(String auth,
                  String fTitle1,
                  String fDes1,
                  Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Embed Information
        embed.setAuthor(auth);

        //Inlined Fields (Look like little sections)
        embed.addField(fTitle1, fDes1, true);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }


    // Building the embed with all of the information
    private EmbedBuilder embed;

    // Getter
    public EmbedBuilder getFields(){
        return embed;
    }

}
