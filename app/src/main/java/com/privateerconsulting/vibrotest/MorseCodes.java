package com.privateerconsulting.vibrotest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MorseCodes {

    public static final Map<Character, MorseCharacter> CODES;
    public static final long
            DOT = 200,
            DASH = DOT * 4,
            SEP = DOT / 2,
            CHAR = SEP * 4,
            WORD = SEP * 8,
            STOP = WORD + CHAR;


    static {
        Map<Character, MorseCharacter> map = new HashMap<>();

        /*
          Use ONLY DOT and DASH to define characters, SEP will be injected later
        */

        map.put('A', new MorseCharacter('A', new long[] {DOT, DASH}));
        map.put('B', new MorseCharacter('B', new long[] {DASH, DOT, DOT, DOT}));
        map.put('C', new MorseCharacter('C', new long[] {DASH, DOT, DASH, DOT}));
        map.put('D', new MorseCharacter('D', new long[] {DASH, DOT}));
        map.put('E', new MorseCharacter('E', new long[] {DOT}));
        map.put('F', new MorseCharacter('F', new long[] {DOT, DOT, DASH, DOT}));
        map.put('G', new MorseCharacter('G', new long[] {DASH, DASH, DOT}));
        map.put('H', new MorseCharacter('H', new long[] {DOT, DOT, DOT, DOT}));
        map.put('I', new MorseCharacter('I', new long[] {DOT, DOT}));
        map.put('J', new MorseCharacter('J', new long[] {DOT, DASH, DASH, DASH}));
        map.put('K', new MorseCharacter('K', new long[] {DASH, DOT, DASH}));
        map.put('L', new MorseCharacter('L', new long[] {DOT, DASH, DOT, DOT}));
        map.put('M', new MorseCharacter('M', new long[] {DASH, DASH}));
        map.put('N', new MorseCharacter('N', new long[] {DASH, DOT}));
        map.put('O', new MorseCharacter('O', new long[] {DASH, DASH, DASH}));
        map.put('P', new MorseCharacter('P', new long[] {DOT, DASH, DASH, DOT}));
        map.put('Q', new MorseCharacter('Q', new long[] {DASH, DASH, DOT, DASH}));
        map.put('R', new MorseCharacter('R', new long[] {DOT, DASH, DOT}));
        map.put('S', new MorseCharacter('S', new long[] {DOT, DOT, DOT}));
        map.put('T', new MorseCharacter('T', new long[] {DASH}));
        map.put('U', new MorseCharacter('U', new long[] {DOT, DOT, DASH}));
        map.put('V', new MorseCharacter('V', new long[] {DOT, DOT, DOT, DASH}));
        map.put('W', new MorseCharacter('W', new long[] {DOT, DASH, DASH}));
        map.put('X', new MorseCharacter('X', new long[] {DASH, DOT, DOT, DASH}));
        map.put('Y', new MorseCharacter('Y', new long[] {DASH, DOT, DASH, DASH}));
        map.put('Z', new MorseCharacter('Z', new long[] {DASH, DASH, DOT, DOT}));
        map.put('0', new MorseCharacter('0', new long[] {DASH, DASH, DASH, DASH, DASH}));
        map.put('1', new MorseCharacter('1', new long[] {DOT, DASH, DASH, DASH, DASH}));
        map.put('2', new MorseCharacter('2', new long[] {DOT, DOT, DASH, DASH, DASH}));
        map.put('3', new MorseCharacter('3', new long[] {DOT, DOT, DOT, DASH, DASH}));
        map.put('4', new MorseCharacter('4', new long[] {DOT, DOT, DOT, DOT, DASH}));
        map.put('5', new MorseCharacter('5', new long[] {DOT, DOT, DOT, DOT, DOT}));
        map.put('6', new MorseCharacter('6', new long[] {DASH, DOT, DOT, DOT, DOT}));
        map.put('7', new MorseCharacter('7', new long[] {DASH, DASH, DOT, DOT, DOT}));
        map.put('8', new MorseCharacter('8', new long[] {DASH, DASH, DASH, DOT, DOT}));
        map.put('9', new MorseCharacter('9', new long[] {DASH, DASH, DASH, DASH, DOT}));

        CODES = Collections.unmodifiableMap(map);
    }


    public static ArrayList<MorseCharacter> encode(CharSequence chs) {
        ArrayList<MorseCharacter> queue = new ArrayList<>();
        long preInterval = 0L;

        for (int i = 0; i < chs.length(); i++) {
            char ch = Character.toUpperCase(chs.charAt(i));

            try {
                if (MorseCodes.CODES.containsKey(ch)) {
                    queue.add(new MorseCharacter(MorseCodes.CODES.get(ch), preInterval));
                    preInterval = MorseCodes.CHAR;
                } else if (ch == '.') {
                    preInterval = MorseCodes.STOP;
                } else {
                    preInterval = MorseCodes.WORD;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return queue;
    }
}
