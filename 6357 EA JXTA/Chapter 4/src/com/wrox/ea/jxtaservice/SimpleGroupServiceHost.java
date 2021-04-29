package com.wrox.ea.jxtaservice;
import net.jxta.peergroup.PeerGroup;
import net.jxta.exception.PeerGroupException;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class SimpleGroupServiceHost extends PGServiceHost {

  public SimpleGroupServiceHost() {
  }



  private static SimpleGroupService serviceInstance = null;
  public static SimpleGroupService getInstance( PeerGroup group) {

                if ( null == serviceInstance ) {
                        serviceInstance = new SimpleGroupService("SimpleGroupService");

                        try {
                                serviceInstance.init( group, null, null );
                        } catch ( PeerGroupException e ) {
                                e.printStackTrace();
                        }

                       serviceInstance.startApp( null) ;
                }



                return serviceInstance;
        }

    public static void main(String args[]) {
        SimpleGroupServiceHost myHost = new SimpleGroupServiceHost();
        myHost.init();
        SimpleGroupService myapp = myHost.getInstance(myHost.getGroup());
        System.out.println ("Simple GROUP service starting...");
        myHost.waitForRequests();


        System.exit(0);
    }

}