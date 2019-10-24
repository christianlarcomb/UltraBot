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

public class Dunced {

    // Getting the footer URL from a Hashmap
    private String dunce_img = "https://i.ya-webdesign.com/images/dunce-hat-png-1.png";

    public Dunced(String errName, String des, Guild guild){

        //Created an Embed
        EmbedBuilder embed = new EmbedBuilder();

        //Information
        embed.setAuthor(errName.toUpperCase());
        embed.setDescription(des);
        embed.setThumbnail(dunce_img);

        //Footer
        embed.setFooter(MetaData.bot_name + " ©", guild.getSelfMember().getUser().getAvatarUrl());

        //Color
        embed.setColor(Color.WHITE);

        this.embed = embed;
    }

    private EmbedBuilder embed;

    public EmbedBuilder getError(){ return embed; }

}