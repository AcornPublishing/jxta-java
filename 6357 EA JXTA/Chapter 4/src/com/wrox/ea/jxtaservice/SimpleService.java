package com.wrox.ea.jxtaservice;
import net.jxta.endpoint.MessageElement;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class SimpleService extends ServiceBase {

    public SimpleService() {
    super(new JXTAServiceInfo("SimpleService", "Simple JXTA Service - a Wrox EA Example",
    "1.0", "wrox.com", "http://www.wrox.com/ea/jxta/SimpleService", "simpleservice.pipe"
    ));


    }

  public void serviceLogic(MessageElement elem) {

    ByteArrayOutputStream bs = new ByteArrayOutputStream();

    InputStream ip = elem.getStream();
    try {
    while (ip.available() > 0)
      bs.write(ip.read());

    bs.flush();

    String cmd = bs.toString();

    System.out.println(cmd);
    bs.close();
    } catch (Exception ex) {}
  }

        public static void main(String args[]) {
        SimpleService myapp = new SimpleService();
        System.out.println ("Simple service starting...");
        myapp.init();

        System.exit(0);
    }

}

