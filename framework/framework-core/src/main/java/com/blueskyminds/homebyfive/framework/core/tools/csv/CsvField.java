package com.blueskyminds.homebyfive.framework.core.tools.csv;

/**
 * Represents a single field with a CSV line and the metadata associated with that field about
 * how it should be treated
 * Written by Jeromy Evans
 *
 * Date Started: 11/06/2006
 *
 * History:
 *   14 Feb 06 - started
 *   11 Jun 06 - ported to Java from C#
 *             - factored the options controlling the parsing into its own class
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public class CsvField
{
  protected String csvValue = null;

  private CsvOptions csvOptions;

  // ----------------------------------------------------------------------------------------------------

  public CsvField(CsvOptions csvOptions,  String value)
  {
      this.csvOptions = csvOptions;
      this.csvValue = value;
  }

  // ----------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Escape text for SQL insert
  /// </summary>
  /// <param name="textToEscape"></param>
  /// <returns></returns>
  protected String escape(String textToEscape)
  {
     return textToEscape.replace(csvOptions.getQuoteString(), csvOptions.getQuoteCharEscaped());
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns the current value in the specified format
  /// </summary>
  public String getValue()
  {
    String outputValue;
    boolean finishedProcessing = false;

    // get the current value and trim whitespace if enabled to do so
    if (csvOptions.isTrimBeforeOutput())
    {
       outputValue = csvValue.trim();
    }
    else
    {
       outputValue = csvValue;
    }

    // determine if the length is zero...
    if (outputValue.length() == 0)
    {
       // it's a blank
       if (csvOptions.isBlankIsNull())
       {
          outputValue = csvOptions.NULL_VALUE;
          // value has been set - do not process further
          finishedProcessing = true;
       }
    }

    if (!finishedProcessing)
    {
       if (csvOptions.isQuoteOutput())
       {
          if (csvOptions.isEscapeOutput())
          {
             // escape and quote the value
             outputValue = csvOptions.getQuoteChar()+escape(outputValue)+csvOptions.getQuoteChar();
          }
          else
          {
             // quote, but don't escape the value (dangerous really)
             outputValue = csvOptions.getQuoteChar()+outputValue+csvOptions.getQuoteChar();
          }
       }
       else
       {
          // don't quote the value
       }
    }

    return outputValue;
 }

   public void setValue(String value) {
        this.csvValue = value;
   }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns the current value as a string without formatting/processing
  /// </summary>
  public String getRawValue()
  {
      return csvValue;     
  }
}

