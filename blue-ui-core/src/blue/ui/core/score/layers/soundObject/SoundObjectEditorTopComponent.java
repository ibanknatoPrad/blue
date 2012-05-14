/*
 * blue - object composition environment for csound Copyright (c) 2000-2009
 * Steven Yi (stevenyi@gmail.com)
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.LIB. If not, write to the Free
 * Software Foundation Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307
 * USA
 */

package blue.ui.core.score.layers.soundObject;

import blue.plugin.BluePlugin;
import blue.event.SelectionEvent;
import blue.event.SelectionListener;
import blue.soundObject.Instance;
import blue.soundObject.SoundObject;
import blue.soundObject.editor.SoundObjectEditor;
import blue.ui.core.BluePluginManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final public class SoundObjectEditorTopComponent extends TopComponent implements SelectionListener {

    private static SoundObjectEditorTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "SoundObjectEditorTopComponent";

    JPanel editPanel = new JPanel();

    CardLayout cardLayout = new CardLayout();

    SoundObject currentSoundObject;

    HashMap<Class, Class> sObjEditorMap = new HashMap<Class, Class>();

    HashMap<Class, SoundObjectEditor> editors = new HashMap<Class, SoundObjectEditor>();

    JPanel emptyPanel = new JPanel();
    
    private SoundObjectEditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SoundObjectEditorTopComponent.class, "CTL_SoundObjectEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(SoundObjectEditorTopComponent.class, "HINT_SoundObjectEditorTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        emptyPanel.setMinimumSize(new Dimension(0,0));
        this.setLayout(new BorderLayout());
        editPanel.setLayout(cardLayout);
        this.add(editPanel, BorderLayout.CENTER);
        editPanel.add(emptyPanel, "none");

        ArrayList<BluePlugin> plugins = BluePluginManager.getInstance().getPlugins(SoundObjectEditor.class);

        for(BluePlugin plugin : plugins) {
            sObjEditorMap.put((Class)plugin.getProperty(BluePlugin.PROP_EDIT_CLASS),
                    plugin.getPluginClass());
        }

        setEditingLibraryObject(null);

        SoundObjectSelectionBus.getInstance().addSelectionListener(this);

        selectionPerformed(SoundObjectSelectionBus.getInstance().getLastSelectionEvent());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.CardLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized SoundObjectEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new SoundObjectEditorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SoundObjectEditorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SoundObjectEditorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SoundObjectEditorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SoundObjectEditorTopComponent) {
            return (SoundObjectEditorTopComponent) win;
        }
        Logger.getLogger(SoundObjectEditorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return SoundObjectEditorTopComponent.getDefault();
        }
    }

      /*
     * (non-Javadoc)
     *
     * @see blue.event.SelectionListener#selectionPerformed(blue.event.SelectionEvent)
     */
    public void selectionPerformed(SelectionEvent e) {
        if(e == null) {
            editSoundObject(null);
            return;
        }

        int selectionType = e.getSelectionType();

//        System.err.println("SoundObject selected: " + e.getSelectedItem());

        if (selectionType == SelectionEvent.SELECTION_SINGLE) {
            SoundObject sObj = (SoundObject) e.getSelectedItem();
            editSoundObject(sObj);

            if(sObj instanceof Instance) {
                setEditingLibraryObject(SelectionEvent.SELECTION_LIBRARY);
            } else {
                setEditingLibraryObject(e.getSelectionSubType());
            }
        } else {
            editSoundObject(null);
        }

    }

    public void setEditingLibraryObject(Object objectType) {
        String name = NbBundle.getMessage(SoundObjectEditorTopComponent.class, "CTL_SoundObjectEditorTopComponent");

        if(objectType == SelectionEvent.SELECTION_LIBRARY) {
            name += " - Library";
        } else if (objectType == SelectionEvent.SELECTION_BLUE_LIVE) {
            name += " - blue Live";
        }

        setName(name);
//        this.libraryEditLabel.setVisible(isLibaryObject);
    }

    public void editSoundObject(SoundObject sObj) {
        if (currentSoundObject == sObj) {
            return;
        }

        currentSoundObject = sObj;

        cardLayout.show(editPanel, "none");
        if (sObj == null) {
            // JOptionPane.showMessageDialog(null, "yo");
            return;
        }

        SoundObject sObjToEdit = sObj;

        if (sObj instanceof Instance) {
            sObjToEdit = ((Instance) sObj).getSoundObject();
            this.setEditingLibraryObject(SelectionEvent.SELECTION_LIBRARY);
        }

        Class sObjClass = sObjToEdit.getClass();
        Class sObjEditClass = null;
        
        for(Class c : sObjEditorMap.keySet()) {
            if(c.isAssignableFrom(sObjClass)) {
                sObjEditClass = sObjEditorMap.get(c);
                break;
            }
        }

        if(sObjEditClass == null) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor(
                    "Could not find editor for SoundObject of type: " + sObjClass.getCanonicalName(),
                    "Error",
                    NotifyDescriptor.DEFAULT_OPTION,
                    NotifyDescriptor.ERROR_MESSAGE, null, null));
            return;
        }

        SoundObjectEditor sObjEditor = editors.get(sObjEditClass);

        if(sObjEditor == null) {
            try {
                sObjEditor = (SoundObjectEditor) sObjEditClass.newInstance();
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
            editors.put(sObjEditClass, sObjEditor);
            editPanel.add(sObjEditor, sObjEditClass.getName());
        }
        cardLayout.show(editPanel, sObjEditClass.getName());
        sObjEditor.editSoundObject(sObjToEdit);
        
//        Logger.getLogger(SoundObjectEditorTopComponent.class.getName()).fine("SoundObject Selected: " + className);;
    }
}