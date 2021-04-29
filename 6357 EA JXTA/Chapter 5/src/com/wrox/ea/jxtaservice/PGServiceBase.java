package com.wrox.ea.jxtaservice;

import java.lang.reflect.InvocationTargetException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Document;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.EndpointService;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.EndpointMessenger;
import net.jxta.endpoint.Message;

import net.jxta.exception.PeerGroupException;
import net.jxta.exception.NoResponseException;
import net.jxta.exception.ResendQueryException;
import net.jxta.exception.DiscardQueryException;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;

import net.jxta.platform.Application;

import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;

import net.jxta.resolver.QueryHandler;

import net.jxta.impl.protocol.ResolverResponse;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public abstract class PGServiceBase  implements Application, EndpointListener, QueryHandler  {
    public static final String serviceAddressTag = "address";
    public static final String clientDataTag = PGClientBase.clientDataTag;
    private static String serviceName = null;


    private PeerGroup group = null;
    public PGServiceBase(String inName) {
       serviceName = inName;
    }


   public void init(PeerGroup group,
		     ID assignedID,
                     Advertisement impl)
	throws PeerGroupException {

        this.group = group;
    }

     public int startApp(String[] arg) {
               // register the message listener
        group.getEndpointService().addListener(serviceName + getGroupId(group),
					       this);

        group.getResolverService().registerHandler(serviceName + getGroupId (group),
						   this);

        return 1;
    }

  public static String getGroupId(PeerGroup group) {
        return group.getPeerGroupID().getUniqueValue().toString();
    }


     public void stopApp() {
    }

   public EndpointService getEndpointService() {
        return group.getEndpointService();
    }

    public String getEndpointAddress() {
        return "jxta://" + getPeerId(group) +
            "/" + serviceName +
            "/" + getGroupId(group);
    }

   public static String getPeerId(PeerGroup group) {
        return group.getPeerID().getUniqueValue().toString();
    }

protected abstract void serviceLogic(Message msg);
    public void processIncomingMessage(Message message,
        EndpointAddress srcAddr, EndpointAddress dstAddr) {

        serviceLogic( message);

    }
 protected  void resolverQueryLogic(String str) {
    }
    public ResolverResponseMsg processQuery(ResolverQueryMsg query)
        throws NoResponseException, ResendQueryException, DiscardQueryException, IOException {

        String queryStr = query.getQuery();

        int index = queryStr.indexOf(';');
        if (index != -1) {
        String addressStr = queryStr.substring(0, index);
        String subString = queryStr.substring(index+1);

        Message message = group.getEndpointService().newMessage();

        message.setBytes(serviceAddressTag, getEndpointAddress().getBytes());
        EndpointAddress myAddr = group.getEndpointService().newEndpointAddress(addressStr);
        EndpointMessenger myMess = group.getEndpointService().getMessenger(myAddr);
        myMess.sendMessage(message);
        myMess.close();
        resolverQueryLogic(subString);
        } else resolverQueryLogic(queryStr);

        throw new NoResponseException();
      }

    protected  void resolverResponseLogic(ResolverResponseMsg resp) {
    }

    public void processResponse(ResolverResponseMsg response) {

    resolverResponseLogic(response);
    }
  public void finalize () {
	stopApp();
    }
}
