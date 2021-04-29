package com.wrox.ea.jxtaservice;

import net.jxta.endpoint.Message;
import net.jxta.protocol.ResolverResponseMsg;

import java.io.*;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class SimpleGroupService extends PGServiceBase {

  public SimpleGroupService(String svcName) {
   super(svcName);

  }

protected void serviceLogic(Message msg) {
 System.out.println("... gotta request...");
 String myCommand = msg.getString(clientDataTag);
 System.out.println(myCommand);
}


}