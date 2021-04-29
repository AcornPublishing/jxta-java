package net.jxta.impl.shell.bin.mkdoc;

import net.jxta.impl.shell.*;
import net.jxta.endpoint.*;
import net.jxta.document.*;


public class mkdoc extends ShellApp {
   ShellEnv env;
  private static final String elementDefine = "-e";
  private static final String combineElements = "-c";
  private static final String makeDoc = "-d";
  private String myDocType = null;

   private String elementName = null;
   private String elementContent = null;
   private String myElement = null;

public mkdoc() {
  }




  public int startApp (String[] args) {

	if ((args == null) || (args.length < 3)) {
	    return reportError();
	}

      env = getEnv();

      if (elementDefine.equals(args[0])) { // define a new element
        
         elementName = args[1];
         elementContent = getRestOfArgs(args,2);
      
            myElement = "<" + elementName + ">" +elementContent + "</"+elementName +">";

	
      }

      if (combineElements.equals(args[0])) { // combine elements into new element
       elementName = args[1];
       elementContent = mergeAllElements(args,2);
       myElement = "<" + elementName + ">" +elementContent + "</"+elementName +">";

        }

       if (makeDoc.equals(args[0])) {// create a structured document
          myDocType = args[1];
          myElement = mergeAllElements(args,2);
 	StructuredDocument doc = null;
 	try {     
 	    	
	    doc = StructuredDocumentFactory.newStructuredDocument (
								   new MimeMediaType ("text/xml"),myDocType,
								   myElement);
	} catch (Exception ex) {
	    	 println("mkdoc: " + ex.toString());
	    return ShellApp.appMiscError;
	}
        env.add(getReturnVariable(),
                                  new ShellObject("StructuredDocument", doc));


        return ShellApp.appNoError;

        }
        env.add(getReturnVariable(),
                                  new ShellObject("String", myElement));


     return ShellApp.appNoError; 
  }

  public void stopApp () {
  }

  public String getRestOfArgs(String [] args, int idx) {
    int arysize = args.length;
    String res = args[idx];

    for (int i=(idx + 1); i < arysize; i++) {
       res = res + " " + args[i];

      }
     return res;

   }

  public String mergeAllElements(  String [] args, int idx) {

    int arysize = args.length;
    String res = "";
    try {
    for (int i= idx; i <arysize; i++) {
           ShellObject obj = env.get (args[i]);
           res += (String) obj.getObject();
       }
    }
    catch (Exception ex) { 
      println("mkdoc: sorry, cannot access some elements.");
      res = "";  
      } 
     return res;
  }
 private int reportError() {
	println ("usage: mkdoc [-e <element name> <element content>]");
      println ("             [-c <element name> <element> .... ]");
      println ("             [-d <doctype> <element> .... ]");
      println (" ");
      println ("'man mkdoc' to get more information.");
	return ShellApp.appParamError;
    }

  public String getDescription() {
      return "Creates a structured document in memory.";
  }


  public void help() {
      println("NAME");
      println("     mkdoc - create a sructured document in memory.");
      println(" ");
      println("SYNOPSIS");
      println("     mkdoc [-e <element name> <element content>]");
      println("           [-c <element name> <element>...]");
      println("           [-d <doctype> <element> ....]");

      println(" ");
      println("DESCRIPTION");
      println(" ");
      println("'mkdoc' provides a way to create complete structured");
      println("documents from scratch, in memory, without using file.");
      println(" ");
      println("First, you use -e to create environment variables that");
      println("contains the element you want. Then you can use -c to");
      println("combine them in new elements, or just create a structured");
      println("document using the -d option");
            println(" ");
      println("EXAMPLE");
      println("    JXTA>name=mkdoc -e name Joe Baxer");
      println("    JXTA>phone=mkdoc -e phone 223-222-3333");
      println("    JXTA>mydoc=mkdoc -d CustData name phone");
      println("Explanation");
      println("    The commands above creates a structured document with");
      println("     root of CustData containing the name and phone info.");
     println(" ");
       }


}