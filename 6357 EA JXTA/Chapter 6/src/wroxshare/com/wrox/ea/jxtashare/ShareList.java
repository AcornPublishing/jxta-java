package com.wrox.ea.jxtashare;


import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DragGestureListener;
import java.awt.datatransfer.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;


import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;


import javax.swing.JList;
import javax.swing.DefaultListModel;


/**
 * Title:        WroxShare
 * Description:  WroxShare
 * Copyright:    Copyright (c) 2001
 * Company:      mW
 * @author       Sing Li
 * @version      1.0
 */


public class ShareList extends JList
    implements  DropTargetListener,DragSourceListener, DragGestureListener    {
    DropTarget dropTarget = null;
    DragSource dragSource = null;


    public ShareList() {
      dropTarget = new DropTarget (this, this);
      dragSource = new DragSource();
      dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
    }

    public void dragEnter (DropTargetDragEvent event) {
      event.acceptDrag (DnDConstants.ACTION_MOVE);
    }

    public void dragExit (DropTargetEvent event) {
    }

    public void dragOver (DropTargetDragEvent event) {
    }


    public void drop (DropTargetDropEvent event) {
       if (this.getModel() instanceof SharedListModel) {
          event.rejectDrop();
          return;
        }
      try {
         Transferable transferable = event.getTransferable();
        // we accept only Strings
        if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor)){
            event.acceptDrop(DnDConstants.ACTION_MOVE);
            String s = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
            addElement( s );
            event.getDropTargetContext().dropComplete(true);
           }
        else{
            event.rejectDrop();
             }
          }
      catch (Exception ex) {
        ex.printStackTrace();
        event.rejectDrop();
       }

  }



  public void dropActionChanged ( DropTargetDragEvent event ) {
  }


  public void dragGestureRecognized( DragGestureEvent event) {
        SharedListModel sm;

        if (!(this.getModel() instanceof SharedListModel))
           return;
        else
           sm = (SharedListModel) this.getModel();

    Object selected = getSelectedValue();
    if ( selected != null ){
        StringSelection text = new StringSelection( selected.toString());
        sm.setFileToTransfer(selected);
        dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
    } else {
    }
  }


  public void dragDropEnd (DragSourceDropEvent event) {
    if ( event.getDropSuccess()){
    }
  }


  public void dragEnter (DragSourceDragEvent event) {
  }

  public void dragExit (DragSourceEvent event) {

  }

  public void dragOver (DragSourceDragEvent event) {

  }


  public void dropActionChanged ( DragSourceDragEvent event) {
  }


   public void addElement( Object s ){
        (( DefaultListModel )getModel()).addElement (s.toString());
  }


  public void removeElement(){
    (( DefaultListModel)getModel()).removeElement( getSelectedValue());
  }

}
