/*
 * blue - object composition environment for csound
 * Copyright (c) 2000-2007 Steven Yi (stevenyi@gmail.com)
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
package blue.soundObject.editor.jmask;

import blue.soundObject.jmask.Random;
import javax.swing.SpinnerNumberModel;

/**
 * 
 * @author steven
 */
public class RandomEditor extends javax.swing.JPanel implements DurationSettable {

    Random random;

    /** Creates new form RandomEditor */
    public RandomEditor(final Random random) {
        this.random = random;

        initComponents();

        minSpinner.setModel(new SpinnerNumberModel(random.getMin(),
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0) {
            @Override
            public void setValue(Object value) {
                if ((value == null) || !(value instanceof Number)) {
                    throw new IllegalArgumentException("illegal value");
                }

                double val = ((Double) value).doubleValue();

                if (val > random.getMax()) {
                    throw new IllegalArgumentException("illegal value");
                }

                super.setValue(value);
            }
        });

        maxSpinner.setModel(new SpinnerNumberModel(random.getMax(),
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0) {
            @Override
            public void setValue(Object value) {
                if ((value == null) || !(value instanceof Number)) {
                    throw new IllegalArgumentException("illegal value");
                }

                double val = ((Double) value).doubleValue();

                if (val < random.getMin()) {
                    throw new IllegalArgumentException("illegal value");
                }

                super.setValue(value);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        minSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        maxSpinner = new javax.swing.JSpinner();

        jLabel1.setText("Random");

        jLabel2.setText("Min");

        minSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minSpinnerStateChanged(evt);
            }
        });

        jLabel3.setText("Max");

        maxSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maxSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {maxSpinner, minSpinner});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(minSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(maxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void maxSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_maxSpinnerStateChanged
        double val = ((Double) maxSpinner.getValue()).doubleValue();

        if (val >= random.getMin()) {
            random.setMax(val);
        }
    }// GEN-LAST:event_maxSpinnerStateChanged

    private void minSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_minSpinnerStateChanged
        double val = ((Double) minSpinner.getValue()).doubleValue();

        if (val <= random.getMax()) {
            random.setMin(val);
        }
    }// GEN-LAST:event_minSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSpinner maxSpinner;
    private javax.swing.JSpinner minSpinner;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setDuration(double duration) {
        //ignore
    }

}