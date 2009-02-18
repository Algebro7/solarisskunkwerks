/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ssw.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;
import ssw.Options;
import ssw.components.Mech;
import ssw.filehandlers.*;
import ssw.print.Printer;

public class dlgOpen extends javax.swing.JDialog {
    private frmMain parent;
    private Options opts = new Options();
    private MechList list;

    /** Creates new form dlgOpen */
    public dlgOpen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = (frmMain) parent;
    }

    private void LoadMech() {
        MechListData Data = list.Get(tblMechData.convertRowIndexToModel(tblMechData.getSelectedRow()));
        try
        {
            XMLReader read = new XMLReader();
            Mech m = read.ReadMech(Data.getFilename());
            parent.CurMech = m;
            if (Data.isOmni()) {
                m.SetCurLoadout( Data.getConfig() );
            }

            parent.Prefs.put("LastOpenDirectory", Data.getFilename().substring(0, Data.getFilename().lastIndexOf("\\")));
            parent.Prefs.put("LastOpenFile", Data.getFilename().substring(Data.getFilename().lastIndexOf("\\")));

            parent.LoadMechIntoGUI();
            this.setVisible(false);

        } catch ( Exception e ) {
            javax.swing.JOptionPane.showMessageDialog( this.parent, e.getMessage() );
        }
    }

    private void Calculate() {
        txtSelected.setText("0 Units Selected for 0 BV and 0 C-Bills");

        int BV = 0;
        float Cost = 0;

        int[] rows = tblMechData.getSelectedRows();
        for ( int i=0; i < rows.length; i++ ) {
            MechListData data = list.Get(tblMechData.convertRowIndexToModel(rows[i]));
            BV += data.getBV();
            Cost += data.getCost();
        }

        txtSelected.setText(rows.length + " Units Selected for " + BV + " BV and " + Cost + " C-Bills");
    }

    public void LoadList() {
        list = new MechList();

        FileList fl = new FileList(FileCommon.GetSafeFilename(opts.SaveLoadPath));
        for ( int i=0; i <= fl.length()-1; i++ ) {
            File f = fl.getFiles()[i];
            try
            {
                list.Add(f);
            } catch (Exception e) {

            }
        }

        if (list.Size() > 0) {
            tblMechData.setModel(list);

            //Create a sorting class and apply it to the list
            TableRowSorter sorter = new TableRowSorter<MechList>(list);
            List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
            tblMechData.setRowSorter(sorter);

            tblMechData.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMechData.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblMechData.getColumnModel().getColumn(2).setPreferredWidth(40);
            tblMechData.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblMechData.getColumnModel().getColumn(4).setPreferredWidth(130);
        }

        this.lblLoading.setText(list.Size() + " Mechs loaded from " + opts.SaveLoadPath);
    }

    private void checkSelection() {
        if ( tblMechData.getSelectedRowCount() > 0 ) {
            Calculate();
            btnOpen.setEnabled(true);
            btnPrint.setEnabled(true);
            btnOpenMech.setEnabled(true);
            btnAdd2Force.setEnabled(true);
        } else {
            btnOpen.setEnabled(false);
            btnPrint.setEnabled(false);
            btnOpenMech.setEnabled(false);
            btnAdd2Force.setEnabled(false);
            txtSelected.setText("0 Units Selected for 0 BV and 0 C-Bills");
        }
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tlbActions = new javax.swing.JToolBar();
        btnOpen = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnAdd2Force = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOptions = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        spnMechTable = new javax.swing.JScrollPane();
        tblMechData = new javax.swing.JTable();
        txtSelected = new javax.swing.JLabel();
        btnOpenMech = new javax.swing.JButton();
        lblLoading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Mech(s)");
        setMinimumSize(new java.awt.Dimension(600, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tlbActions.setFloatable(false);
        tlbActions.setRollover(true);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/folder.gif"))); // NOI18N
        btnOpen.setToolTipText("Open Mech");
        btnOpen.setEnabled(false);
        btnOpen.setFocusable(false);
        btnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        tlbActions.add(btnOpen);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/action_print.gif"))); // NOI18N
        btnPrint.setToolTipText("Print Selected Mechs");
        btnPrint.setEnabled(false);
        btnPrint.setFocusable(false);
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tlbActions.add(btnPrint);

        btnAdd2Force.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/page_up.gif"))); // NOI18N
        btnAdd2Force.setToolTipText("Add to Force List");
        btnAdd2Force.setEnabled(false);
        btnAdd2Force.setFocusable(false);
        btnAdd2Force.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd2Force.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tlbActions.add(btnAdd2Force);
        tlbActions.add(jSeparator1);

        btnOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/icon_settings.gif"))); // NOI18N
        btnOptions.setToolTipText("Change Options");
        btnOptions.setFocusable(false);
        btnOptions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOptions.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });
        tlbActions.add(btnOptions);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/action_refresh_blue.gif"))); // NOI18N
        jButton1.setToolTipText("Refresh Mech List");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        tlbActions.add(jButton1);

        tblMechData.setAutoCreateRowSorter(true);
        tblMechData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblMechData.setIntercellSpacing(new java.awt.Dimension(4, 4));
        tblMechData.setShowVerticalLines(false);
        tblMechData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMechDataMouseClicked(evt);
            }
        });
        spnMechTable.setViewportView(tblMechData);

        txtSelected.setText("0 Units Selected");

        btnOpenMech.setText("Open Mech");
        btnOpenMech.setEnabled(false);
        btnOpenMech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenMechActionPerformed(evt);
            }
        });

        lblLoading.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoading.setText("Loading Mechs....");
        lblLoading.setMaximumSize(new java.awt.Dimension(500, 14));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tlbActions, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSelected, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblLoading, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnMechTable, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(494, Short.MAX_VALUE)
                .addComponent(btnOpenMech)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tlbActions, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnMechTable, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOpenMech, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMechDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMechDataMouseClicked
        if ( evt.getClickCount() == 2 ) {
            LoadMech();
        } else {
            checkSelection();
        }
    }//GEN-LAST:event_tblMechDataMouseClicked

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        LoadMech();
}//GEN-LAST:event_btnOpenActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (list.Size() == 0) LoadList();
    }//GEN-LAST:event_formWindowOpened

    private void btnOpenMechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenMechActionPerformed
        LoadMech();
    }//GEN-LAST:event_btnOpenMechActionPerformed

    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        dlgOptions dgOptions = new dlgOptions( parent, true );
        dgOptions.setLocationRelativeTo( this );
        dgOptions.setVisible( true );
        opts = new Options();
        LoadList();
    }//GEN-LAST:event_btnOptionsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LoadList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if ( tblMechData.getSelectedRowCount() > 0 ) {
            Printer print = new Printer();

            try
            {
                XMLReader read = new XMLReader();
                int[] rows = tblMechData.getSelectedRows();
                for ( int i=0; i < rows.length; i++ ) {
                    MechListData data = list.Get(tblMechData.convertRowIndexToModel(rows[i]));
                    Mech m = read.ReadMech(data.getFilename());
                    if (data.isOmni()) {
                        m.SetCurLoadout(data.getConfig());
                    }
                    print.AddMech(m);
                }
                print.Print();
                tblMechData.clearSelection();
                checkSelection();

            } catch ( Exception e ) {

            }
        }

    }//GEN-LAST:event_btnPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd2Force;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnOpenMech;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton jButton1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel lblLoading;
    private javax.swing.JScrollPane spnMechTable;
    private javax.swing.JTable tblMechData;
    private javax.swing.JToolBar tlbActions;
    private javax.swing.JLabel txtSelected;
    // End of variables declaration//GEN-END:variables

}
