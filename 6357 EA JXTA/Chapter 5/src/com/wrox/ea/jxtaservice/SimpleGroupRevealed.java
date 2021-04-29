package com.wrox.ea.jxtaservice;
import net.jxta.endpoint.Message;
import net.jxta.protocol.ResolverResponseMsg;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class SimpleGroupRevealed extends PGServiceBase {

  public SimpleGroupRevealed(String svcName) {
   super(svcName);
  }

  protected void serviceLogic(Message msg) {
    System.out.println("... gotta request...");
    String myCommand = msg.getString(PGServiceBase.clientDataTag);
    System.out.println(myCommand);
   }

  protected void resolverQueryLogic(String str){
     System.out.println(".. gotta RESOLVER REQUEST...");
     System.out.println("Query = " + str);
   }

  protected void resolverResponseLogic(ResolverResponseMsg resp){
    System.out.println("!!!!Got a Response!!!!!");
   }



}