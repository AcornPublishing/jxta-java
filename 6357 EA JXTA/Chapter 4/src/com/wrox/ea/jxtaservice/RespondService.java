package com.wrox.ea.jxtaservice;
import net.jxta.endpoint.MessageElement;
import java.io.InputStream;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class RespondService extends ServiceBase {

  public RespondService() {
    super(new JXTAServiceInfo("RespondService", "JXTA Service with Respond Pipe - a Wrox EA Example",
    "1.0", "wrox.com", "http://www.wrox.com/ea/jxta/RespondService", "respondservice.pipe",
    true));


    }

  public void serviceLogic(MessageElement elem) {
    InputStream ip = elem.getStream();
    try {
    while (ip.available() > 0)
      System.out.write(ip.read());
    } catch (Exception ex) {}
    System.out.flush();
  }

  public String respondLogic() {
System.out.println("RESP SERV: respond logic called");

  return ("my final answer");
  }

        public static void main(String args[]) {
        RespondService myapp = new RespondService();
        System.out.println ("Service with Response pipes starting...");
        myapp.init();

        System.exit(0);
    }
}