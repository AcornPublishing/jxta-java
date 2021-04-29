package com.wrox.ea.jxtashare;

import java.io.File;
import java.io.FileInputStream;

import java.util.Vector;
import java.util.Iterator;

import net.jxta.document.Advertisement;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;

import net.jxta.share.client.CachedListContentRequest;
import net.jxta.share.client.GetContentRequest;
import net.jxta.share.CMS;
import net.jxta.share.Content;
import net.jxta.share.ContentManager;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.ContentId;
import net.jxta.share.ContentIdImpl;

/**
 * Title:        WroxShare
 * Description:  WroxShare
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */

public class FinderThread extends Thread {
  private static String DISC_FILTER_PATTERN = "";
  private static String PEER_PREMIERE = null;
  private static String WROX_BASEDIR = "WROX_SHARE";
  private static final String shareDirName = "sharedir";
  private static final int WAIT_TIME = 10 * 1000;  // 10 secs
  private PeerGroup group = null;

  private boolean doQuit = false;
  private static Vector results = new Vector();
  private ListRequestor listReq = null;

  private static Vector filesToFetch = new Vector();
  private SearchListener listener = null;
  private static boolean inSearch = false;
  private static CMS cms = null;

  public FinderThread(PeerGroup grp) {
     doQuit = false;
     group = grp;
  }
  public boolean isInSearch() {
     return inSearch;
  }
  public void cancel() {
    this.doQuit = true;
  }

   private void shareLocal() {
    System.out.println("*** SHARE LOCAL*****");
    File shareDir = new File(shareDirName);
    ContentManager cmgr = cms.getContentManager();

    if ((null != shareDir) && ( shareDir.isDirectory()) ){
       File [] filesToShare = shareDir.listFiles() ;
       try {
       for (int i=0; i< filesToShare.length; i++)  {
          System.out.println("trying " + filesToShare[i].getAbsolutePath());

          ContentIdImpl mycid = new ContentIdImpl(new FileInputStream(filesToShare[i]));

          Content [] match = cmgr.getContent(mycid);
          if (0 == match.length) {
                 cmgr.share(filesToShare[i]);
                 System.out.println("*** SHARED 1 FILE -- " + filesToShare[i].getAbsolutePath());
             }
          }// of for
         } catch (Exception ex) { ex.printStackTrace();}
    }
   }


  public void run() {
      int waitTime = WAIT_TIME;
      cms = getCMSinstance(this.group);  // get CMS singleton instance
      shareLocal();  // share all the local files

      while (!doQuit) {
          getLocalFiles();
          if (!inSearch)
              checkSharedFiles();

          try {
                Thread.sleep(  waitTime );
             } catch (InterruptedException e) {
	     } // of try
          transferFiles();
        }  // of while !doQuit
    }  // of run


  private void getLocalFiles() {
     System.out.println("****  GET LOCAL****");
     ContentManager contentManager = cms.getContentManager();
     Content[] foundContent = contentManager.getContent();
     System.out.println("found " + foundContent.length + " files");
     ContentAdvertisement tpAdv;
     if( foundContent != null){
         synchronized(results) {
              for (int i=0; i<foundContent.length; i++) {
                   tpAdv = foundContent[i].getContentAdvertisement();
                   System.out.println("trying to add one");
	           SearchResult searchResult = new SearchResult(
                       tpAdv);
		   if ( searchResult != null ) {
                      results.addElement( searchResult );
                      System.out.println("added one!");
                    }
	       }
              } // of synchronized

          if (null != listener) {
             listener.searchUpdate();
              }
           } // of if
   }


    private void checkSharedFiles() {
      clearListReq();
      listReq = new ListRequestor(group,DISC_FILTER_PATTERN);
      inSearch = true;
      listReq.activateRequest();
      System.out.println("*** starting search now *****");
     }  // of checkSharedFiles

   public void clearListReq() {
      if (null != listReq) {
          listReq.cancel();
        }
    }
    public void setListener(SearchListener l) {
        listener = l;
    }

    public void resetListener(SearchListener l) {
        listener = null;
    }

// wrapper class, handling the notification and propagate
// back to listener - *must not block*
   class ListRequestor  extends CachedListContentRequest{
                     public ListRequestor( PeerGroup group, String inSubStr ) {
                        super( group, inSubStr );
                }


public void notifyMoreResults() {
     System.err.println("..*..*.. notifyMORE");
     ContentAdvertisement[] foundContent = this.getResults();
     if( foundContent != null){
             synchronized(results) {
               for (int i=0; i<foundContent.length; i++) {
	           SearchResult searchResult = new SearchResult(
                       foundContent[i]);
               if ( searchResult != null )
                   results.addElement( searchResult );
	       }
              } // of synchronized
	    }

            // notify the listener

            if (null != listener) {
                listener.searchUpdate();
            }
             inSearch = false;
             System.out.println("** Search succeed ****");
        }

        public void notifyFailure() {
           System.err.println("..*..*.. notify  FAILURE");
           inSearch = false;
           System.out.println("** Search FAILED ****");
          }  //of notifyFailure
  } // of class


    public SearchResult[] grabResults() {
        SearchResult[] res = new SearchResult[results.size()];
        results.copyInto(res);

        return res;
    }

    public static CMS getCMSinstance(PeerGroup group) {

                if ( null == cms ) {
                        cms = new CMS();

                        try {
                                cms.init( group,null, group.getImplAdvertisement() );
                        } catch ( PeerGroupException e ) {
                                e.printStackTrace();
                        }

                        cms.startApp( new File( getGroupDir( group ), CMS.DEFAULT_DIR ) );
                }


                return cms;

    }

        private static File getGroupDir(PeerGroup grp) {
           File dir = null;

           // make sure the root directory exists
           File rootDir = new File(WROX_BASEDIR);
           if (!rootDir.exists()) {
               rootDir.mkdir();
            }
           // create a unique subdir per group
           dir = new File(rootDir,grp.getPeerGroupID().getUniqueValue().toString());
           if (!dir.exists()) {
                dir.mkdir();
            }

        return dir;
       }






    public void transferFiles() {
      if (filesToFetch.size() > 0) {
        ContentManager cmgr = cms.getContentManager();
        Iterator it = filesToFetch.iterator();
        while (it.hasNext())   {
         SearchResult sr = (SearchResult) it.next();
         ContentAdvertisement tpAdv = sr.getContentAdvertisement();
         System.err.println(".....requesting content");
         GetContentRequest req = new GetContentRequest(group,
            tpAdv, new File(shareDirName + File.separator +
            tpAdv.getName()) );
          } // of while
          filesToFetch.removeAllElements();
      } // of if
    }

    public void fetchFile(SearchResult inRes) {
       filesToFetch.add(inRes);
    }
  }


