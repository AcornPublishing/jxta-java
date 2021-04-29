package net.jxta.impl.shell.bin.pgrvtest;
import com.wrox.ea.jxtaservice.PGClientBase;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.peergroup.PeerGroup;
import net.jxta.endpoint.Message;


/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class pgrvtest extends ShellApp  {
    ShellEnv env;
    private PGClientBase myClient;

  public pgrvtest() {
  }

    public void stopApp () {
    }

    public int startApp (String[] args) {

        env = getEnv();

        // get the std group
        ShellObject obj = env.get("stdgroup");

        PeerGroup group = (PeerGroup)obj.getObject();
        myClient = new PGClientBase("SimpleGroupRevealed");
        myClient.init(group);

        myClient.sendMessageToService("Now we see what's happening underneath!");

        return ShellApp.appNoError;
    }

    public void help() {
        println( "NAME" );
        println( "     pgrvtest - peergroup service REVEALED test" );
        println( " " );
        println( "SYNOPSIS" );
        println( "     pgrvtest");

    }

   public void tapIncomingMessage(Message msg) {
        System.out.println("GOT A REPLY MESSAGE");
        System.out.println("Message contains reply address of - " +
        msg.getString("address"));
      }





}