package com.wrox.ea.jxtashare;

import javax.swing.AbstractListModel;
import java.util.Vector;
import net.jxta.peergroup.PeerGroup;


/**
 * Title:        WroxShare
 * Description:  WroxShare
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class SharedListModel extends AbstractListModel implements SearchListener {
  SearchResult [] foundSharedItems = null;
  FinderThread backgroundFinder = null;
  Thread bkgThread = null;
  Vector listData = null;
  Vector displayData = null;


  public SharedListModel() {
    super();
  }

  public SharedListModel(PeerGroup pg) {
      super();

      listData = new Vector();
      backgroundFinder = new FinderThread(pg);
      backgroundFinder.setListener(this);

      bkgThread = new Thread(backgroundFinder);
      bkgThread.setPriority ( Thread.MAX_PRIORITY -1  );
      bkgThread.start();
  }

  public int getSize() {
    /**@todo: implement this javax.swing.AbstractListModel abstract method*/
    int retval;
    if (null == displayData)
       retval = 1;
    else
       retval = displayData.size();
    return retval;
  }

  public Object getElementAt(int parm1) {
     System.out.println("****called element at with param=" + parm1);
     if (null == displayData)
         return ("searching, please wait .......");
     else
         return (  displayData.get(parm1));
  }

  // implementation of SearchListener
  public void searchUpdate() {
     boolean newAdditions = false;
     foundSharedItems = backgroundFinder.grabResults();
     int startIndex = listData.size();
     for (int i=0; i<foundSharedItems.length; i++)  {
          SearchResult tpRes = foundSharedItems[i];

          if (!listData.contains(tpRes)) {  // add it if not already in the list
             listData.add(tpRes);
             newAdditions = true;
           } //if
      } // of for
     if (newAdditions) {
         this.displayData = (Vector) this.listData.clone();
         this.fireIntervalAdded(this, startIndex, listData.size());
         System.err.println("**** called update with " + listData.size() + " items");
      }
     else
         System.err.println("**** called update but no new updates");
    } //of searchUpdate

 public void setFileToTransfer(Object inFile) {
       if (inFile instanceof SearchResult) {
         SearchResult tpRes = (SearchResult) inFile;
         backgroundFinder.fetchFile(tpRes);
       }
    }

  }
