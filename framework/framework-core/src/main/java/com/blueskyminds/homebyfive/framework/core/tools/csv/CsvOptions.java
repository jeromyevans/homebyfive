package com.blueskyminds.homebyfive.framework.core.tools.csv;

/**
 * Options controlling the CSV parser
 *
 * Date Started: 11/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class CsvOptions {

    private boolean quoteOutput = true;
    private char quoteChar = '\'';
    private String quoteString = "\'";
    private String quoteCharEscaped = "\'\'";
    private boolean escapeOutput = true;
    private boolean trimBeforeOutput = true;
    private boolean blankIsNull = true;
    public final String NULL_VALUE = "null";

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public boolean isQuoteOutput() {
        return quoteOutput;
    }

    public void setQuoteOutput(boolean quoteOutput) {
        this.quoteOutput = quoteOutput;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public String getQuoteString() {
        return quoteString;
    }

    public void setQuoteString(String quoteString) {
        this.quoteString = quoteString;
    }

    public String getQuoteCharEscaped() {
        return quoteCharEscaped;
    }

    public void setQuoteCharEscaped(String quoteCharEscaped) {
        this.quoteCharEscaped = quoteCharEscaped;
    }

    public boolean isEscapeOutput() {
        return escapeOutput;
    }

    public void setEscapeOutput(boolean escapeOutput) {
        this.escapeOutput = escapeOutput;
    }

    public boolean isTrimBeforeOutput() {
        return trimBeforeOutput;
    }

    public void setTrimBeforeOutput(boolean trimBeforeOutput) {
        this.trimBeforeOutput = trimBeforeOutput;
    }

    public boolean isBlankIsNull() {
        return blankIsNull;
    }

    public void setBlankIsNull(boolean blankIsNull) {
        this.blankIsNull = blankIsNull;
    }

}
