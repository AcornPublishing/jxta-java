package net.jxta.impl.shell.bin.rptest;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import com.wrox.ea.jxtaservice.ClientBase;
import net.jxta.peergroup.PeerGroup;
import net.jxta.endpoint.MessageElement;
import java.io.InputStream;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class rptest extends ShellApp {

    ShellEnv env;



    private ClientBase myClient = null;


    public rptest() {
    }

    public void stopApp () {
    }

    public int startApp (String[] args) {

        env = getEnv();

        // get the std group
        ShellObject obj = env.get("stdgroup");

        PeerGroup group = (PeerGroup)obj.getObject();

           myClient =  new ClientBase("RespondService", true) {

         protected void processResponse(MessageElement payload) {
            System.out.println("Response received...");
             try {
            InputStream  is = payload.getStream();
            while (is.available() > 0) {
             System.out.write(is.read());
            }
            System.out.flush();
            is.close();
            } catch (Exception ex) {}
           }
           };

          myClient.init(group);
          myClient.sendRequestToService("Request from client");
        return ShellApp.appNoError;
    }

    public void help() {
        println( "NAME" );
        println( "     rptet - respondService test" );
        println( " " );
        println( "SYNOPSIS" );
        println( "    rptest");

    }


}