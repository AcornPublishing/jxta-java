package com.wrox.ea.jxtaservice;

import java.io.ByteArrayInputStream;

import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.StructuredDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.document.Element;
import net.jxta.document.StructuredDocumentUtils;

import net.jxta.platform.Application;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.MimeMediaType;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.OutputPipeListener;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipe;

import net.jxta.id.IDFactory;
import net.jxta.id.ID;

import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.peergroup.PeerGroupFactory;

import net.jxta.impl.peergroup.Platform;
import net.jxta.impl.peergroup.GenericPeerGroup;


import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import net.jxta.impl.endpoint.MessageElementImpl;

import net.jxta.discovery.DiscoveryService;

import net.jxta.exception.PeerGroupException;


import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.ModuleClassAdvertisement;

import net.jxta.platform.ModuleClassID;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */


public abstract class ServiceBase  implements  PipeMsgListener,OutputPipeListener { // extends Thread  {
    private DocMechanix instanceDocm = null;
    private GroupMechanix instanceGroupm=null;
    private MessageMechanix instanceMessagem=null;
    private PipeMechanix instancePipem = null;
    private PersistenceMechanix instancePersistencem = null;
    private PeerMechanix instancePeerm = null;

    static PeerGroup group = null;
    static PeerGroupAdvertisement groupAdvertisement = null;

    private DiscoveryService disco;
    private PipeService pipes;
    private InputPipe myPipe; // input pipe for the service
    private Message msg;      // pipe message received
    private ID gid;  // group id

    private JXTAServiceInfo instanceInfo;

    public static final String MODULE_CLASS_PREFIX = "JXTAMOD:";
    public static final String MODULE_SPEC_PREFIX = "JXTASPEC:";

    public static final MimeMediaType textPlainType = new MimeMediaType("text/plain");
    public static final MimeMediaType textXmlType = new MimeMediaType("text/xml");
    public static String requestDataTag = "WroxCall";
    public static String responsePipeTag = "WroxResponse";


    private String advertisedServiceName = null;
    private String advertisedPipeName = null;
    public ServiceBase(JXTAServiceInfo  inServ) {
    instanceInfo = inServ;
    advertisedServiceName = MODULE_CLASS_PREFIX + instanceInfo.getName();
    advertisedPipeName = MODULE_SPEC_PREFIX + instanceInfo.getName();
    }


  public void init()  {
       startJxta();

        groupAdvertisement = group.getPeerGroupAdvertisement();
        System.out.println("Getting DiscoveryService");
        disco = group.getDiscoveryService();
        System.out.println("Getting PipeService");
        pipes = group.getPipeService();

        instanceGroupm = new GroupMechanix(group);
        instancePipem = new PipeMechanix(group);
        instanceMessagem = new MessageMechanix();
        instanceDocm = new DocMechanix();
        instancePeerm = new PeerMechanix();
        instancePersistencem = new PersistenceMechanix( group);
	startService();
  }


      private void startService() {

	System.out.println("Start the Server daemon");

	// get the peergroup service we need
	gid = group.getPeerGroupID();

	try {

           // create the service advertisement
	    ModuleClassAdvertisement classAdv = (ModuleClassAdvertisement)
                AdvertisementFactory.newAdvertisement(
		ModuleClassAdvertisement.getAdvertisementType());
	    classAdv.setName(advertisedServiceName);
	    classAdv.setDescription(instanceInfo.getDescription());

	    ModuleClassID classID = IDFactory.newModuleClassID();

	    classAdv.setModuleClassID(classID);
	    disco.publish(classAdv, DiscoveryService.ADV);
	    disco.remotePublish(classAdv, DiscoveryService.ADV);


	    ModuleSpecAdvertisement specAdv = (ModuleSpecAdvertisement)
                AdvertisementFactory.newAdvertisement(
		ModuleSpecAdvertisement.getAdvertisementType());


	    specAdv.setName(MODULE_SPEC_PREFIX + instanceInfo.getName());
	    specAdv.setVersion(instanceInfo.getVersion());
	    specAdv.setCreator(instanceInfo.getCreator());
	    specAdv.setModuleSpecID(IDFactory.newModuleSpecID(classID));
	    specAdv.setSpecURI(instanceInfo.getURI());

            PipeAdvertisement pipeadv =
            instancePersistencem.CreatePipeAdvIfNotExist(advertisedPipeName,
            instanceInfo.getPersistedPipeAdv());



            StructuredTextDocument paramDoc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument
            (textXmlType,"Parm");

            StructuredDocumentUtils.copyElements(paramDoc, paramDoc, (Element)
             pipeadv.getDocument(textXmlType));
	     specAdv.setParam((StructuredDocument) paramDoc);

	    disco.publish(specAdv, DiscoveryService.ADV);
	    disco.remotePublish(specAdv, DiscoveryService.ADV);

       instanceDocm.dumpDoc( specAdv.getDocument(textPlainType),
         System.out);

         // create a pipe and subscribe to incoming messages
	    myPipe = pipes.createInputPipe(pipeadv, (PipeMsgListener) this);

	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.out.println("ServerBase: advertisement creation failure");
	}
	    System.out.println("Waiting for client messages to arrive....");

      synchronized(this) {
      try {
         wait(); } catch (Exception ex) {}
         }
    }

   public void pipeMsgEvent( PipeMsgEvent ev) {

      System.out.println("ServiceBase: A request has been received!");

   PipeAdvertisement pipeAdv = null;
    Message inReq = ev.getMessage();
    MessageElement coreMsg = inReq.getElement(requestDataTag);

    try {
    if (coreMsg != null)
      serviceLogic(coreMsg);

    if (instanceInfo.hasResponse()) {
    System.out.println("FOUND A REPLY PIPE");
           MessageElement respPipe = inReq.getElement(responsePipeTag);
           if (respPipe != null)   { //response pipe included
	        pipeAdv = (PipeAdvertisement)
                     AdvertisementFactory.newAdvertisement(
		         textXmlType, respPipe.getStream());
               pipes.createOutputPipe(pipeAdv, this);
          }
      }

     } catch (Exception ex) {}
}
   protected abstract void serviceLogic(MessageElement el);

   private void startJxta() {
        try {
            // create, and Start the default jxta NetPeerGroup
            group = PeerGroupFactory.newNetPeerGroup();
        } catch (PeerGroupException e) {
            // could not instanciate the group, print the stack and exit
            System.out.println("ServiceBase : group creation failure");
            e.printStackTrace();
            System.exit(1);
        }

    }
protected  String respondLogic() {
return "";
}
      public void outputPipeEvent(OutputPipeEvent evt) {
      // create the data string to send to the server
      System.out.println("ServiceBase: Output Pipe Ready - sending a message to the pipe now!");
      OutputPipe myPipe = evt.getOutputPipe();
	    String data = respondLogic();

	    // create the pipe message
	    msg = pipes.createMessage();


           try {
	    // store the client data into the message with the requestDataTag
            ByteArrayInputStream ip = new ByteArrayInputStream(data.getBytes());
            MessageElement myElem =
            new MessageElementImpl(ServiceBase.requestDataTag,textXmlType,ip);
            ip.close();
            msg.addElement(myElem);
	    myPipe.send (msg);
            }
          catch (Exception ex) {
            System.out.println("ServiceBase: message creation failed");
            ex.printStackTrace();
           }


	    System.out.println("ServiceBase: response message \"" + data + "\" sent back to the client");

    }

    public DocMechanix getDocMechanix() {
    return instanceDocm;
    }
    public GroupMechanix getGroupMechanix() {
    return instanceGroupm;;
    }
    public MessageMechanix getMessageMechanix() {
    return instanceMessagem;
    }
    public PeerMechanix getPeerMechanix() {
    return instancePeerm;
    }
    public PersistenceMechanix getPersistenceMechanix() {
    return instancePersistencem;
    }

    public PipeMechanix getPipeMechanix() {
    return instancePipem;
    }


}

