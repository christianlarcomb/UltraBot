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

public class Basic {

    // Getting the footer URL from a Hashmap
    private String footerImg = "https://cdn.discordapp.com/avatars/578277176290443264/de682214077d6e72e40dc5cb806d5033.png?size=256";

    Color embedColor = Color.darkGray;

    // Embed with Title, Description, Thumbnail, and 1 Image
    public Basic(String title, String des, String thumbnailImgURL, String largeImgURL, Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Information
        embed.setAuthor(title.toUpperCase());
        embed.setDescription(des);
        embed.setThumbnail(thumbnailImgURL);
        embed.setImage(largeImgURL);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // Embed with Title, Description, Thumbnail, and No Image
    public Basic(String title, String des, String thumbnailImgURL, Guild guild){

        Date date = new Date();

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Information
        embed.setAuthor(title.toUpperCase());
        embed.setDescription(des);
        embed.setThumbnail(thumbnailImgURL);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    // Embed with Title, Description, No Thumbnail, and No Image
    public Basic(String title, Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Information
        embed.setAuthor(title.toUpperCase());
        //embed.setThumbnail(iconUrl);

        //Footer
        embed.setFooter(MetaData.bot_name + " © • ",guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(embedColor);

        this.embed = embed;
    }

    private EmbedBuilder embed;

    public EmbedBuilder getBasic(){
        return embed;
    }

}