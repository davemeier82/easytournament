package com.easytournament.statistic.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.table.DefaultTableModel;

public class StatisticCSVHandler {

  public static void saveDefaultStat(File filename, DefaultTableModel tm) throws FileNotFoundException{
    PrintStream out = null;
    try {
        out = new PrintStream(new FileOutputStream(filename));
        for(int col = 0; col < tm.getColumnCount(); col++){
          if(col != 0)
            out.print(";");
          out.print(tm.getColumnName(col));
        }
        out.println();
        for(int row = 0; row < tm.getRowCount(); row++){
          for(int col = 0; col < tm.getColumnCount(); col++){
            if(col != 0)
              out.print(";");
            out.print(tm.getValueAt(row, col));            
          }
          out.println();
        }
    }
    finally {
        if (out != null) out.close();
    }

  }
}
