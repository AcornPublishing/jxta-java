package net.jxta.impl.shell.bin.date;

import net.jxta.impl.shell.*;
import java.util.Calendar;
import java.text.DateFormat;


public class date extends ShellApp {

  public date() {
  }

  public int startApp (String[] args) {
     println (DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
     return ShellApp.appNoError; 
  }

  public void stopApp () {
  }
  public String getDescription() {
      return "Returns the current date and time.";
  }


  public void help() {
      println("NAME");
      println("     date - get the current date and time");
      println(" ");
      println("SYNOPSIS");
      println("     date");
      println(" ");
      println("DESCRIPTION");
      println(" ");
      println("'date' prints the current date and time to stdout.");
            println(" ");
      println("EXAMPLE");
      println("    JXTA> date");
     println(" ");
       }


}