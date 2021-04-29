package com.wrox.ea.jxtaservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;

import java.net.URL;

import net.jxta.pipe.PipeService;
import net.jxta.pipe.OutputPipeListener;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;

import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.document.Document;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;

import net.jxta.discovery.DiscoveryService;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryEvent;

import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;

import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleSpecAdvertisement;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

import net.jxta.impl.endpoint.MessageElementImpl;

import net.jxta.id.IDFactory;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public  class ClientBase implements DiscoveryListener, OutputPipeListener, PipeMsgListener  {
  static final String NAME_ATTRIBUTE = "Name";
  static final String PIPE_ADV_TAG ="jxta:PipeAdvertisement";
  static final String ID_TAG = "Id";
  static final String TYPE_TAG = "Type";

  static final String RESP_PIPE_TAG = ServiceBase.responsePipeTag;
  static final String CALL_TAG = ServiceBase.requestDataTag;

  private String ServiceName = null;
  private PeerGroup gp = null;
  private     DiscoveryService disco = null;
  private PipeService pipes = null;
  private String respTag = null;
  private String callTag = null;
  private boolean hasResp = false;
  private OutputPipe myPipe = null;
  private  Document respondInputPipeDoc = null;

  public ClientBase( String srvName,  boolean resp) {
  ServiceName = ServiceBase.MODULE_SPEC_PREFIX + srvName;
  hasResp = resp;
   }

  public void init(PeerGroup group) {
        gp = group;
        disco = gp.getDiscoveryService();
        pipes = gp.getPipeService();
        System.out.println("ClientBase: init...");

	System.out.println("ClientBase: Searching for the service advertisement");
	Enumeration enum = null;


	    try {

		enum = disco.getLocalAdvertisements(DiscoveryService.ADV,
					     NAME_ATTRIBUTE,
					     ServiceName);



		if ((enum != null) && enum.hasMoreElements()) {
                    processAdv(enum);
                return;
                }


		disco.getRemoteAdvertisements(null,
					 DiscoveryService.ADV,
					 NAME_ATTRIBUTE,
					 ServiceName,1, this);

            Thread.sleep(5000);   // delay for discovery

	    } catch (Exception e){
		// found nothing!  move on
	    }


  } // of testService()

  public void processAdv(Enumeration enum) {
	System.out.println("ClientBase: found the advertisement");
        ModuleSpecAdvertisement	 specAdv = (ModuleSpecAdvertisement) enum.nextElement();
	try {
          DocMechanix.dumpDoc(specAdv.getDocument(ServiceBase.textPlainType), System.out);
           } catch(Exception ex) {}

	    // we can extract the pipe information to connect to the service
	    // from the advertisement as part of the Param section of the advertisement
	    StructuredTextDocument paramDoc = (StructuredTextDocument)  specAdv.getParam();

	    // extract both the Pipe ID and the Pipe Type
	    String pID = null;
	    String pType = null;
            Enumeration docElements = paramDoc.getChildren();
            TextElement pipeAdvElem = null;
            while (docElements.hasMoreElements()) {

               pipeAdvElem = (TextElement) docElements.nextElement();
               String nm =pipeAdvElem.getName();
                  System.out.println("parsing <"+ nm + ">");

               if (nm.equals(PIPE_ADV_TAG))
                  break;
              }
	    Enumeration elements = pipeAdvElem.getChildren();
	    while (elements.hasMoreElements()) {
		TextElement elem = (TextElement) elements.nextElement();
		String nm = elem.getName();

                System.out.println("parsing <"+ nm + ">");

		 if(nm.equals(ID_TAG)) {
		     pID = elem.getTextValue();
		     continue;
		 }

		 if(nm.equals(TYPE_TAG)) {
		     pType = elem.getTextValue();
		     continue;
		 }

	    }

System.out.println("Pipe ID is " + pID);
          PipeAdvertisement pipeadv = (PipeAdvertisement)
		AdvertisementFactory.newAdvertisement(
		  PipeAdvertisement.getAdvertisementType());

	    try {
                URL pipeID = new URL(pID);
                pipeadv.setPipeID( (PipeID) IDFactory.fromURL( pipeID ) );
		pipeadv.setType(pType);
                } catch ( Exception badID ) {
		    badID.printStackTrace();
                    throw new IllegalArgumentException( "Bad pipe ID in advertisement" );
                }




	try {


            pipes.createOutputPipe(pipeadv, this);



	} catch (Exception ex) {

	    ex.printStackTrace();
	    System.out.println("Client: Error sending message to the service");
	}

    }
    public void discoveryEvent(DiscoveryEvent evt) {
     System.out.println("Remote discovery succeeds!");
     DiscoveryResponseMsg dmsg = evt.getResponse();
     if (dmsg.getResponseCount() > 0) {
       Enumeration enum = dmsg.getResponses();
       processAdv(enum);

       }


    }

protected void processResponse(MessageElement payload) {
 // override only if client has response pipe
}
public void pipeMsgEvent( PipeMsgEvent ev) {


    Message inReq = ev.getMessage();
    MessageElement coreMsg = inReq.getElement(ServiceBase.requestDataTag);
    if (coreMsg != null) {
      System.out.println("Clientbase: A response has been received!");
      processResponse(coreMsg);
     }
   }


public void outputPipeEvent(OutputPipeEvent evt) {

 PipeAdvertisement respPipeAdv = null;
 System.out.println("ClientBase: Output Pipe Created...");

      System.out.println("ClientBase: Output Pipe Ready - creating a response input pipe now!");
         respPipeAdv = (PipeAdvertisement)
		AdvertisementFactory.newAdvertisement(
		  PipeAdvertisement.getAdvertisementType());

	    try {

                respPipeAdv.setPipeID(  IDFactory.newPipeID(gp.getPeerGroupID() ) );
		respPipeAdv.setType("JxtaUnicast");
                pipes.createInputPipe(respPipeAdv, (PipeMsgListener) this);
               respondInputPipeDoc =    respPipeAdv.getDocument(ServiceBase.textXmlType);

                } catch ( Exception badID ) {
		    badID.printStackTrace();

                }
       myPipe = evt.getOutputPipe();
  }
public void sendRequestToService(String inReq) {

      if ( null != myPipe) {
	    // create the pipe message
	    Message msg = pipes.createMessage();


          ByteArrayInputStream dataIs = new ByteArrayInputStream (inReq.getBytes());

           try {

            InputStream ip = respondInputPipeDoc.getStream();
            msg.addElement(new MessageElementImpl(RESP_PIPE_TAG,ServiceBase.textXmlType,ip));
            msg.addElement(new MessageElementImpl(CALL_TAG, ServiceBase.textXmlType, dataIs));

	    // send the message to the service pipe
	    myPipe.send (msg);
            }
          catch (Exception ex) {}


	    System.out.println("ClientBase: message \"" + inReq + "\" sent to the Server");

      }
    }
}