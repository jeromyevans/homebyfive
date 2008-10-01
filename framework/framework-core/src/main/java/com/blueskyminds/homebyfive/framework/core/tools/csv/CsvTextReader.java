package com.blueskyminds.homebyfive.framework.core.tools.csv;

import java.io.*;
import java.util.ArrayList;
import java.net.URL;

/**
 *
 * Simple class to read a CSV file into an object.  the file is read into an
 * array of CsvLine objects
 *
 * Written by Jeromy Evans
 *
 * History:
 *  13 Jan 06 - modified to suppory multiple-line fields
 *  14 Feb 06 - created a CsvField subclass that contains metadata about the field.  This is the first step
 *  towards allowing metadata to be specified for columns that affect how the data is handled
 *  (eg. blanks are blank or blanks are nulls, on a column by column basis)
 *  11 Jun 06 - converted to Java from C#
 *
 * Date Started: 11/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

/// <summary>
/// Summary description for CSVFile.
/// </summary>
public class CsvTextReader {

  enum State {IN_TEXT, IN_QUOTE}

    private CsvOptions csvOptions;

  // ----------------------------------------------------------------------------------------------------
  /// <summary>
  /// Array of lines in the file
  /// </summary>
  protected ArrayList<CsvLine> csvLines = null;

  /// <summary>
  /// Current index into the CSVlines
  /// </summary>
  protected int lineIndex = -1;

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Instantiates the reader, reads the entire file into memory and makes each line
  /// available for parsing
  /// </summary>
  /// <param name="fileName"></param>
    public CsvTextReader(String fileName)
    {
        try {
            this.csvOptions = new CsvOptions(); // use default options
            start(new URL(fileName).openStream());
        } catch(IOException e) {
            //
        }
    }

    public CsvTextReader(String fileName, CsvOptions csvOptions)
    {
        try {
            this.csvOptions = csvOptions;
            start(new URL(fileName).openStream());
        } catch(IOException e) {
            //
        }
    }

    // ----------------------------------------------------------------------------------------------------

    public CsvTextReader(InputStream inputStream) {

        this.csvOptions = new CsvOptions(); // use default options
        start(inputStream);
    }

    // ----------------------------------------------------------------------------------------------------

    public CsvTextReader(InputStream inputStream, CsvOptions csvOptions) {
        this.csvOptions = csvOptions;
        start(inputStream);

    }

    // ----------------------------------------------------------------------------------------------------

    private void start(InputStream inputStream) {

        String content = readTextFile(inputStream);

        if (content.length() > 0)
        {
            parseCSV(content);
        }
    }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// reads an entire text file into memory for parsing
  /// </summary>
  /// <param name="fileName"></param>
  /// <returns>a String containing the entire content</returns>
  protected static String readTextFile(InputStream inputStream)
  {
     StringBuffer content = new StringBuffer();
     InputStreamReader in = null;
     char[] buffer = new char[1000];
     int charsRead;

     try
     {
         in = new InputStreamReader(inputStream);

         // read to the end of file, up to 1000 bytes at a time
         while ((charsRead = in.read(buffer)) > 0) {
             content.append(buffer, 0, charsRead);
         }

     }
     catch (IOException e) {
         //
     }
     finally
     {
        if (in != null)
        {
            try {
                in.close();
            } catch (IOException e) {
                //
            }
        }
     }

     return content.toString();
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// moves to the next line in the csv buffer.  returns true if successful
  /// </summary>
  /// <returns></returns>
  public boolean read()
  {
     if (csvLines != null)
     {
        lineIndex++;
        if (lineIndex < csvLines.size())
        {
           return true;
        }
        else
        {
           lineIndex--;  // undo - keep in bounds
           return false;
        }
     }
     else
     {
        return false;
     }
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// This inner class implements a state machine to parse a text file character
  /// by character and convert it into the array of CSV lines
  /// </summary>
  protected class CSVParser
  {

     // ----------------------------------------------------------------------------------------------------

     /// <summary>
     /// Array of characters to be parsed
     /// </summary>
     protected char[] charArray;

     // ----------------------------------------------------------------------------------------------------

     /// <summary>
     /// List of the CSVLines created from this document
     /// </summary>
     protected ArrayList<CsvLine> listOfCsvLines;

     // ----------------------------------------------------------------------------------------------------

     private static final char QUOTE_CHAR = '\"';
     private static final char ESCAPE_CHAR = '\\';
     private static final char EOL_CHAR1 = '\n';
     private static final char EOL_CHAR2 = '\r';
     private static final char SEPARATOR = ',';

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// The content of the current field being assembled
     /// </summary>
     protected StringBuilder thisFieldString;

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// The list of fields found on the current line
     /// </summary>
     protected ArrayList<CsvField> fieldsOnThisLine;

     // ----------------------------------------------------------------------------------------------------

     /// <summary>
     /// Extracts the field for the current position and returns it as a String
     /// </summary>
     /// <returns>The field value that was added</returns>
     protected CsvField extractThisField()
     {
        String thisValue = thisFieldString.toString().trim();
        CsvField csvField = new CsvField(csvOptions, thisValue);

        // special exception check - a blank value in quotations is converted
        // to a single quotation (parser first treats as an escaped quote)
        // this converts back to a blank.
        if (thisValue.equals("\""))
        {
           csvField.setValue("");
        }

        return csvField;
     }

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// Adds the specified field value to this list for the current line
     /// </summary>
     protected void storeThisField(CsvField csvField)
     {
        fieldsOnThisLine.add(csvField);
     }

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// Resets the current field value
     /// </summary>
     protected void resetThisField()
     {
         thisFieldString.setLength(0);
     }

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// Resets the current line
     /// </summary>
     protected void resetThisLine()
     {
        fieldsOnThisLine.clear();
     }

     // ----------------------------------------------------------------------------------------------------
     /// <summary>
     /// remembers the current line.
     /// </summary>
     protected void storeThisLine()
     {
        listOfCsvLines.add(new CsvLine(fieldsOnThisLine));
     }

     // ----------------------------------------------------------------------------------------------------

     public CSVParser(char[] charArray)
     {
        this.charArray = charArray;

        doParse();
     }

     // ----------------------------------------------------------------------------------------------------

     /// <summary>
     /// Performs the parsing of the character array
     /// Loops through character by character, adding each char to a stringBuffer to form
     /// field, and adding each field to an arraylist to form a line.
     /// The character-by-character parsing is necessary to detect
     /// quotes (to permit multi-line fields) and escape characters (escaped commas
     /// and quotes)
     /// </summary>
     protected void doParse()
     {
        int currentPos = 0;
        boolean keepThisChar;
        boolean parsed;
        CsvField thisFieldValue;
        boolean inQuote = false;
        int totalChars = charArray.length;
        char thisChar;

        listOfCsvLines = new ArrayList<CsvLine>();
        thisFieldString = new StringBuilder();
        fieldsOnThisLine = new ArrayList<CsvField>(1000);

        // loop character by character...
        while (currentPos < totalChars)
        {
           thisChar = charArray[currentPos];

           keepThisChar = true;

           // determine what this character is...
           switch (thisChar)
           {
              /*case ESCAPE_CHAR:
                 // the escape char means the next character is part of
                 // the text. This is important if its a comma, quote etc

                 // preview the next char
                 if (currentPos < charArray.length-1)
                 {
                    // the next char is escaped so it becomes part of the text
                    // instead of the escape char
                    thisChar = charArray[currentPos+1];

                    // LEAP AHEAD one character - it's escaped, so it doesn't need
                    // to be parsed (this simplies the parser because it doesn't
                    // encounter an escaped comma, quote, etc
                    currentPos++;
                 }
                 else
                 {
                    // at the end of the document...the character is part of the last field
                 }
                 break;*/
              case QUOTE_CHAR:

                 // the quote char may indicate the start of quoted text
                 // two quotes in a row indicate an escaped quote char

                 parsed = false;

                 // preview the next char to see if it's a quote too
                 if (currentPos < charArray.length-1)
                 {
                    if (charArray[currentPos+1] == QUOTE_CHAR)
                    {
                       // the next char is a quotation too...this is text and
                       // needs to be retained (exception: if this is a blank String
                       // in quotations)

                       // LEAP AHEAD one character - it's escaped, so it doesn't need
                       // to be parsed (this simplifies the parser because it doesn't
                       // need to look back when it hits the next quote)
                       currentPos++;
                       parsed = true;
                    }
                 }

                 if (!parsed)
                 {
                    // unescaped quotes allow fields to extend over multiple lines
                    if (inQuote)
                    {
                       // this is the end of the current quote
                       inQuote = false;
                    }
                    else
                    {
                       // this is the start of a quote
                       inQuote = true;
                    }

                    // don't use this character in the field's value
                    keepThisChar = false;
                 }

                 break;

              case SEPARATOR :
                 if (!inQuote)
                 {
                    // the unquoted separator character marks the end of a field
                    thisFieldValue = extractThisField();
                    storeThisField(thisFieldValue);

                    // reset the current field's value
                    resetThisField();

                    // don't keep this character in the field's value
                    keepThisChar = false;
                 }
                 else
                 {
                    // this character is inside the quotes
                    // it's text
                    // (no action to perform for this other than the default below)
                 }
                 break;
              case EOL_CHAR1 :
              case EOL_CHAR2 :
                 if (!inQuote)
                 {
                    // this is the end of the current field and current csv line
                    thisFieldValue = extractThisField();

                    // ensure this is not a blank line with blank value
                    if ((fieldsOnThisLine.size() == 0) && (thisFieldValue.getRawValue().length() == 0))
                    {
                       // nothing on this line to process
                       // (also arrives here between \n\r)
                    }
                    else
                    {
                       // remember the field's value (blanks are permitted)
                       storeThisField(thisFieldValue);

                       // do something with the CSV line
                       storeThisLine();
                    }

                    // reset the current field's value
                    resetThisField();

                    // start a new CSV line
                    resetThisLine();

                    // don't keep this character in the field's value
                    keepThisChar = false;
                 }
                 else
                 {
                    // the end-of-line is within a quotation...it will be retained as
                    // party of the value
                    // (no action to perform for this other than the default below)
                 }
                 break;
              default:
                 // every other character will be keep and appended to the
                 // current field's value (trailing and leading spaces removed later)
                 // (no action to perform for this other than the default below)
                 break;
           }

           if (keepThisChar)
           {
              // keep this char
              thisFieldString.append(thisChar);
           }

           currentPos++;
        }

        // IMPORTANT: if the end of the document is reached and the last line
        // still contains information then it needs to be processed too
        thisFieldValue = extractThisField();
        if (fieldsOnThisLine.size() > 0)
        {
           storeThisField(thisFieldValue);
           storeThisLine();
        }
        else
        {
           // there's nothing on this line...but if the field contains something
           // then it may be a single-value line
           if (thisFieldValue.getRawValue().length() > 0)
           {
              storeThisField(thisFieldValue);
              storeThisLine();
           }
        }
     }

     // ----------------------------------------------------------------------------------------------------

     /// <summary>
     /// Returns the list of CsvLine objects generated by the parser
     /// </summary>
     public ArrayList<CsvLine> getCsvLines() {
        return listOfCsvLines;
     }
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// parses a String of comma deliminated text into a two dimensional array of values
  /// Each array row is a new line in the CSV.
  /// Each subArray is the list of values on that line.  This can be jagged (varying number of values)
  /// </summary>
  /// <param name="csvContent"></param>
  protected void parseCSV(String csvContent)
  {
     // parser
     CSVParser csvParser = new CSVParser(csvContent.toCharArray());

     csvLines = csvParser.getCsvLines();

     /*
     lines = csvContent.Split('\n');
     csvLines = new CsvLine[lines.length];
     for (int i = 0; i < lines.length; i++)
     {
        if (lines[i].length > 0)
        {
           csvLines[i] = new CsvLine(lines[i]);
        }
     } */
  }

  // ----------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line and returns it as a String
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>a blank String if index out of bounds or no value</returns>
  public String getAsString(int indexOnLine)
  {
     if (lineIndex < csvLines.size())
     {
        if (csvLines.get(lineIndex) != null)
        {
           return csvLines.get(lineIndex).getAsString(indexOnLine);
        }
     }
     return null;
  }


  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line, parsing it as a percent
  /// value (optionally including the % sign, and scale down by 100)
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>0.0 if not valid, otherwise the value</returns>
  public float getAsPercent(int indexOnLine)
  {
     if (lineIndex < csvLines.size())
     {
        if (csvLines.get(lineIndex)!= null)
        {
           return csvLines.get(lineIndex).getAsPercent(indexOnLine);
        }
     }
     return 0.0F;
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line, parsing it as a dollar
  /// value (optionally preceeded by the $ sign)
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>0.0 if not valid, otherwise the value</returns>
  public float getAsDollar(int indexOnLine)
  {
     if (lineIndex < csvLines.size())
     {
        if (csvLines.get(lineIndex)!= null)
        {
           return csvLines.get(lineIndex).getAsDollar(indexOnLine);
        }
     }
     return 0.0F;
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line, parsing it as an integer
  /// value (optionally containing commas)
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>int.MinValue if not valid, otherwise the value</returns>
  public int getAsInteger(int indexOnLine)
  {
     if (lineIndex < csvLines.size())
     {
        if (csvLines.get(lineIndex)!= null)
        {
           return csvLines.get(lineIndex).getAsInteger(indexOnLine);
        }
     }
     return Integer.MIN_VALUE;
  }

  // ----------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns true if the current line contains text
  /// </summary>
  public boolean isNonBlank()
  {

    boolean nonBlank = false;

    if (lineIndex < csvLines.size())
    {
       if (csvLines.get(lineIndex)!= null)
       {
          nonBlank = csvLines.get(lineIndex).isNonBlank();
       }
    }
    return nonBlank;
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns all the values on the current line as an array of strings
  /// </summary>
  /// <returns></returns>
  public String[] getAsStrings()
  {
     String[] values = null;

     if (lineIndex < csvLines.size())
     {
        if (csvLines.get(lineIndex)!= null)
        {
           values = csvLines.get(lineIndex).getAsStrings();
        }
     }

     return values;
  }

  // ----------------------------------------------------------------------------------------------------
}
