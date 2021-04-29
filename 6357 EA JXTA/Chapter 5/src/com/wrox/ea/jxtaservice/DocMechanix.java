package com.wrox.ea.jxtaservice;
import java.io.*;

import net.jxta.document.*;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class DocMechanix {

  public DocMechanix() {
  }
  public net.jxta.document.StructuredDocument createStructuredDocument() {
    return null;
  }
  public net.jxta.document.Element findElement() {
    return null;
  }
  public String doc2String (StructuredDocument inDoc) {
     return "";
  }
  public StructuredDocument string2Doc (String inStr) {
    return null;
  }

  public static void dumpDoc(Document inDoc, PrintStream inPW) {
	    StructuredTextDocument doc = (StructuredTextDocument)
		inDoc;


            try {
	    StringWriter out = new StringWriter();
	    doc.sendToWriter(out);
	    inPW.println(out.toString());
	    out.close();
            } catch (Exception ex) {
              System.out.println("DocMechanix: dumpDoc failed");
              ex.printStackTrace();
            }
       }

}
