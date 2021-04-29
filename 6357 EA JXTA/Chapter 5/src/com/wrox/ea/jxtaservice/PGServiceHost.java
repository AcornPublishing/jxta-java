package com.wrox.ea.jxtaservice;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.exception.PeerGroupException;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class PGServiceHost {
  protected PeerGroup group = null;
  public PGServiceHost() {
  }

  public PeerGroup getGroup() {
  return group;
  }
  public void waitForRequests() {
   synchronized(this) {
        try {
         wait(); } catch (Exception ex) {}
        }
  }
  public void init() {
  startJxta();
  }

   private void startJxta() {
        try {

            group = PeerGroupFactory.newNetPeerGroup();

        } catch (PeerGroupException e) {

            System.out.println("PGServiceHost: group creation failure");
            e.printStackTrace();
            System.exit(1);
        }

    }


 }




