package ru.luvas.multiutils;

/**
 *
 * @author Константин
 */
public class Storager {
    
    public static boolean has(String string, int id) {
        int n = id / 3, o = id % 3;
        try {
            int value = string.charAt(n) - '0';
            return (value & 1 << o) != 0;
        }catch(StringIndexOutOfBoundsException ex) {
            return false;
        }
    }
    
    public static String update(String string, int id, boolean value) {
        int n = id / 3, o = id % 3;
        StringBuilder sb = new StringBuilder(string);
        while(sb.length() <= n)
            sb.append(0);
        int v = sb.charAt(n) - '0';
        if(value) v |= 1 << o;
        else if((v & 1 << o) != 0) v ^= 1 << o;
        sb.setCharAt(n, (char) (v + '0'));
        return sb.toString();
    }
    
    public static String getDefault(int maxN) {
        ++maxN; maxN /= 3;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < maxN; ++i) sb.append(0);
        return sb.toString();
    }
    
    public static int countTotal(String s) {
        int total = 0;
        for(char c : s.toCharArray()) {
            int i = c - '0';
            i -= ((i >>> 1) & 0x55555555);
            i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
            total += (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
        }
        return total;
    }
    
}
