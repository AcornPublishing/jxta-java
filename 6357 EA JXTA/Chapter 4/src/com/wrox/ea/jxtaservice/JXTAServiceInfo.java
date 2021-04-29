package com.wrox.ea.jxtaservice;

/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class JXTAServiceInfo {
   private String Name = null;
    private String Description = null;
    private String Version = null;
    private String Creator = null;
    private String URI = null;
    private String PersistedPipeAdv = null;

    private boolean hasResponsePipe = false;


  public JXTAServiceInfo(String name,String desc, String ver, String creat,
     String uri, String pipe) {
     setVars(name, desc, ver, creat, uri, pipe);


  }

   public JXTAServiceInfo(String name,String desc, String ver, String creat,
     String uri, String pipe,  boolean hasResp) {

     hasResponsePipe = hasResp;

     setVars(name, desc,ver,creat,uri,pipe);
  }

    private void setVars(String name,String desc, String ver, String creat,
     String uri, String pipe) {
     Name = name;
     Description = desc;
     Version = ver;
     Creator = creat;
     URI = uri;
     PersistedPipeAdv = pipe;



  }

  public String getName() { return Name; }
  public String getDescription() { return Description; }
  public String getVersion() { return Version; }
  public String getCreator() { return Creator; }
  public String getURI() { return URI; }
  public String getPersistedPipeAdv() { return PersistedPipeAdv; }
  public boolean hasResponse() { return hasResponsePipe; }
}