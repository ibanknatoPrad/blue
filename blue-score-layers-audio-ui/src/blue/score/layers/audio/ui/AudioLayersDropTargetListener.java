/*
 * blue - object composition environment for csound
 * Copyright (c) 2000-2004 Steven Yi (stevenyi@gmail.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */

package blue.score.layers.audio.ui;

import blue.BlueSystem;
import blue.score.TimeState;
import blue.soundObject.AudioFile;
import blue.soundObject.PolyObject;
import blue.utility.SoundFileUtilities;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Steven Yi
 */
public class AudioLayersDropTargetListener implements DropTargetListener {

    DropTarget target;

    private AudioLayersPanel sTimeCanvas;
    
    TimeState timeState;

    public AudioLayersDropTargetListener(AudioLayersPanel sTimeCanvas) {
        this.sTimeCanvas = sTimeCanvas;
        target = new DropTarget(sTimeCanvas, this);
    }

    public void setTimeState(TimeState timeState) {
        this.timeState = timeState;
    }
    
    

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        boolean isFile = dtde
                .isDataFlavorSupported(DataFlavor.javaFileListFlavor);

        if (!isFile) {
            if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                isFile = true;
            }
        }

        if (isFile) {
            dtde.acceptDrag(DnDConstants.ACTION_LINK);
        } else {
            dtde.rejectDrag();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();

            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_LINK);

                List list = (List) tr
                        .getTransferData(DataFlavor.javaFileListFlavor);

                if (list.size() != 1) {
                    dtde.dropComplete(false);
                    return;
                }

                String s = list.get(0).toString().trim();

                System.out.println("file flavor found: " + s);
                if (!s.toLowerCase().endsWith("wav")
                        && !s.toLowerCase().endsWith("aif")
                        && !s.toLowerCase().endsWith("aiff")) {

                    dtde.dropComplete(false);
                    return;
                }

                String sObjName = s
                        .substring(s.lastIndexOf(File.separator) + 1);

                Point p = dtde.getLocation();

//                int index = sTimeCanvas.pObj.getLayerNumForY(p.y);
//
//                AudioFile af = new AudioFile();
//                af.setName(sObjName);
//                af.setSoundFileName(BlueSystem.getRelativePath(s));
//
//                PolyObject pObj = sTimeCanvas.getPolyObject();
//
//                float startTime = (float) p.x / timeState.getPixelSecond();
//                float dur = SoundFileUtilities.getDurationInSeconds(s);
//
//                af.setStartTime(startTime);
//                af.setSubjectiveDuration(dur);
//
//                pObj.addSoundObject(index, af);
//
                dtde.dropComplete(true);
                return;
            } else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_LINK);

                String str = (String) tr
                        .getTransferData(DataFlavor.stringFlavor);

                System.out.println("String flavor found: " + str);
                if (!str.startsWith("file://")) {
                    dtde.dropComplete(false);
                    return;
                }

                str = str.substring(7).trim();

                if (!str.toLowerCase().endsWith("wav")
                        && !str.toLowerCase().endsWith("aif")
                        && !str.toLowerCase().endsWith("aiff")) {
                    System.err.println("Could not open file: " + str);
                    dtde.dropComplete(false);
                    return;
                }

                str = URLDecoder.decode(str);
                str = str.replaceAll(" ", "\\ ");

                File f = new File(str);

                if (!f.exists()) {
                    dtde.dropComplete(false);
                    return;
                }

                String sObjName = str
                        .substring(str.lastIndexOf(File.separator) + 1);

                Point p = dtde.getLocation();

//                int index = sTimeCanvas.pObj.getLayerNumForY(p.y);

//                AudioFile af = new AudioFile();
//                af.setName(sObjName);
//                af.setSoundFileName(str);
//
//                PolyObject pObj = sTimeCanvas.getPolyObject();
//
//                float startTime = (float) p.x / timeState.getPixelSecond();
//                float dur = SoundFileUtilities.getDurationInSeconds(str);
//
//                af.setStartTime(startTime);
//                af.setSubjectiveDuration(dur);
//
//                pObj.addSoundObject(index, af);

                dtde.dropComplete(true);
                return;
            }
            dtde.rejectDrop();
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
            dtde.rejectDrop();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
     */
    @Override
    public void dragExit(DropTargetEvent dte) {
        // TODO Auto-generated method stub

    }
}