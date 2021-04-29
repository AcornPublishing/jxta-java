package com.wrox.ea.jxtaservice;
import net.jxta.pipe.*;
import net.jxta.peergroup.*;
import java.util.*;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */
public class PipeMechanix {
  private PeerGroup intGroup = null;
  public PipeMechanix(PeerGroup gp) {
      intGroup = gp;
  }
  public Enumeration getAppPipes() {
  return null;
  }
  public String findPipe() {
  return "";
  }
  public PipeService createNewPipe() {
  return null;
  }
  public void addAppPipe(PipeService ps, String desc) {
  }
}