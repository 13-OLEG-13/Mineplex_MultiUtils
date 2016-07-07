package ru.luvas.multiutils.structures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Константин
 */
public class FastScanner {

    private BufferedReader reader;
    private StringTokenizer tokenizer;

    public FastScanner(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
        tokenizer = null;
    }

    public boolean hasNext() {
        try {
            return tokenizer != null && tokenizer.hasMoreTokens() || reader.ready();
        } catch (IOException ex) {
            Logger.getLogger(FastScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return tokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public float nextFloat() {
        return Float.parseFloat(next());
    }

    public byte nextByte() {
        return Byte.parseByte(next());
    }

    public short nextShort() {
        return Short.parseShort(next());
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
