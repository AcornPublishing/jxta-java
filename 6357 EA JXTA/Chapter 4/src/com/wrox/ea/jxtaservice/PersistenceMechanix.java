package com.wrox.ea.jxtaservice;
import net.jxta.document.*;
import net.jxta.protocol.*;
import net.jxta.pipe.*;
import java.io.*;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.*;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class PersistenceMechanix {
  protected PeerGroup currentGroup = null;
  public PersistenceMechanix(PeerGroup group) {
     currentGroup = group;
  }
  public void changeGroup(PeerGroup group) {
    currentGroup = group;
    }
  public void PersistDoc(StructuredDocument doc) {
  }
  public void PersistAdv(Advertisement ad) {
  }
  public PipeAdvertisement CreatePipeAdvIfNotExist(String pipeName, String fileName) {

            System.out.println("Trying to read " + fileName);
	    PipeAdvertisement pipeAdv = null;
            FileInputStream is = null;
            FileOutputStream os = null;
            Document myDoc = null;
	    try {
		 is = new FileInputStream(fileName);
            pipeAdv = (PipeAdvertisement)
                    AdvertisementFactory.newAdvertisement(
                    new MimeMediaType("text/xml"), is);
            is.close();
	    } catch (Exception e) {
               System.out.println("cannot read " + fileName + ", now creating new one.");
               try {
                pipeAdv = (PipeAdvertisement)
                    AdvertisementFactory.newAdvertisement(
                    PipeAdvertisement.getAdvertisementType());

                      //PipeID pipeID = IDFactory.newPipeID();
                      PipeID pipeID = IDFactory.newPipeID((PeerGroupID) currentGroup.getPeerGroupID());
                      pipeAdv.setPipeID(pipeID);
                pipeAdv.setName(pipeName);
                pipeAdv.setType(PipeService.UnicastType);
		System.out.println("... created new pipe Advertisment....");
		 myDoc = pipeAdv.getDocument(new MimeMediaType("text/xml"));

                   os = new FileOutputStream(fileName);
		  myDoc.sendToStream(os);
                  os.flush();
                  os.close();
                  System.out.println("new pipe advertisement peristed!");
                  }
                  catch (Exception ex) {
                     System.out.println("PersistenceMechanix: problem while persisting");
                    ex.printStackTrace();
                    }
	    }

  return pipeAdv;
  }
}