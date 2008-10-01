package com.blueskyminds.homebyfive.framework.framework.tools.streams;


import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Removes consecuative whitespace characters from an output stream
 * <p/>
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class ASCIIWhitespaceFilter extends FilterOutputStream {

    private static final int CR = '\n';
    private static final int LF = '\r';
    private static final int SPACE = ' ';
    private static final byte CR_BYTE = '\n';
    private static final byte LF_BYTE = '\r';
    private static final byte SPACE_BYTE = ' ';

    public ASCIIWhitespaceFilter(OutputStream out) {
        super(out);
    }

    private boolean accept(int b) {
        return b != CR  && b != LF;
    }

    private boolean isASCIIWhitespace(byte b) {
        return b == SPACE_BYTE || b == CR_BYTE || b == LF_BYTE;
    }

    /** Accepts a whitespace char if the previous wasn't a whitepace */
    private boolean accept(byte last, byte b) {
        if (isASCIIWhitespace(last)) {
            return !isASCIIWhitespace(b);
        } else {
            return true;
        }
    }

    /**
     * Filter characters out of the source array.
     *
     * @param b
     * @param offset
     * @param length
     * @return a new copy of the array
     */
    private byte[] filter(byte b[], int offset, int length) {
        byte[] filtered = copyArray(b);
        byte last = 'a';

        int index = 0;
        for (int sourceIndex = offset; sourceIndex < offset + length; sourceIndex++) {
            if (accept(last, b[sourceIndex])) {
                filtered[index++] = b[sourceIndex];
            }
            last = b[sourceIndex];
        }
        return filtered;
    }

    private byte[] filter(byte b[]) {
        return filter(b, 0, b.length);
    }

    private byte[] filter(byte b) {
        return filter(new byte[] { b }, 0, 1);
    }

    private byte[] copyArray(byte[] b) {
        byte filtered[] = new byte[b.length];
        System.arraycopy(b, 0, filtered, 0, b.length);
        return filtered;
    }

    @Override
    public void write(int b) throws IOException {
        if (accept(b)) {
            super.write(b);
        }
    }
}
