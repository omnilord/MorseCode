package com.privateerconsulting.vibrotest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by etruitte on 11/6/16.
 */
public final class MorseCodes {

    public static final Map<Character, long[]> CODES;

    public static final long
            DOT = 100,
            DASH = 300,
            SEP = 100,
            CHAR = 300,
            WORD = 500,
            STOP = 1000;

    static {
        Map<Character, long[]> map = new HashMap<>();
        map.put('A', new long[] {DOT, SEP, DASH});
        map.put('B', new long[] {DASH, SEP, DOT, SEP, DOT, SEP, DOT});
        map.put('C', new long[] {DASH, SEP, DOT, SEP, DASH, SEP, DOT});
        map.put('D', new long[] {DASH, SEP, DOT});
        map.put('E', new long[] {DOT});
        map.put('F', new long[] {DOT, SEP, DOT, SEP, DASH, SEP, DOT});
        map.put('G', new long[] {DASH, SEP, DASH, SEP, DOT});
        map.put('H', new long[] {DOT, SEP, DOT, SEP, DOT, SEP, DOT});
        map.put('I', new long[] {DOT, SEP, DOT});
        map.put('J', new long[] {DOT, SEP, DASH, SEP, DASH, SEP, DASH});
        map.put('K', new long[] {DASH, SEP, DOT, SEP, DASH});
        map.put('L', new long[] {DOT, SEP, DASH, DOT, DOT});
        map.put('M', new long[] {DASH, SEP, DASH});
        map.put('N', new long[] {DASH, SEP, DOT});
        map.put('O', new long[] {DASH, SEP, DASH, SEP, DASH});
        map.put('P', new long[] {DOT, SEP, DASH, SEP, DASH, SEP, DOT});
        map.put('Q', new long[] {DASH, SEP, DASH, SEP, DOT, SEP, DASH});
        map.put('R', new long[] {DOT, SEP, DASH, SEP, DOT});
        map.put('S', new long[] {DOT, SEP, DOT, SEP, DOT});
        map.put('T', new long[] {DASH});
        map.put('U', new long[] {DOT, SEP, DOT, SEP, DASH});
        map.put('V', new long[] {DOT, SEP, DOT, SEP, DOT, SEP, DASH});
        map.put('W', new long[] {DOT, SEP, DASH, SEP, DASH});
        map.put('X', new long[] {DASH, SEP, DOT, SEP, DOT, SEP, DASH});
        map.put('Y', new long[] {DASH, SEP, DOT, SEP, DASH, SEP, DASH});
        map.put('Z', new long[] {DASH, SEP, DASH, SEP, DOT, SEP, DOT});
        map.put('0', new long[] {DASH, SEP, DASH, SEP, DASH, SEP, DASH, SEP, DASH});
        map.put('1', new long[] {DOT, SEP, DASH, SEP, DASH, SEP, DASH, SEP, DASH});
        map.put('2', new long[] {DOT, SEP, DOT, SEP, DASH, SEP, DASH, SEP, DASH});
        map.put('3', new long[] {DOT, SEP, DOT, SEP, DOT, SEP, DASH, SEP, DASH});
        map.put('4', new long[] {DOT, SEP, DOT, SEP, DOT, SEP, DOT, SEP, DASH});
        map.put('5', new long[] {DOT, SEP, DOT, SEP, DOT, SEP, DOT, SEP, DOT});
        map.put('6', new long[] {DASH, SEP, DOT, SEP, DOT, SEP, DOT, SEP, DOT});
        map.put('7', new long[] {DASH, SEP, DASH, SEP, DOT, SEP, DOT, SEP, DOT});
        map.put('8', new long[] {DASH, SEP, DASH, SEP, DASH, SEP, DOT, SEP, DOT});
        map.put('9', new long[] {DASH, SEP, DASH, SEP, DASH, SEP, DASH, SEP, DOT});
        //map.put(',', new long[] {WORD});
        //map.put(' ', new long[] {WORD});
        //map.put(';', new long[] {WORD});
        //map.put('.', new long[] {STOP});

        CODES = Collections.unmodifiableMap(map);
    }

    public static final long[] encode(CharSequence chs) {
        ArrayList<Long> _pat = new ArrayList<>();
        _pat.add(0L);

        for (int i = 0; i < chs.length(); i++) {
            char ch = Character.toUpperCase(chs.charAt(i));
            if (MorseCodes.CODES.containsKey(ch)) {
                long[] codes = MorseCodes.CODES.get(ch);
                for (int j = 0; j < codes.length; j++) {
                    //_pat.add(SEP);
                    _pat.add(codes[j]);
                }
                _pat.add(CHAR);
            } else if (ch == '.') {
                _pat.add(0L);
                _pat.add(STOP);
            } else {
                _pat.add(0L);
                _pat.add(WORD);
            }
        }

        long[] pat = new long[_pat.size()];
        for (int i = 0; i < pat.length; i++) {
            pat[i] = _pat.get(i);
        }

        return pat;
    }
}
