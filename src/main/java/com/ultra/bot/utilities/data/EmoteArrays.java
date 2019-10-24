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

package com.ultra.bot.utilities.data;

import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;

public class EmoteArrays {

    private ArrayList<String> serverPreferences = new ArrayList<>();
    {
        serverPreferences.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_a:"));                 // 'P'  Notifications
        serverPreferences.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_b:"));                 // 'Q'  DM Friendly
        serverPreferences.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_c:"));                 // 'R'  Comp Player
        serverPreferences.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_d:"));                 // 'S'  Non-Comp Player
    }

    // Array List of University Emoji's
    private ArrayList<String> rocketRankSelection = new ArrayList<>();
    {
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_e:"));                 // 'A'  Grand Champ
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_f:"));                 // 'B'  Champ
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_g:"));                 // 'C'  Diamond
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_h:"));                 // 'D'  Platinum
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_i:"));                 // 'E'  Gold
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_j:"));                 // 'F'  Silver
        rocketRankSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_k:"));                 // 'G'  Bronze
    }

    private ArrayList<String> rocketPrefSelection = new ArrayList<>();
    {
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_l:"));                 // 'H'  Solos
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_m:"));                 // 'I'  Duos
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_n:"));                 // 'J'  Threes
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_o:"));                 // 'K'  4s
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_p:"));                 // 'L'  Dropshot
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_q:"));                 // 'M'  Hoops
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_r:"));                 // 'N'  Hockey
        rocketPrefSelection.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_s:"));                 // 'O'  Rumble
    }

    private ArrayList<String> options = new ArrayList<>();
    {
        options.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_y:"));
        options.add(EmojiParser.parseToUnicode(":regional_indicator_symbol_n:"));
    }

    private ArrayList<String> vote = new ArrayList<>();
    {
        vote.add(EmojiParser.parseToUnicode(":white_check_mark:"));
        vote.add(EmojiParser.parseToUnicode(":x:"));
    }

    private ArrayList<String> numbers = new ArrayList<>();
    {
        numbers.add(EmojiParser.parseToUnicode(":one:"));
        numbers.add(EmojiParser.parseToUnicode(":two:"));
        numbers.add(EmojiParser.parseToUnicode(":three:"));
        numbers.add(EmojiParser.parseToUnicode(":four:"));
        numbers.add(EmojiParser.parseToUnicode(":five:"));
    }

    public ArrayList<String> getRocketRankSelection() {
        return rocketRankSelection;
    }

    public ArrayList<String> getRocketPrefSelection() { return rocketPrefSelection; }

    public ArrayList<String> getServerPreferences() {
        return serverPreferences;
    }

    public ArrayList<String> getOptions() { return options; }

    public ArrayList<String> getVote() { return vote; }

    public ArrayList<String> getNumbers() { return numbers; }
}
