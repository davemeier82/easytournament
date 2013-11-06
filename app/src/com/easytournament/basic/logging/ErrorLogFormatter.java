package com.easytournament.basic.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.easytournament.basic.MetaInfos;


public class ErrorLogFormatter extends Formatter {

  private DateFormat dateformatter = DateFormat.getDateTimeInstance(
      DateFormat.SHORT, DateFormat.MEDIUM, Locale.US);

  public ErrorLogFormatter() {
    super();
  }

  public String format(LogRecord record) {

    StringBuffer sb = new StringBuffer();

    sb.append("Date: ");
    Date date = new Date(record.getMillis());
    sb.append(dateformatter.format(date));
    sb.append("\nVersion: ");
    sb.append(MetaInfos.getVersionNr());

    sb.append("\nLevel: ");
    sb.append(record.getLevel().getName());
    if (record.getSourceClassName() != null) {
      sb.append("\nClass: ");
      sb.append(record.getSourceClassName());
      sb.append(" Method: ");
      sb.append(record.getSourceMethodName());
    }
    else {
      sb.append("\nLogger: ");
      sb.append(record.getLoggerName());
    }
    sb.append("\nMessage : ");
    sb.append(formatMessage(record));
    String throwable = "";
    if (record.getThrown() != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.println();
      record.getThrown().printStackTrace(pw);
      pw.close();
      throwable = sw.toString();
    }
    sb.append(throwable);
    sb.append("\n");

    return sb.toString();
  }
}
