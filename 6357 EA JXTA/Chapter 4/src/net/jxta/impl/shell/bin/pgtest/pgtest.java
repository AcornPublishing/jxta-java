package net.jxta.impl.shell.bin.pgtest;
import com.wrox.ea.jxtaservice.PGClientBase;

import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.peergroup.PeerGroup;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class pgtest extends ShellApp  {
    ShellEnv env;
  private PGClientBase myClient;

  public pgtest() {
  }
    public void stopApp () {
    }

    public int startApp (String[] args) {

        env = getEnv();

        // get the std group
        ShellObject obj = env.get("stdgroup");

        PeerGroup group = (PeerGroup)obj.getObject();
        myClient = new PGClientBase("SimpleGroupService");
        myClient.init(group);
        myClient.sendMessageToService("Client and service in an unbreakable link!");

        return ShellApp.appNoError;
    }

    public void help() {
        println( "NAME" );
        println( "     pgtest - peergroup service test" );
        println( " " );
        println( "SYNOPSIS" );
        println( "     pgtest");

    }


}




