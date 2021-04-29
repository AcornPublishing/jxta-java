package net.jxta.impl.shell.bin.sstest;

import com.wrox.ea.jxtaservice.ClientBase;
import net.jxta.peergroup.PeerGroup;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class sstest extends ShellApp {

    ShellEnv env;

    private ClientBase myClient = null;


    public sstest() {
    }

    public void stopApp () {
    }

    public int startApp (String[] args) {

        env = getEnv();

        // get the std group
        ShellObject obj = env.get("stdgroup");

        PeerGroup group = (PeerGroup)obj.getObject();


           myClient =  new ClientBase("SimpleService", false);

          myClient.init(group);
          myClient.sendRequestToService("This is request for the client");
        return ShellApp.appNoError;
    }

    public void help() {
        println( "NAME" );
        println( "     sstest  - simple service test" );
        println( " " );
        println( "SYNOPSIS" );
        println( "     sstest");

    }


}