/*
 * blue - object composition environment for csound Copyright (c) 2001-2017
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
package blue.soundObject.editor;

import blue.Arrangement;
import blue.BlueSystem;
import blue.CompileData;
import blue.Tables;
import blue.automation.LineColors;
import blue.automation.Parameter;
import blue.components.lines.Line;
import blue.components.lines.LineList;
import blue.gui.ExceptionDialog;
import blue.gui.InfoDialog;
import blue.jfx.BlueFX;
import blue.orchestra.editor.blueSynthBuilder.jfx.LineSelector;
import blue.orchestra.editor.blueSynthBuilder.jfx.LineView;
import blue.plugin.ScoreObjectEditorPlugin;
import blue.score.ScoreObject;
import blue.soundObject.NoteList;
import blue.soundObject.Sound;
import blue.soundObject.SoundObject;
import blue.ui.core.orchestra.editor.BlueSynthBuilderEditor;
import java.awt.BorderLayout;
import java.util.concurrent.CountDownLatch;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

/**
 * Editor for Sound SoundObject.
 *
 * @author steven yi
 * @version 1.0
 */
@ScoreObjectEditorPlugin(scoreObjectType = Sound.class)
public class SoundEditor extends ScoreObjectEditor {

    Sound sObj;

    JLabel editorLabel = new JLabel();

    JPanel topPanel = new JPanel();

    JButton testButton = new JButton();

    LineList lineList = new LineList();

    BlueSynthBuilderEditor editor = new BlueSynthBuilderEditor();

    LineView lineView;
    LineSelector lineSelector;

    public SoundEditor() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        this.setLayout(new BorderLayout());
        this.add(editor);
        editor.setLabelText("[ Sound ]");

        JFXPanel jfxPanel = new JFXPanel();
        CountDownLatch latch = new CountDownLatch(1);

        BlueFX.runOnFXThread(() -> {

            try {

                MenuButton btn = new MenuButton("Automations");

                BorderPane mainPane = new BorderPane();
                lineView = new LineView(lineList);
                lineSelector = new LineSelector(lineList);

                lineView.widthProperty().bind(mainPane.widthProperty());
                lineView.heightProperty().bind(mainPane.heightProperty().subtract(lineSelector.heightProperty()));

                lineView.selectedLineProperty().bind(lineSelector.selectedLineProperty());

                mainPane.setCenter(lineView);
                mainPane.setTop(new BorderPane(lineSelector, null, null, null, btn));

                btn.showingProperty().addListener((obs, old, newVal) -> {
                    if (newVal) {
                        if (sObj != null) {
                            sObj.getBlueSynthBuilder().getParameterList().sorted()
                                    .forEach((param) -> {
                                        MenuItem m = new MenuItem(param.getName());
                                        m.setOnAction(e -> {
                                            param.setAutomationEnabled(!param.isAutomationEnabled());
                                            if (param.isAutomationEnabled()) {
                                                param.getLine().setVarName(param.getName());
                                                lineList.add(param.getLine());
                                            } else {
                                                lineList.remove(param.getLine());
                                            }

                                            int colorCount = 0;
                                            for(Line line : lineList) {
                                                line.setColor(LineColors.getColor(colorCount++));
                                            }
                                        });
                                        if (param.isAutomationEnabled()) {
                                            m.setStyle("-fx-text-fill: green;");
                                        }
                                        btn.getItems().add(m);
                                    });
                        }
                    } else {
                        btn.getItems().clear();
                    }
                });

                final Scene scene = new Scene(mainPane);
                BlueFX.style(scene);
                jfxPanel.setScene(scene);
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        editor.getTabs().insertTab("Automation", null, jfxPanel, "", 1);

    }

    @Override
    public final void editScoreObject(ScoreObject sObj) {
        if (sObj == null) {
            this.sObj = null;
            editorLabel.setText("no editor available");
            editor.editInstrument(null);
            return;
        }

        if (!(sObj instanceof Sound)) {
            this.sObj = null;
            editorLabel.setText("no editor available");
            editor.editInstrument(null);

            return;
        }

        this.sObj = (Sound) sObj;
        editor.editInstrument(this.sObj.getBlueSynthBuilder());

        lineList.clear();
        int colorCount = 0;
        for (Parameter p : this.sObj.getBlueSynthBuilder().getParameterList().sorted()) {
            if (p.isAutomationEnabled()) {
                p.getLine().setVarName(p.getName());
                p.getLine().setColor(LineColors.getColor(colorCount++));
                lineList.add(p.getLine());
            }
        }
    }

    public final void testSoundObject() {
        if (this.sObj == null) {
            return;
        }

        NoteList notes = null;

        try {
            notes = ((SoundObject) this.sObj).generateForCSD(new CompileData(new Arrangement(), new Tables()), 0.0f, -1.0f);
        } catch (Exception e) {
            ExceptionDialog.showExceptionDialog(SwingUtilities.getRoot(this), e);
        }

        if (notes != null) {
            InfoDialog.showInformationDialog(SwingUtilities.getRoot(this),
                    notes.toString(), BlueSystem
                    .getString("soundObject.generatedScore"));
        }
    }
}
