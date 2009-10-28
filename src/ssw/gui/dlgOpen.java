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

import Force.Unit;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;
import components.Mech;
import filehandlers.*;
import list.*;
import ssw.print.Printer;

public class dlgOpen extends javax.swing.JFrame implements PropertyChangeListener {
    private frmMain parent;
    private MechList list = new MechList();
    private Media media = new Media();
    private String dirPath = "";
    private String NL = "";
    private String msg = "";

    /** Creates new form dlgOpen */
    public dlgOpen(java.awt.Frame parent, boolean modal) {
        initComponents();
        this.parent = (frmMain) parent;
        
        prgResaving.setVisible(false);
        cmbTech.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any Tech", "Clan", "Inner Sphere", "Mixed" }));
        cmbEra.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any Era", "Age of War/Star League", "Succession Wars", "Clan Invasion", "Dark Ages", "All Eras (non-canon)" }));
        cmbRulesLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any Level", "Introductory", "Tournament Legal", "Advanced Rules", "Experimental Tech", "Era Specific" }));
        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any Type", "BattleMech", "IndustrialMech", "Primitive BattleMech", "Primitive IndustrialMech" }));
        cmbMotive.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any Motive", "Biped", "Quad" }));
        NL = System.getProperty( "line.separator" );
    }

    private void LoadMech() {
        MechListData Data = ((MechList) tblMechData.getModel()).Get( tblMechData.convertRowIndexToModel( tblMechData.getSelectedRow() ) );
        try
        {
            MechReader read = new MechReader();
            Mech m = read.ReadMech( Data.getFilename(), parent.data );
            if (Data.isOmni()) {
                m.SetCurLoadout( Data.getConfig() );
            }
            parent.setMech(m);

            parent.Prefs.put( "LastOpenDirectory", Data.getFilename().substring( 0, Data.getFilename().lastIndexOf( File.separator ) + 1 ) );
            parent.Prefs.put( "LastOpenFile", Data.getFilename().substring( Data.getFilename().lastIndexOf( File.separator ) + 1 ) ); 

            parent.CurMech.SetChanged( false );

            tblMechData.clearSelection();
            this.setVisible(false);

        } catch ( Exception e ) {
            javax.swing.JOptionPane.showMessageDialog( this.parent, e.getMessage() );
        }
    }

    private void Calculate() {
        txtSelected.setText("0 Units Selected for 0 BV and 0 C-Bills");

        int BV = 0;
        double Cost = 0;

        int[] rows = tblMechData.getSelectedRows();
        for ( int i=0; i < rows.length; i++ ) {
            MechListData data = list.Get(tblMechData.convertRowIndexToModel(rows[i]));
            BV += data.getBV();
            Cost += data.getCost();
        }

        txtSelected.setText(rows.length + " Units Selected for " + String.format("%,d", BV) + " BV and " + String.format("%,.2f", Cost) + " C-Bills");
    }

    public void LoadList() {
        LoadList(true);
    }
    public void LoadList(boolean useIndex) {
        this.lblStatus.setText("Loading Mechs...");
        this.txtSelected.setText("0 Units Selected for 0 BV and 0 C-Bills");
        this.tblMechData.setModel(new MechList());
        
        if (dirPath.isEmpty()) {
            dirPath = parent.Prefs.get("ListPath", parent.Prefs.get( "LastOpenDirectory", "" ) );

            if ( dirPath.isEmpty() && this.isVisible() ) {
                dirPath = media.GetDirectorySelection(this, "", "Select SSW File Directory");
                parent.Prefs.put("ListPath", dirPath);
            }
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        list = new MechList(dirPath, useIndex);

        if (list.Size() > 0) {
            setupList(list);
        } else {
            dirPath = media.GetDirectorySelection(this, "", "Select SSW File Directory");
            parent.Prefs.put("ListPath", dirPath);
            LoadList(false);
        }

        String displayPath = dirPath;
        if (! dirPath.isEmpty() ) {
            if (dirPath.contains(File.separator)) {
                displayPath = dirPath.substring(0, 3) + "..." + dirPath.substring(dirPath.lastIndexOf(File.separator)) + "";
            }
        }
        this.lblStatus.setText(list.Size() + " Mechs loaded from " + displayPath);
        this.lblStatus.setToolTipText(dirPath);
        Filter(null);
        spnMechTable.getVerticalScrollBar().setValue(0);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void setupList(MechList mechList) {
        tblMechData.setModel(mechList);

        //Create a sorting class and apply it to the list
        TableRowSorter sorter = new TableRowSorter<MechList>(mechList);
        List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        tblMechData.setRowSorter(sorter);

        tblMechData.getColumnModel().getColumn(0).setPreferredWidth(20);
        tblMechData.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblMechData.getColumnModel().getColumn(2).setPreferredWidth(20);
        tblMechData.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblMechData.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblMechData.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblMechData.getColumnModel().getColumn(6).setPreferredWidth(50);
        tblMechData.getColumnModel().getColumn(7).setPreferredWidth(20);
    }

    private void checkSelection() {
        if ( tblMechData.getSelectedRowCount() > 0 ) {
            Calculate();
            btnOpen.setEnabled(true);
            btnPrint.setEnabled(true);
            btnAdd2Force.setEnabled(true);
        } else {
            btnOpen.setEnabled(false);
            btnPrint.setEnabled(false);
            btnAdd2Force.setEnabled(false);
            txtSelected.setText("0 Units Selected for 0 BV and 0 C-Bills");
        }
    }

    private void batchUpdateMechs() {
        //String msg = "";
        prgResaving.setValue(0);
        prgResaving.setVisible(true);
        int Response = javax.swing.JOptionPane.showConfirmDialog(this, "This will open and re-save each file in the current directory so that all files are updated with current BV and Cost calculations.\nThis process could take a few minutes, are you ready?", "Batch Mech Processing", javax.swing.JOptionPane.YES_NO_OPTION);
        if (Response == javax.swing.JOptionPane.YES_OPTION) {
            msg = "";
            setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR ) );
            try {
                Resaver Saving = new Resaver( this );
                Saving.addPropertyChangeListener( this );
                Saving.execute();
            } catch( Exception e ) {
                // fatal error.  let the user know
                javax.swing.JOptionPane.showMessageDialog( this, "A fatal error occured while processing the 'Mechs:\n" + e.getMessage() );
                e.printStackTrace();
            }
            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
            LoadList(false);
        } else {
            prgResaving.setVisible(false);
        }
    }

    private void setTooltip( MechListData data ) {
        //spnMechTable.setToolTipText( data.getInfo() );
        txtSelected.setText(data.getInfo());
    }
    public void propertyChange( PropertyChangeEvent e ) {
       prgResaving.setValue( ((Resaver) e.getSource()).getProgress() );
    }

    private class Resaver extends SwingWorker<Void,Void> {
        dlgOpen Owner;
        public Resaver( dlgOpen owner ) {
            Owner = owner;
        }

        @Override
        public void done() {
            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
            if( msg.length() > 0 ) {
                dlgTextExport Message = new dlgTextExport( Owner, true, msg );
                Message.setLocationRelativeTo( Owner );
                Message.setVisible( true );
            } else {
                javax.swing.JOptionPane.showMessageDialog( Owner, "Finished the operation without errors." );
            }
            prgResaving.setVisible(false);
            prgResaving.setValue( 0 );
            LoadList();
        }

        @Override
        protected Void doInBackground() throws Exception {
            MechReader read = new MechReader();
            MechWriter writer = new MechWriter();

            FileList List = new FileList(dirPath);
            File[] Files = List.getFiles();

            for ( int i=0; i < Files.length; i++ ) {
                File file = Files[i];
                if (file.isFile() && file.getCanonicalPath().endsWith(".ssw")) {
                    try {
                        Mech m = read.ReadMech( file.getCanonicalPath(), parent.data );

                        // save the mech to XML in the current location
                        writer.setMech(m);
                        try {
                            writer.WriteXML( file.getCanonicalPath() );
                        } catch( IOException e ) {
                            msg += "Could not load the following file:" + NL;
                            msg += file.getCanonicalPath() + NL + NL;
                        }
                    } catch ( Exception e ) {
                        // log the error
                        msg += file.getCanonicalPath() + NL;
                        if( e.getMessage() == null ) {
                            StackTraceElement[] trace = e.getStackTrace();
                            for( int j = 0; j < trace.length; j++ ) {
                                msg += trace[j].toString() + NL;
                            }
                            msg += NL;
                        } else {
                            msg += e.getMessage() + NL + NL;
                        }
                    }
                }
                int progress = ((int) (( ((double) i + 1) / (double) Files.length ) * 100.0 ) );
                setProgress( progress );
            }

            return null;
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

        txtSelected = new javax.swing.JLabel();
        tlbActions = new javax.swing.JToolBar();
        btnChangeDir = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnOpen = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnAdd2Force = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOptions = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnMagic = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        spnMechTable = new javax.swing.JScrollPane();
        tblMechData = new javax.swing.JTable();
        pnlFilters = new javax.swing.JPanel();
        lblTech = new javax.swing.JLabel();
        cmbTech = new javax.swing.JComboBox();
        lblEra = new javax.swing.JLabel();
        cmbEra = new javax.swing.JComboBox();
        lblLevel = new javax.swing.JLabel();
        cmbRulesLevel = new javax.swing.JComboBox();
        lblType = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox();
        lblMotive = new javax.swing.JLabel();
        cmbMotive = new javax.swing.JComboBox();
        lblTonnage = new javax.swing.JLabel();
        txtMinTon = new javax.swing.JTextField();
        txtMaxTon = new javax.swing.JTextField();
        lblBV = new javax.swing.JLabel();
        txtMinBV = new javax.swing.JTextField();
        txtMaxBV = new javax.swing.JTextField();
        lblCost = new javax.swing.JLabel();
        txtMinCost = new javax.swing.JTextField();
        txtMaxCost = new javax.swing.JTextField();
        lblSource = new javax.swing.JLabel();
        txtSource = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        chkOmni = new javax.swing.JCheckBox();
        btnClearFilter = new javax.swing.JButton();
        btnFilter = new javax.swing.JButton();
        lblMinMP = new javax.swing.JLabel();
        cmbMinMP = new javax.swing.JComboBox();
        lblStatus = new javax.swing.JLabel();
        prgResaving = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Mech(s)");
        setMinimumSize(new java.awt.Dimension(600, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtSelected.setText("0 Units Selected");

        tlbActions.setFloatable(false);
        tlbActions.setRollover(true);

        btnChangeDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/folders.png"))); // NOI18N
        btnChangeDir.setToolTipText("Change Directory");
        btnChangeDir.setFocusable(false);
        btnChangeDir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeDir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnChangeDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeDirActionPerformed(evt);
            }
        });
        tlbActions.add(btnChangeDir);
        tlbActions.add(jSeparator4);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/folder-open-document.png"))); // NOI18N
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

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/printer.png"))); // NOI18N
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

        btnAdd2Force.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/clipboard--plus.png"))); // NOI18N
        btnAdd2Force.setToolTipText("Add to Force List");
        btnAdd2Force.setEnabled(false);
        btnAdd2Force.setFocusable(false);
        btnAdd2Force.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd2Force.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd2Force.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ForceActionPerformed(evt);
            }
        });
        tlbActions.add(btnAdd2Force);
        tlbActions.add(jSeparator1);

        btnOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/gear.png"))); // NOI18N
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

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/arrow-circle-double.png"))); // NOI18N
        btnRefresh.setToolTipText("Refresh Mech List");
        btnRefresh.setFocusable(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        tlbActions.add(btnRefresh);
        tlbActions.add(jSeparator2);

        btnMagic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssw/Images/wand.png"))); // NOI18N
        btnMagic.setToolTipText("Update Mech Files (Long Process!)");
        btnMagic.setFocusable(false);
        btnMagic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMagic.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMagic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMagicActionPerformed(evt);
            }
        });
        tlbActions.add(btnMagic);
        tlbActions.add(jSeparator3);

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
        tblMechData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMechDataMouseMoved(evt);
            }
        });
        tblMechData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblMechDataKeyPressed(evt);
            }
        });
        spnMechTable.setViewportView(tblMechData);

        lblTech.setText("Technology");

        cmbTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Filter(evt);
            }
        });

        lblEra.setText("Era");

        cmbEra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Filter(evt);
            }
        });

        lblLevel.setText("Rules Level");

        cmbRulesLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRulesLevelFilter(evt);
            }
        });

        lblType.setText("Mech Type");

        cmbType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTypeFilter(evt);
            }
        });

        lblMotive.setText("Motive Type");

        cmbMotive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMotiveFilter(evt);
            }
        });

        lblTonnage.setText("Tonnage");

        txtMaxTon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaxTonFilter(evt);
            }
        });

        lblBV.setText("Battle Value");

        txtMaxBV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Filter(evt);
            }
        });

        lblCost.setText("C-Bill Cost");

        txtMinCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMinCostFilter(evt);
            }
        });

        txtMaxCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaxCostFilter(evt);
            }
        });

        lblSource.setText("Source");

        txtSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSourceActionPerformed(evt);
            }
        });
        txtSource.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSourceKeyReleased(evt);
            }
        });

        lblName.setText("Name");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameKeyTyped(evt);
            }
        });

        chkOmni.setText("Omni Only");
        chkOmni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOmniActionPerformed(evt);
            }
        });

        btnClearFilter.setText("Clear");
        btnClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFilterFilter(evt);
            }
        });

        btnFilter.setText("Filter");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Filter(evt);
            }
        });

        lblMinMP.setText("Min MP");

        cmbMinMP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));
        cmbMinMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMinMPFilter(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltersLayout = new javax.swing.GroupLayout(pnlFilters);
        pnlFilters.setLayout(pnlFiltersLayout);
        pnlFiltersLayout.setHorizontalGroup(
            pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbTech, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTech))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEra, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEra))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbRulesLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLevel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMotive)
                            .addComponent(cmbMotive, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFiltersLayout.createSequentialGroup()
                                .addComponent(txtMinTon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaxTon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTonnage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFiltersLayout.createSequentialGroup()
                                .addComponent(txtMinBV, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaxBV, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblBV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFiltersLayout.createSequentialGroup()
                                .addComponent(txtMinCost, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaxCost, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCost))
                        .addGap(6, 6, 6)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSource, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSource))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFiltersLayout.createSequentialGroup()
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkOmni))
                            .addComponent(lblName))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMinMP)
                    .addComponent(cmbMinMP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(212, 212, 212)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFilter)
                    .addComponent(btnClearFilter))
                .addContainerGap())
        );
        pnlFiltersLayout.setVerticalGroup(
            pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLevel)
                    .addComponent(lblEra)
                    .addComponent(lblTech)
                    .addComponent(lblType)
                    .addComponent(lblMotive))
                .addGap(1, 1, 1)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRulesLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMotive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTonnage)
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaxTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMinTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblBV)
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaxBV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMinBV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCost)
                            .addComponent(lblSource)
                            .addComponent(lblName))
                        .addGap(1, 1, 1)
                        .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMinCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaxCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkOmni)
                            .addComponent(txtSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFiltersLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(btnFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearFilter)
                .addContainerGap())
            .addGroup(pnlFiltersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMinMP)
                .addGap(1, 1, 1)
                .addComponent(cmbMinMP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        lblStatus.setText("Loading Mechs....");

        prgResaving.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tlbActions, javax.swing.GroupLayout.DEFAULT_SIZE, 1033, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addComponent(prgResaving, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnMechTable, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFilters, javax.swing.GroupLayout.DEFAULT_SIZE, 1013, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tlbActions, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(prgResaving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnMechTable, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        dlgPrefs preferences = new dlgPrefs( parent, true );
        preferences.setLocationRelativeTo( this );
        preferences.setVisible( true );
        this.setVisible(true);
        parent.Mechrender.Reset();
        LoadList();
    }//GEN-LAST:event_btnOptionsActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        LoadList(false);
        Filter(evt);
        this.setVisible(true);
}//GEN-LAST:event_btnRefreshActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if ( tblMechData.getSelectedRowCount() > 0 ) {
            Printer print = new Printer();

            try
            {
                MechReader read = new MechReader();
                int[] rows = tblMechData.getSelectedRows();
                for ( int i=0; i < rows.length; i++ ) {
                    MechListData data = list.Get(tblMechData.convertRowIndexToModel(rows[i]));
                    Mech m = read.ReadMech( data.getFilename(), parent.data );
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

    private void btnMagicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMagicActionPerformed
        //setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
        batchUpdateMechs();
        //setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
    }//GEN-LAST:event_btnMagicActionPerformed

    private void Filter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Filter
        ListFilter filters = new ListFilter();

        if (cmbTech.getSelectedIndex() > 0) {filters.setTech(cmbTech.getSelectedItem().toString());}
        if (cmbEra.getSelectedIndex() > 0) {filters.setEra(cmbEra.getSelectedItem().toString());}
        if (cmbType.getSelectedIndex() > 0) {filters.setType(cmbType.getSelectedItem().toString().replace(" ", ""));}
        if (cmbMotive.getSelectedIndex() > 0) {filters.setMotive(cmbMotive.getSelectedItem().toString());}
        if (cmbRulesLevel.getSelectedIndex() > 0) {filters.setLevel(cmbRulesLevel.getSelectedItem().toString());}
        if (cmbMinMP.getSelectedIndex() > 0) {filters.setMinMP(Integer.parseInt(cmbMinMP.getSelectedItem().toString()));}
        if (! txtMinBV.getText().isEmpty() ) {
            if ( txtMaxBV.getText().isEmpty() ) {
                filters.setBV(0, Integer.parseInt(txtMinBV.getText()));
            } else {
                filters.setBV(Integer.parseInt(txtMinBV.getText()), Integer.parseInt(txtMaxBV.getText()));
            }
        }
        if (! txtMinCost.getText().isEmpty() ) {
            if ( txtMaxCost.getText().isEmpty() ) {
                filters.setCost(0, Double.parseDouble(txtMinCost.getText()));
            } else {
                filters.setCost(Double.parseDouble(txtMinCost.getText()), Double.parseDouble(txtMaxCost.getText()));
            }
        }
        if (! txtMinTon.getText().isEmpty() ) {
            if ( txtMaxTon.getText().isEmpty() ) {
                filters.setTonnage(20, Integer.parseInt(txtMinTon.getText() ));
            } else {
                filters.setTonnage(Integer.parseInt( txtMinTon.getText() ), Integer.parseInt( txtMaxTon.getText() ));
            }
        }
        if (! txtName.getText().isEmpty() ) { filters.setName(txtName.getText().trim()); }
        if (! txtSource.getText().isEmpty() ) { filters.setSource(txtSource.getText().trim()); }
        filters.setIsOmni(chkOmni.isSelected());

        MechList filtered = list.Filter(filters);
        setupList(filtered);
    }//GEN-LAST:event_Filter

    private void txtMinCostFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinCostFilter
        // TODO add your handling code here:
}//GEN-LAST:event_txtMinCostFilter

    private void txtMaxCostFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaxCostFilter
        // TODO add your handling code here:
}//GEN-LAST:event_txtMaxCostFilter

    private void btnClearFilterFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterFilter
        setupList(list);
        
        //clear the dropdowns
        cmbEra.setSelectedIndex(0);
        cmbMotive.setSelectedIndex(0);
        cmbRulesLevel.setSelectedIndex(0);
        cmbTech.setSelectedIndex(0);
        cmbType.setSelectedIndex(0);
        cmbMinMP.setSelectedIndex(0);

        //clear text fields
        txtMinBV.setText("");
        txtMaxBV.setText("");
        txtMinCost.setText("");
        txtMaxCost.setText("");
        txtMinTon.setText("");
        txtMaxTon.setText("");
        txtName.setText("");
        chkOmni.setSelected(false);
}//GEN-LAST:event_btnClearFilterFilter

    private void tblMechDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblMechDataKeyPressed
        //javax.swing.JOptionPane.showMessageDialog(this, "You typed a " + evt.getKeyChar());
    }//GEN-LAST:event_tblMechDataKeyPressed

    private void btnAdd2ForceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ForceActionPerformed
        if ( tblMechData.getSelectedRowCount() > 0 ) {
            int[] rows = tblMechData.getSelectedRows();
            for ( int i=0; i < rows.length; i++ ) {
                MechListData data = ((MechList) tblMechData.getModel()).Get(tblMechData.convertRowIndexToModel(rows[i]));
                parent.dForce.getForce().Units.add(new Unit(data));
                parent.dForce.getForce().RefreshBV();
            }
        }
        tblMechData.clearSelection();

        parent.dForce.setLocationRelativeTo(null);
        parent.dForce.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAdd2ForceActionPerformed

    private void txtMaxTonFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaxTonFilter
        Filter(null);
}//GEN-LAST:event_txtMaxTonFilter

    private void cmbRulesLevelFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRulesLevelFilter
        Filter(null);
}//GEN-LAST:event_cmbRulesLevelFilter

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        Filter(null);
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyTyped
        Filter(null);
    }//GEN-LAST:event_txtNameKeyTyped

    private void cmbTypeFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTypeFilter
        Filter(null);
}//GEN-LAST:event_cmbTypeFilter

    private void cmbMotiveFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMotiveFilter
        Filter(null);
}//GEN-LAST:event_cmbMotiveFilter

    private void btnChangeDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeDirActionPerformed
        dirPath = media.GetDirectorySelection( parent, dirPath );
        this.setVisible(true);
        parent.Prefs.put("ListPath", dirPath);
        LoadList(false);
    }//GEN-LAST:event_btnChangeDirActionPerformed

    private void chkOmniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOmniActionPerformed
        Filter(null);
    }//GEN-LAST:event_chkOmniActionPerformed

    private void txtSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSourceActionPerformed
        Filter(null);
}//GEN-LAST:event_txtSourceActionPerformed

    private void txtSourceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSourceKeyReleased
        Filter(null);
    }//GEN-LAST:event_txtSourceKeyReleased

    private void cmbMinMPFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMinMPFilter
        Filter(null);
}//GEN-LAST:event_cmbMinMPFilter

    private void tblMechDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMechDataMouseMoved
        setTooltip( (MechListData) ((MechList) tblMechData.getModel()).Get(tblMechData.convertRowIndexToModel(tblMechData.rowAtPoint(evt.getPoint()))) );
    }//GEN-LAST:event_tblMechDataMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd2Force;
    private javax.swing.JButton btnChangeDir;
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnMagic;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JCheckBox chkOmni;
    private javax.swing.JComboBox cmbEra;
    private javax.swing.JComboBox cmbMinMP;
    private javax.swing.JComboBox cmbMotive;
    private javax.swing.JComboBox cmbRulesLevel;
    private javax.swing.JComboBox cmbTech;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JLabel lblBV;
    private javax.swing.JLabel lblCost;
    private javax.swing.JLabel lblEra;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JLabel lblMinMP;
    private javax.swing.JLabel lblMotive;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSource;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTech;
    private javax.swing.JLabel lblTonnage;
    private javax.swing.JLabel lblType;
    private javax.swing.JPanel pnlFilters;
    private javax.swing.JProgressBar prgResaving;
    private javax.swing.JScrollPane spnMechTable;
    private javax.swing.JTable tblMechData;
    private javax.swing.JToolBar tlbActions;
    private javax.swing.JTextField txtMaxBV;
    private javax.swing.JTextField txtMaxCost;
    private javax.swing.JTextField txtMaxTon;
    private javax.swing.JTextField txtMinBV;
    private javax.swing.JTextField txtMinCost;
    private javax.swing.JTextField txtMinTon;
    private javax.swing.JTextField txtName;
    private javax.swing.JLabel txtSelected;
    private javax.swing.JTextField txtSource;
    // End of variables declaration//GEN-END:variables

}
