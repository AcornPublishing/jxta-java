package com.wrox.ea.jxtaservice;
import net.jxta.peergroup.*;
/**
 * Title:        JXTA EZEL
 * Description:  JXTA EZEL
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class GroupMechanix {
  PeerGroup internGroup = null;
  public GroupMechanix(PeerGroup gp) {
      internGroup = gp;
  }

  public PeerGroup createJXTAPeergroup() {
  return null;
  }
  public String seekAndJoin(PeerGroup inGroup) {
  return "";
  }
  public String leave(PeerGroup inGroup) {
  return "";
  }

}