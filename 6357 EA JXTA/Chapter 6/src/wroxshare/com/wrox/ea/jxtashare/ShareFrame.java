package com.wrox.ea.jxtashare;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;



import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.exception.PeerGroupException;


/**
 * Title:        WroxShare
 * Description:  WroxShare
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */



class ShareFrame extends JFrame{

  static SharedListModel targetModel = null;
  private String pname = null;
  private PeerGroup group = null;

   private void startJxta() {
        try {
            // create, and Start the default jxta NetPeerGroup
            group = PeerGroupFactory.newNetPeerGroup();
        } catch (PeerGroupException e) {

            System.out.println("ShareFrame : group creation failure");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public ShareFrame() {
      super("Wrox JXTAShare");
      System.out.println ("Starting jxta ....");
      startJxta();
      System.out.println ("Starting UI....");

      SharedListModel availModel = null;
      ShareList availList = new ShareList();
      if (null != group) {
      availModel = new SharedListModel(group);
      }  else {
      System.out.println("ShareFrame:  group must be initialized before building UI");
      System.exit(1);
      }
      JPanel availPanel = getListPanel(availList, "Available Shares", (ListModel)availModel);

      ShareList napList = new ShareList();
      DefaultListModel napModel = new DefaultListModel();
      JPanel napPanel = getListPanel(napList, "Napped Shares", napModel);

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
      mainPanel.add( availPanel );
      mainPanel.add( napPanel );

      getContentPane().add( mainPanel );
      setSize (500, 300);
      addWindowListener (new WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        exitFrame();
      }
     });
    setVisible (true);

  }


  private JPanel getListPanel(ShareList list, String labelName, ListModel listModel ){
        JPanel listPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(list);

        list.setModel(listModel);
        JLabel nameListName = new JLabel(labelName );

        listPanel.setLayout( new BorderLayout());
        listPanel.add(nameListName, BorderLayout.NORTH);
        listPanel.add( scrollPane, BorderLayout.CENTER);

        return listPanel;
  }





   // in ShareFrame class
   private void exitFrame () {
       this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

       public static void main(String args[]) {
        ShareFrame myapp = new ShareFrame();

    }

}

