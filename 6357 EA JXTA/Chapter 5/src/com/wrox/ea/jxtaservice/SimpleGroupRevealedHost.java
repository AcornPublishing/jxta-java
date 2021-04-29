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

public class SimpleGroupRevealedHost extends PGServiceHost {

  public SimpleGroupRevealedHost() {
  }
  private static SimpleGroupRevealed serviceInstance = null;
  public static SimpleGroupRevealed getInstance( PeerGroup group) {

                if ( null == serviceInstance ) {
                        serviceInstance = new SimpleGroupRevealed("SimpleGroupRevealed");

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
        SimpleGroupRevealedHost myHost = new SimpleGroupRevealedHost();
        myHost.init();
        SimpleGroupRevealed myapp = myHost.getInstance(myHost.getGroup());
        System.out.println ("Simple GROUP service REVEALED starting...");
        myHost.waitForRequests();


        System.exit(0);
    }

}