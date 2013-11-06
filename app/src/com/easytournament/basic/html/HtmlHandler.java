package com.easytournament.basic.html;

import java.io.PrintStream;

public class HtmlHandler {

  public static void writeHtmlHead(PrintStream out) {
    out.print("<head>");
    out.println("<style type=\"text/css\">");
    out.println("body {");
    out.println("\tfont-family: Arial, Helvetica, sans-serif;");
    out.println("}");
    out.println("h1 {");
    out.println("\ttext-align: center;");
    out.println("}");
    out.println("h2 {");
    out.println("\ttext-align: center;");
    out.println("}");
    out.println("table {");
    out.println("\tmargin-left: auto; ");
    out.println("\tmargin-right: auto;");
    out.println("\tborder-width: 1px;");
    out.println("\tborder-spacing: 0px;");
    out.println("\tborder-style: outset;");
    out.println("\tborder-color: black;");
    out.println("\tborder-collapse: collapse;");
    out.println("\tbackground-color: white;");
    out.println("}");
    out.println("\ttable th {");
    out.println("\tborder-width: 1px;");
    out.println("\tpadding: 5px;");
    out.println("\tborder-style: outset;");
    out.println("\tborder-color: black;");
    out.println("\tbackground-color: white;");
    out.println("}");
    out.println("\ttable td {");
    out.println("\tborder-width: 1px;");
    out.println("\tpadding: 5px;");
    out.println("\tborder-style: outset;");
    out.println("\tborder-color: black;");
    out.println("\tbackground-color: white;");
    out.println("}");     

    out.println("</style>");
    out.println("</head>");
  }
}
