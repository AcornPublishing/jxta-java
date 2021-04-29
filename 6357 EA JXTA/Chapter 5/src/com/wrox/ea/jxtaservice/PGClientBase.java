package com.wrox.ea.jxtaservice;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.peergroup.PeerGroup;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointMessenger;
import net.jxta.endpoint.EndpointService;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class PGClientBase implements EndpointListener {
    public static final String serviceAddressTag = PGServiceBase.serviceAddressTag;
    public static final String clientDataTag = "dataTag";
    private String serviceAddress = null;
    private String serviceName = null;
    private EndpointService myEPS = null;
    private String PeerID = null;
    private String GroupID = null;
    private EndpointAddress myAddress = null;
    private String myAddressStr = null;

  public PGClientBase(String svName) {
     serviceName = svName;
  }
  public void init(PeerGroup group) {
        PeerID = group.getPeerID().getUniqueValue().toString();
        GroupID = group.getPeerGroupID().getUniqueValue().toString();
        myEPS = group.getEndpointService();


        myAddressStr = "jxta://" + PeerID +
                "/Resolved" +
                "/"+ GroupID;

        myAddress = myEPS.newEndpointAddress(myAddressStr);
        myEPS.addListener(myAddress.getServiceName()
                         + myAddress.getServiceParameter(), this);

      if (null == serviceAddress) {
      try {
          Message message = myEPS.newMessage();
          String tpmsg = myAddressStr + ";Just Some Data";

          ResolverQuery query = new ResolverQuery(serviceName +
						group.getPeerGroupID().getUniqueValue().toString(),
						null,
						group.getPeerID().toString(),
						tpmsg,
						100);

        group.getResolverService().sendQuery(null, query);

         Thread.sleep(5000);
         } catch (Exception ex) { ex.printStackTrace(); }

       }  //of if
     }
   public void sendMessageToService(String inMsg) {

    try {
     if (null != serviceAddress) {
      System.out.println("TRYING TO SEND MESSAGE TO SERVICE now...");
        EndpointAddress liveAddr = myEPS.newEndpointAddress(serviceAddress);
        EndpointMessenger myMsgr = myEPS.getMessenger(liveAddr);

        Message message = myEPS.newMessage();
        message.setBytes(clientDataTag, inMsg.getBytes());
        myMsgr.sendMessage(message);
        myMsgr.close();
        }
       } catch ( Exception ex ) {
               ex.printStackTrace();
      }
  }

  public void tapIncomingMessage(Message msg) {
  }
  public void processIncomingMessage(Message message,
        EndpointAddress srcAddr, EndpointAddress dstAddr) {

        if (null == serviceAddress) {
         serviceAddress = message.getString(serviceAddressTag);

        }
         tapIncomingMessage(message);
  }
  }