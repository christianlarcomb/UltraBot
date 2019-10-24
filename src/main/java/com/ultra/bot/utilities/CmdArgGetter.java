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

package com.ultra.bot.utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CmdArgGetter {

    public String[] getArgs(CommandEvent event) {

        // Getting the String after the first character (the prefix)
        String msg = event.getMessage().getContentRaw().substring(2);

        // Splitting the Strings
        String[] args = msg.split(" ");

        return args;
    }

    public String[] getArgs(MessageReceivedEvent event) {

        // Getting the String after the first character (the prefix)
        String msg = event.getMessage().getContentRaw().substring(2);

        // Splitting the Strings
        String[] args = msg.split(" ");

        return args;
    }
}
