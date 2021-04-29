package com.wrox.ea.jxtashare;


import net.jxta.share.ContentAdvertisement;


/**
 * Title:        WroxShare
 * Description:  WroxShare
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class SearchResult extends Object {

    public ContentAdvertisement contentAdv = null;

    SearchResult(
    ContentAdvertisement inContent) {
        contentAdv = inContent;
    }

    public ContentAdvertisement getContentAdvertisement(){
	return contentAdv;
    }
  public String toString() {
       return (contentAdv.getName() + "   Size: " +  contentAdv.getLength());
    }
  public boolean equals(Object inObj) {
     boolean retval = false;
    if (inObj instanceof SearchResult) {
      SearchResult tpSR = (SearchResult) inObj;

      retval =contentAdv.getContentId().equals(tpSR.getContentAdvertisement().getContentId()) ;

        if (retval)
            System.err.println("-- compared IS equal");
        else
            System.err.println("--- compared NOT equal");

    }
    return retval;
  }
    }

