package com.blueskyminds.homebyfive.framework.core.tools.csv;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * ----------------------------------------------------------------------------------------------------------
 *
 * Simple class to represent a single line of a CSV file
 *
 * Written by Jeromy Evans
 *
 * History:
 *  13 Jan 06 - modified to suppory multiple-line fields
 *  14 Feb 06 - modified to accept an arraylist of CsvFields instead of strings
 *  11 Jun 06 - ported to Java from C#
 *
 * ---[ Blue Sky Minds Pty Ltd ]-----------------------------------------------------------------------------*/

public class CsvLine {

  /// <summary>
  /// comma separated values extracted on this line
  /// </summary>
  protected ArrayList<CsvField> values;

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// instantiates a CSV line from a list of fields that have already been separated
  /// </summary>
  public CsvLine(ArrayList<CsvField> listOfFields)
  {
     // convert the arraylist of values to the fixed array
     //values = (CsvField[]) listOfFields.toArray();
      values = new ArrayList<CsvField>(listOfFields);
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line and returns it as a String
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>a blank String if index out of bounds or no value</returns>
  public String getAsString(int indexOnLine)
  {
     if (indexOnLine < values.size())
     {
        if (values.get(indexOnLine) != null)
        {
           return values.get(indexOnLine).getValue();
        }
        else
        {
           return "";
        }
     }
     else
     {
        return "";
     }
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line, parsing it as a percent
  /// value (scale down by 100)
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>0.0 if not valid, otherwise the value</returns>
  public float getAsPercent(int indexOnLine)
  {
     float floatValue = 0.0F;
     String stringValue;
     Pattern percentExp = Pattern.compile("^-?\\d+(\\.\\d+)*");
     Matcher matcher;

     if (indexOnLine < values.size()) {
        if (values.get(indexOnLine) != null) {
           try {
              matcher = percentExp.matcher(values.get(indexOnLine).getRawValue());
              if (matcher.find()) {
                 stringValue = matcher.group();

                 floatValue = Float.parseFloat(stringValue);
                 floatValue = floatValue / 100.0F;
              }
           } catch (Exception e) {
               //
           }
        }
     }

     return floatValue;
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
     float floatValue = 0.0F;
     String stringValue;

     Pattern commaDollaExp = Pattern.compile("[\\$|,|\\s]");
     Pattern matchExp = Pattern.compile("^\\d+(\\.\\d+)*");  // 1 or more digits, optionally followed by . and one or more digits
     Matcher matcher;
     String textString;

     if (indexOnLine < values.size()) {
        if (values.get(indexOnLine) != null) {
           try {
               // remove commas and $ in the value
               matcher = commaDollaExp.matcher(values.get(indexOnLine).getRawValue());
               textString = matcher.replaceAll("");
               matcher = matchExp.matcher(textString);
               if (matcher.find()) {
                  stringValue = matcher.group();

                  floatValue = Float.parseFloat(stringValue);
               }
           } catch (Exception e) {
               //
           }
        }
     }

     return floatValue;
  }


  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Gets the value at the specified index on the line, parsing it as an integer
  /// value (optionally contining commas)
  /// </summary>
  /// <param name="indexOnLine">starts from zero</param>
  /// <returns>int.MinValue if not valid, otherwise the value</returns>
  public int getAsInteger(int indexOnLine)
  {
     int intValue = Integer.MIN_VALUE;
     String stringValue;

     Pattern commaExp = Pattern.compile("[,|\\s]");
     Pattern matchExp = Pattern.compile("^\\d+");
     Matcher matcher;
     String textString;

     if (indexOnLine < values.size()) {
        if (values.get(indexOnLine) != null) {
           try {
              // remove commas in the value
              matcher = commaExp.matcher(values.get(indexOnLine).getRawValue());
              textString = matcher.replaceAll("");
              matcher = matchExp.matcher(textString);
              if (matcher.find()) {
                 stringValue = matcher.group();

                 intValue = Integer.parseInt(stringValue);
              }
           } catch (Exception e) {
               //
           }
        }
     }

     return intValue;
  }

  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns true if the current line is non-blank
  /// </summary>
  public boolean isNonBlank()
  {
    return (values.size() > 0);
  }


  // ----------------------------------------------------------------------------------------------------

  /// <summary>
  /// Returns all the values on this line as an array of strings
  /// </summary>
  public String[] getAsStrings()
  {
     String[] stringValues = new String[values.size()];
     int index = 0;

     for (CsvField field : values)
     {
        stringValues[index++] = field.getValue();
     }

     return stringValues;
  }

  // ----------------------------------------------------------------------------------------------------
  // ----------------------------------------------------------------------------------------------------
}

