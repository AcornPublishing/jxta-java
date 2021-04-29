package net.jxta.impl.shell.bin.qrytest;


import net.jxta.peergroup.PeerGroup;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.resolver.QueryHandler;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.impl.protocol.ResolverResponse;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class qrytest extends ShellApp {
ShellEnv env;



  public qrytest() {
  }

  public void stopApp () {
    }

    public int startApp (String[] args) {

        env = getEnv();

        // get the std group
        ShellObject obj = env.get("stdgroup");

        PeerGroup group = (PeerGroup)obj.getObject();

      try {
        ResolverQuery query = new ResolverQuery("SimpleGroupRevealed" +
						group.getPeerGroupID().getUniqueValue().toString(),
						null,
						group.getPeerID().toString(),
						"just a query string",
						100);

        group.getResolverService().sendQuery(null, query);
            } catch ( Exception ex ) {
               ex.printStackTrace();
            }
        return ShellApp.appNoError;
    }

    public void help() {
        println( "NAME" );
        println( "     qrytest - test of query propagation" );
        println( " " );
        println( "SYNOPSIS" );
        println( "     qrytest");

    }






}