/*
 * Created by JFormDesigner on Sat Jan 08 22:22:25 CET 2011
 */

package main;

import action.base.ClientType;
import converter.PatientStatusAktivConverter;
import converter.PatientStatusDiagnosticConverter;
import data.*;
import dialogs.DiagnosticDialog;
import dialogs.DialogCallback;
import dialogs.MedicsDialog;
import dialogs.PatientAddDialog;
import ini.ErrorLog;
import ini.Theme;
import ini.VersionProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.*;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import server.ServerUtils;
import utils.ControllerException;
import validators.PositiveValidator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static javax.swing.JOptionPane.YES_OPTION;

/**
 * @author Zdenek Vyskocil
 */
@SuppressWarnings({"unchecked", "FieldCanBeLocal"})
public class Application extends JFrame {

    public ServerDataController controller;
    final JFileChooser fc = new JFileChooser();
    private final PatientAddDialog patientAddDialog;
    private final DiagnosticDialog diagnosticDialog;
    private final MedicsDialog medicsDialog;

    public static void main(String[] args) {
        ErrorLog.init(args, "server");
        Theme.init();
        Application dialog = new Application();
        dialog.setVisible(true);
    }

    public Application() {
        controller = new ServerDataController();
        patientAddDialog = new PatientAddDialog(this, controller);
        diagnosticDialog = new DiagnosticDialog(this, controller);
        medicsDialog = new MedicsDialog(this, controller);
        ServerUtils.addServerClientListener(controller);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setMultiSelectionEnabled(false);
        fc.setFileHidingEnabled(true);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String ex = getExtension(f);
                return ex != null && "osetrovna".equalsIgnoreCase(ex);
            }

            @Override
            public String getDescription() {
                return "Scifi-osetrovna soubory (*.osetrovna)";
            }
        });
        initComponents();
        this.setTitle(this.getTitle() + " (" + VersionProperty.getVersion() + ")");
        sicknessTable.setDefaultEditor(MedicTool.class, new DefaultCellEditor(medicToolComboBox));
        sicknessTable.setDefaultEditor(DiagnosticTool.class, new DefaultCellEditor(diagnosticToolComboBox));
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @SuppressWarnings("UnusedParameters")
    private void thisWindowClosing(WindowEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu ukončit server?", "Konec?", JOptionPane.YES_NO_OPTION)) {
            ServerUtils.stopServer();
            System.exit(0);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void startServerButtonActionPerformed(ActionEvent e) {
        startServerButton.setEnabled(false);
        ServerUtils.startServer();
        stopServerButton.setEnabled(true);
        saveDataButton.setEnabled(false);
        loadDataButton.setEnabled(false);
    }

    @SuppressWarnings("UnusedParameters")
    private void stopServerButtonActionPerformed(ActionEvent e) {
        stopServerButton.setEnabled(false);
        ServerUtils.stopServer();
        startServerButton.setEnabled(true);
        saveDataButton.setEnabled(true);
        loadDataButton.setEnabled(true);
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    @SuppressWarnings("UnusedParameters")
    private void chatButtonActionPerformed(ActionEvent e) {
        if (chatEdit.getText().trim().length() > 0) {
            controller.getChatServerData().writeText(ClientType.POCITAC, chatEdit.getText().trim());
            chatEdit.setText("");
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void saveActionPerformed(ActionEvent e) {
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setDialogTitle("Uložit nastavení");
        int returnVal = fc.showDialog(this, "Uložit");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            String ex = getExtension(fc.getSelectedFile());
            if (!"osetrovna".equalsIgnoreCase(ex)) {
                fileName += ".osetrovna";
            }
            controller.save(fileName);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void loadDataActionPerformed(ActionEvent e) {
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setDialogTitle("Nahrát nastavení");
        int returnVal = fc.showDialog(this, "Nahrát");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String ex = getExtension(fc.getSelectedFile());
            if ("osetrovna".equalsIgnoreCase(ex)) {
                controller.load(fc.getSelectedFile().getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Vložte soubor s příponou '.osetrovna'!", "Špatný soubor",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void chatEnableButtonActionPerformed(ActionEvent e) {
        chatServerData.setChatEnabled(!chatServerData.isChatEnabled());
    }

    private void chatEditKeyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
            chatButtonActionPerformed(null);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void addCrewButtonActionPerformed(ActionEvent e) {
        controller.addCrewMember();
    }

    @SuppressWarnings("UnusedParameters")
    private void addToolButtonActionPerformed(ActionEvent e) {
        controller.addTool();
    }

    @SuppressWarnings("UnusedParameters")
    private void addSicknessButtonActionPerformed(ActionEvent e) {
        controller.addSickness();
    }

    @SuppressWarnings("UnusedParameters")
    private void addDiagnosticButtonActionPerformed(ActionEvent e) {
        controller.addDiagnosticTool();
    }

    private void comboBoxKeyPressed(KeyEvent e) {
        if (e.getSource() instanceof JComboBox) {
            delComboBoxValue((JComboBox) e.getSource(), e.getKeyCode());
        }
    }

    private void delComboBoxValue(JComboBox box, int keyCode) {
        if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
            box.setSelectedIndex(-1);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void addPatientButtonActionPerformed(ActionEvent e) {
        patientAddDialog.setVisible(true);
    }

    @SuppressWarnings("UnusedParameters")
    private void patientDiagnosticButtonActionPerformed(ActionEvent e) {
        diagnosticDialog.show(new DialogCallback<DiagnosticTool>() {
            public void onSelected(DiagnosticTool... items) {
                try {
                    controller.doDiagnostic(controller.getPatient(patientTable.getSelectedRow()), items[0]);
                    patientTable.clearSelection();
                } catch (ControllerException e1) {
                    JOptionPane.showMessageDialog(Application.this, e1.getMessage());
                }
            }
        });
    }

    private void patientMedicButtonActionPerformed(ActionEvent e) {
        medicsDialog.show(new DialogCallback<MedicTool>() {
            public void onSelected(MedicTool... items) {
                try {
                    controller.doMedic(controller.getPatient(patientTable.getSelectedRow()), items[0], items[1], items[2]);
                    patientTable.clearSelection();
                } catch (ControllerException e1) {
                    JOptionPane.showMessageDialog(Application.this, e1.getMessage());
                }
            }
        });
    }

    private void patientStopButtonActionPerformed(ActionEvent e) {
        try {
            controller.doStopPatient(controller.getPatient(patientTable.getSelectedRow()));
            patientTable.clearSelection();
        } catch (ControllerException e1) {
            JOptionPane.showMessageDialog(Application.this, e1.getMessage());
        }
    }

    private void removePatientButtonActionPerformed(ActionEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu smazat pacienta?", "Smazání pacienta", JOptionPane.YES_NO_OPTION)) {
            controller.removePatient(controller.getPatient(patientTable.getSelectedRow()));
        }
    }

    private void removeSicknessButtonActionPerformed(ActionEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu smazat nemoc?", "Smazání nemoci", JOptionPane.YES_NO_OPTION)) {
            controller.removeSickness(controller.getSickness(sicknessTable.getSelectedRow()));
        }
    }

    private void removeCrewMemberButtonActionPerformed(ActionEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu smazat člena posádky?", "Smazání člena posádky", JOptionPane.YES_NO_OPTION)) {
            controller.removeCrewMember(controller.getCrewMember(crewTable.getSelectedRow()));
        }
    }

    private void removeMedicToolMemberButtonActionPerformed(ActionEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu smazat léčebný přístroj?", "Smazání léčebného přístroje", JOptionPane.YES_NO_OPTION)) {
            controller.removeMedicTool(controller.getMedicTool(toolTable.getSelectedRow()));
        }
    }

    private void removeDiagnosticToolMemberButtonActionPerformed(ActionEvent e) {
        if (YES_OPTION == JOptionPane.showConfirmDialog(null, "Opravdu smazat diagnostický přístroj?", "Smazání diagnostického přístroje", JOptionPane.YES_NO_OPTION)) {
            controller.removeDiagnosticTool(controller.getDiagnosticTool(diagnosticTable.getSelectedRow()));
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        mainPanel = new JPanel();
        tabPanel = new JTabbedPane();
        crewsToolsTabPanel = new JPanel();
        crewPanel = new JPanel();
        crewToolbar = new JToolBar();
        addCrewButton = new JButton();
        removeCrewMemberButton = new JButton();
        crewScrollPanel = new JScrollPane();
        crewTable = new JTable();
        crewTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        toolPanel = new JPanel();
        toolToolbar = new JToolBar();
        addToolButton = new JButton();
        removeMedicToolMemberButton = new JButton();
        toolScrollPanel = new JScrollPane();
        toolTable = new JTable();
        toolTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        diagnosticPanel = new JPanel();
        diagnosticToolbar = new JToolBar();
        addDiagnosticButton = new JButton();
        removeDiagnosticToolMemberButton = new JButton();
        diagnosticScrollPanel = new JScrollPane();
        diagnosticTable = new JTable();
        diagnosticTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        sicknessToolsTabPanel = new JPanel();
        sicknessPanel = new JPanel();
        sicknessToolbar = new JToolBar();
        addSicknessButton = new JButton();
        removeSicknessButton = new JButton();
        sicknessScrollPanel = new JScrollPane();
        sicknessTable = new JTable();
        sicknessTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        patientToolsTabPanel = new JPanel();
        patientPanel = new JPanel();
        patientToolbar = new JToolBar();
        addPatientButton = new JButton();
        removePatientButton = new JButton();
        patientDiagnosticButton = new JButton();
        patientMedicButton = new JButton();
        patientStopButton = new JButton();
        patientScrollPanel = new JScrollPane();
        patientTable = new JTable();
        patientTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        mainToolbar = new JToolBar();
        startServerButton = new JButton();
        stopServerButton = new JButton();
        saveDataButton = new JButton();
        loadDataButton = new JButton();
        chatEnableButton = new JButton();
        bottomPanel = new JPanel();
        chatPanel = new JPanel();
        panel2 = new JPanel();
        chatListPanel = new JScrollPane();
        chatList = new JList();
        chatEditPanel = new JPanel();
        chatEdit = new JTextField();
        chatButton = new JButton();
        clientPanel = new JPanel();
        clinetScrollPane = new JScrollPane();
        clientTable = new JTable();
        medicToolComboBox = new JComboBox();
        clientsListModel = ServerUtils.getServerClients();
        chatListModel = controller.getChatServerData().getChatListModel();
        chatServerData = controller.getChatServerData();
        serverDataController = controller;
        positiveValidator1 = new PositiveValidator();
        patientStatusDiagnosticConverter1 = new PatientStatusDiagnosticConverter();
        diagnosticToolComboBox = new JComboBox();
        patientStatusAktivConverter1 = new PatientStatusAktivConverter();

        //======== this ========
        setTitle("Server - O\u0161et\u0159ovna");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== mainPanel ========
        {
            mainPanel.setLayout(new BorderLayout());

            //======== tabPanel ========
            {

                //======== crewsToolsTabPanel ========
                {
                    crewsToolsTabPanel.setLayout(new GridLayout(1, 3));

                    //======== crewPanel ========
                    {
                        crewPanel.setBorder(new TitledBorder("Pos\u00e1dka"));
                        crewPanel.setLayout(new BorderLayout());

                        //======== crewToolbar ========
                        {
                            crewToolbar.setFloatable(false);

                            //---- addCrewButton ----
                            addCrewButton.setText("P\u0159idat");
                            addCrewButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    addCrewButtonActionPerformed(e);
                                }
                            });
                            crewToolbar.add(addCrewButton);

                            //---- removeCrewMemberButton ----
                            removeCrewMemberButton.setText("Smazat");
                            removeCrewMemberButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    removeCrewMemberButtonActionPerformed(e);
                                }
                            });
                            crewToolbar.add(removeCrewMemberButton);
                        }
                        crewPanel.add(crewToolbar, BorderLayout.NORTH);

                        //======== crewScrollPanel ========
                        {

                            //---- crewTable ----
                            crewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            crewScrollPanel.setViewportView(crewTable);
                        }
                        crewPanel.add(crewScrollPanel, BorderLayout.CENTER);
                    }
                    crewsToolsTabPanel.add(crewPanel);

                    //======== toolPanel ========
                    {
                        toolPanel.setBorder(new TitledBorder("L\u00e9\u010debn\u00e9 p\u0159\u00edstroje"));
                        toolPanel.setLayout(new BorderLayout());

                        //======== toolToolbar ========
                        {
                            toolToolbar.setFloatable(false);

                            //---- addToolButton ----
                            addToolButton.setText("P\u0159idat");
                            addToolButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    addToolButtonActionPerformed(e);
                                }
                            });
                            toolToolbar.add(addToolButton);

                            //---- removeMedicToolMemberButton ----
                            removeMedicToolMemberButton.setText("Smazat");
                            removeMedicToolMemberButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    removeMedicToolMemberButtonActionPerformed(e);
                                }
                            });
                            toolToolbar.add(removeMedicToolMemberButton);
                        }
                        toolPanel.add(toolToolbar, BorderLayout.NORTH);

                        //======== toolScrollPanel ========
                        {

                            //---- toolTable ----
                            toolTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            toolScrollPanel.setViewportView(toolTable);
                        }
                        toolPanel.add(toolScrollPanel, BorderLayout.CENTER);
                    }
                    crewsToolsTabPanel.add(toolPanel);

                    //======== diagnosticPanel ========
                    {
                        diagnosticPanel.setBorder(new TitledBorder("Diagnostick\u00e9 p\u0159\u00edstroje"));
                        diagnosticPanel.setLayout(new BorderLayout());

                        //======== diagnosticToolbar ========
                        {
                            diagnosticToolbar.setFloatable(false);

                            //---- addDiagnosticButton ----
                            addDiagnosticButton.setText("P\u0159idat");
                            addDiagnosticButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    addDiagnosticButtonActionPerformed(e);
                                }
                            });
                            diagnosticToolbar.add(addDiagnosticButton);

                            //---- removeDiagnosticToolMemberButton ----
                            removeDiagnosticToolMemberButton.setText("Smazat");
                            removeDiagnosticToolMemberButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    removeDiagnosticToolMemberButtonActionPerformed(e);
                                }
                            });
                            diagnosticToolbar.add(removeDiagnosticToolMemberButton);
                        }
                        diagnosticPanel.add(diagnosticToolbar, BorderLayout.NORTH);

                        //======== diagnosticScrollPanel ========
                        {

                            //---- diagnosticTable ----
                            diagnosticTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            diagnosticScrollPanel.setViewportView(diagnosticTable);
                        }
                        diagnosticPanel.add(diagnosticScrollPanel, BorderLayout.CENTER);
                    }
                    crewsToolsTabPanel.add(diagnosticPanel);
                }
                tabPanel.addTab("Pos\u00e1dka / P\u0159\u00edstroje", crewsToolsTabPanel);


                //======== sicknessToolsTabPanel ========
                {
                    sicknessToolsTabPanel.setLayout(new BorderLayout());

                    //======== sicknessPanel ========
                    {
                        sicknessPanel.setBorder(new TitledBorder("Nemoci"));
                        sicknessPanel.setLayout(new BorderLayout());

                        //======== sicknessToolbar ========
                        {
                            sicknessToolbar.setFloatable(false);

                            //---- addSicknessButton ----
                            addSicknessButton.setText("P\u0159idat");
                            addSicknessButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    addSicknessButtonActionPerformed(e);
                                }
                            });
                            sicknessToolbar.add(addSicknessButton);

                            //---- removeSicknessButton ----
                            removeSicknessButton.setText("Smazat");
                            removeSicknessButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    removeSicknessButtonActionPerformed(e);
                                }
                            });
                            sicknessToolbar.add(removeSicknessButton);
                        }
                        sicknessPanel.add(sicknessToolbar, BorderLayout.NORTH);

                        //======== sicknessScrollPanel ========
                        {

                            //---- sicknessTable ----
                            sicknessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            sicknessScrollPanel.setViewportView(sicknessTable);
                        }
                        sicknessPanel.add(sicknessScrollPanel, BorderLayout.CENTER);
                    }
                    sicknessToolsTabPanel.add(sicknessPanel, BorderLayout.CENTER);
                }
                tabPanel.addTab("Nemoci", sicknessToolsTabPanel);


                //======== patientToolsTabPanel ========
                {
                    patientToolsTabPanel.setLayout(new BorderLayout());

                    //======== patientPanel ========
                    {
                        patientPanel.setBorder(new TitledBorder("Pacienti"));
                        patientPanel.setLayout(new BorderLayout());

                        //======== patientToolbar ========
                        {
                            patientToolbar.setFloatable(false);

                            //---- addPatientButton ----
                            addPatientButton.setText("P\u0159idat");
                            addPatientButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    addPatientButtonActionPerformed(e);
                                }
                            });
                            patientToolbar.add(addPatientButton);

                            //---- removePatientButton ----
                            removePatientButton.setText("Smazat");
                            removePatientButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    removePatientButtonActionPerformed(e);
                                }
                            });
                            patientToolbar.add(removePatientButton);

                            //---- patientDiagnosticButton ----
                            patientDiagnosticButton.setText("Diagnostikovat");
                            patientDiagnosticButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    patientDiagnosticButtonActionPerformed(e);
                                }
                            });
                            patientToolbar.add(patientDiagnosticButton);

                            //---- patientMedicButton ----
                            patientMedicButton.setText("L\u00e9\u010dit");
                            patientMedicButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    patientMedicButtonActionPerformed(e);
                                }
                            });
                            patientToolbar.add(patientMedicButton);

                            //---- patientStopButton ----
                            patientStopButton.setText("Zastav diag/l\u00e9\u010d");
                            patientStopButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    patientStopButtonActionPerformed(e);
                                }
                            });
                            patientToolbar.add(patientStopButton);
                        }
                        patientPanel.add(patientToolbar, BorderLayout.NORTH);

                        //======== patientScrollPanel ========
                        {

                            //---- patientTable ----
                            patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            patientScrollPanel.setViewportView(patientTable);
                        }
                        patientPanel.add(patientScrollPanel, BorderLayout.CENTER);
                    }
                    patientToolsTabPanel.add(patientPanel, BorderLayout.CENTER);
                }
                tabPanel.addTab("Pacienti", patientToolsTabPanel);

            }
            mainPanel.add(tabPanel, BorderLayout.CENTER);
        }
        contentPane.add(mainPanel, BorderLayout.CENTER);

        //======== mainToolbar ========
        {
            mainToolbar.setFloatable(false);

            //---- startServerButton ----
            startServerButton.setText("Spustit server");
            startServerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startServerButtonActionPerformed(e);
                }
            });
            mainToolbar.add(startServerButton);

            //---- stopServerButton ----
            stopServerButton.setText("Zastavit server");
            stopServerButton.setEnabled(false);
            stopServerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stopServerButtonActionPerformed(e);
                }
            });
            mainToolbar.add(stopServerButton);

            //---- saveDataButton ----
            saveDataButton.setText("Ulozit nastaveni");
            saveDataButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveActionPerformed(e);
                }
            });
            mainToolbar.add(saveDataButton);

            //---- loadDataButton ----
            loadDataButton.setText("Nahrat nastaveni");
            loadDataButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadDataActionPerformed(e);
                }
            });
            mainToolbar.add(loadDataButton);

            //---- chatEnableButton ----
            chatEnableButton.setText("text");
            chatEnableButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chatEnableButtonActionPerformed(e);
                }
            });
            mainToolbar.add(chatEnableButton);
        }
        contentPane.add(mainToolbar, BorderLayout.PAGE_START);

        //======== bottomPanel ========
        {
            bottomPanel.setLayout(new BorderLayout());

            //======== chatPanel ========
            {
                chatPanel.setBorder(new TitledBorder("Chat"));
                chatPanel.setMinimumSize(new Dimension(300, 50));
                chatPanel.setPreferredSize(new Dimension(300, 50));
                chatPanel.setMaximumSize(new Dimension(300, 50));
                chatPanel.setLayout(new BorderLayout());

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //======== chatListPanel ========
                    {

                        //---- chatList ----
                        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        chatList.setEnabled(false);
                        chatListPanel.setViewportView(chatList);
                    }
                    panel2.add(chatListPanel, BorderLayout.CENTER);

                    //======== chatEditPanel ========
                    {
                        chatEditPanel.setLayout(new BoxLayout(chatEditPanel, BoxLayout.X_AXIS));

                        //---- chatEdit ----
                        chatEdit.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                                chatEditKeyTyped(e);
                            }
                        });
                        chatEditPanel.add(chatEdit);

                        //---- chatButton ----
                        chatButton.setText("Odeslat");
                        chatButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                chatButtonActionPerformed(e);
                            }
                        });
                        chatEditPanel.add(chatButton);
                    }
                    panel2.add(chatEditPanel, BorderLayout.NORTH);
                }
                chatPanel.add(panel2, BorderLayout.CENTER);
            }
            bottomPanel.add(chatPanel, BorderLayout.CENTER);

            //======== clientPanel ========
            {
                clientPanel.setBorder(new TitledBorder("Klienti"));
                clientPanel.setMaximumSize(new Dimension(300, 150));
                clientPanel.setPreferredSize(new Dimension(300, 150));
                clientPanel.setMinimumSize(new Dimension(300, 150));
                clientPanel.setLayout(new BorderLayout());

                //======== clinetScrollPane ========
                {

                    //---- clientTable ----
                    clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    clinetScrollPane.setViewportView(clientTable);
                }
                clientPanel.add(clinetScrollPane, BorderLayout.CENTER);
            }
            bottomPanel.add(clientPanel, BorderLayout.LINE_END);
        }
        contentPane.add(bottomPanel, BorderLayout.PAGE_END);
        setSize(1080, 600);
        setLocationRelativeTo(null);

        //---- medicToolComboBox ----
        medicToolComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                comboBoxKeyPressed(e);
            }
        });

        //---- diagnosticToolComboBox ----
        diagnosticToolComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                comboBoxKeyPressed(e);
            }
        });

        //---- bindings ----
        bindingGroup = new BindingGroup();
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    clientsListModel, clientTable);
            binding.setEditable(false);
            binding.addColumnBinding(BeanProperty.create("clientType"))
                    .setColumnName("Klient")
                    .setColumnClass(String.class);
            binding.addColumnBinding(BeanProperty.create("clientIp"))
                    .setColumnName("Ip adrresa")
                    .setColumnClass(String.class);
            binding.addColumnBinding(BeanProperty.create("connected"))
                    .setColumnName("Connected")
                    .setColumnClass(Boolean.class);
            binding.addColumnBinding(BeanProperty.create("port"))
                    .setColumnName("Port")
                    .setColumnClass(Integer.class);
            bindingGroup.addBinding(binding);
            binding.bind();
        }
        bindingGroup.addBinding(SwingBindings.createJListBinding(UpdateStrategy.READ,
                chatListModel, chatList));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                chatListModel, ELProperty.create("${length}"),
                chatList, BeanProperty.create("selectedIndex")));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                chatServerData, BeanProperty.create("chatButtonTitle"),
                chatEnableButton, BeanProperty.create("text")));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                chatServerData, BeanProperty.create("chatEnabled"),
                chatEdit, BeanProperty.create("enabled")));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                chatServerData, BeanProperty.create("chatEnabled"),
                chatButton, BeanProperty.create("enabled")));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                chatServerData, BeanProperty.create("chatEnabled"),
                chatList, BeanProperty.create("enabled")));
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    serverDataController, (BeanProperty) BeanProperty.create("crewListModel"), crewTable);
            binding.addColumnBinding(BeanProperty.create("name"))
                    .setColumnName("Jm\u00e9no")
                    .setColumnClass(String.class);
            binding.addColumnBinding(BeanProperty.create("visible"))
                    .setColumnName("Viditelnost")
                    .setColumnClass(Boolean.class);
            bindingGroup.addBinding(binding);
        }
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    serverDataController, (BeanProperty) BeanProperty.create("toolListModel"), toolTable);
            binding.addColumnBinding(BeanProperty.create("name"))
                    .setColumnName("N\u00e1zev")
                    .setColumnClass(String.class);
            JTableBinding.ColumnBinding columnBinding = binding.addColumnBinding(BeanProperty.create("count"))
                    .setColumnName("Celkem")
                    .setColumnClass(Integer.class);
            columnBinding.setValidator(positiveValidator1);
            columnBinding = binding.addColumnBinding(BeanProperty.create("inUse"))
                    .setColumnName("Pou\u017e\u00edv\u00e1 se")
                    .setColumnClass(Integer.class)
                    .setEditable(false);
            columnBinding.setValidator(positiveValidator1);
            binding.addColumnBinding(BeanProperty.create("visible"))
                    .setColumnName("Viditelnost")
                    .setColumnClass(Boolean.class);
            bindingGroup.addBinding(binding);
        }
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    serverDataController, (BeanProperty) BeanProperty.create("sicknessListModel"), sicknessTable);
            binding.addColumnBinding(BeanProperty.create("name"))
                    .setColumnName("N\u00e1zev")
                    .setColumnClass(String.class);
            binding.addColumnBinding(BeanProperty.create("medicTool1"))
                    .setColumnName("P\u0159\u00edstroj 1")
                    .setColumnClass(MedicTool.class);
            binding.addColumnBinding(BeanProperty.create("medicTool2"))
                    .setColumnName("P\u0159\u00edstroj 2")
                    .setColumnClass(MedicTool.class);
            binding.addColumnBinding(BeanProperty.create("medicTool3"))
                    .setColumnName("P\u0159\u00edstroj 3")
                    .setColumnClass(MedicTool.class);
            binding.addColumnBinding(BeanProperty.create("diagnosticTool1"))
                    .setColumnName("Diag. p\u0159\u00edstroj 1")
                    .setColumnClass(DiagnosticTool.class);
            binding.addColumnBinding(BeanProperty.create("diagnosticTool2"))
                    .setColumnName("Diag. p\u0159\u00edstroj 2")
                    .setColumnClass(DiagnosticTool.class);
            binding.addColumnBinding(BeanProperty.create("diagnosticTool3"))
                    .setColumnName("Diag. p\u0159\u00edstroj 3")
                    .setColumnClass(DiagnosticTool.class);
            JTableBinding.ColumnBinding columnBinding = binding.addColumnBinding(BeanProperty.create("healthTime"))
                    .setColumnName("\u010cas l\u00e9\u010den\u00ed (sec)")
                    .setColumnClass(Long.class);
            columnBinding.setValidator(positiveValidator1);
            columnBinding = binding.addColumnBinding(BeanProperty.create("deadTime"))
                    .setColumnName("\u010cas do umrt\u00ed (sec)")
                    .setColumnClass(Long.class);
            columnBinding.setValidator(positiveValidator1);
            binding.addColumnBinding(BeanProperty.create("visible"))
                    .setColumnName("Viditelnost")
                    .setColumnClass(Boolean.class);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("toolListModel"), medicToolComboBox));
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    serverDataController, (BeanProperty) BeanProperty.create("diagnosticToolListModel"), diagnosticTable);
            binding.addColumnBinding(BeanProperty.create("name"))
                    .setColumnName("N\u00e1zev")
                    .setColumnClass(String.class);
            JTableBinding.ColumnBinding columnBinding = binding.addColumnBinding(BeanProperty.create("count"))
                    .setColumnName("Celkem")
                    .setColumnClass(Integer.class);
            columnBinding.setValidator(positiveValidator1);
            binding.addColumnBinding(BeanProperty.create("inUse"))
                    .setColumnName("Pou\u017e\u00edv\u00e1 se")
                    .setColumnClass(Integer.class);
            columnBinding = binding.addColumnBinding(BeanProperty.create("diagnosticTime"))
                    .setColumnName("\u010cas diagn\u00f3zy (sec)")
                    .setColumnClass(Long.class);
            columnBinding.setValidator(positiveValidator1);
            binding.addColumnBinding(BeanProperty.create("visible"))
                    .setColumnName("Viditelnost")
                    .setColumnClass(Boolean.class);
            bindingGroup.addBinding(binding);
        }
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                    serverDataController, (BeanProperty) BeanProperty.create("patientListModel"), patientTable);
            binding.addColumnBinding(BeanProperty.create("crewMember"))
                    .setColumnName("Jm\u00e9no")
                    .setColumnClass(CrewMember.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("sickness"))
                    .setColumnName("Nemoc")
                    .setColumnClass(Sickness.class);
            binding.addColumnBinding(BeanProperty.create("status"))
                    .setColumnName("Stav")
                    .setColumnClass(PatientStatus.class);
            binding.addColumnBinding(BeanProperty.create("tool1"))
                    .setColumnName("P\u0159\u00edstroj 1")
                    .setColumnClass(MedicTool.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("tool2"))
                    .setColumnName("P\u0159\u00edstroj 2")
                    .setColumnClass(MedicTool.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("tool3"))
                    .setColumnName("P\u0159\u00edstroj 3")
                    .setColumnClass(MedicTool.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("diagnosticTool"))
                    .setColumnName("Diagnostick\u00fd p\u0159\u00edstroj")
                    .setColumnClass(DiagnosticTool.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("time"))
                    .setColumnName("Zb\u00fdvaj\u00edc\u00ed \u010das")
                    .setColumnClass(Long.class)
                    .setEditable(false);
            binding.addColumnBinding(BeanProperty.create("visible"))
                    .setColumnName("Viditelnost")
                    .setColumnClass(Boolean.class);
            binding.addColumnBinding(BeanProperty.create("timeAble"))
                    .setColumnName("Odpo\u010d\u00edt\u00e1van\u00ed")
                    .setColumnClass(Boolean.class);
            binding.addColumnBinding(BeanProperty.create("deadTime"))
                    .setColumnName("\u010cas do \u00famrt\u00ed (sec)")
                    .setColumnClass(Long.class)
                    .setEditable(false);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    patientTable, BeanProperty.create("selectedElement"),
                    patientDiagnosticButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            binding.setConverter(patientStatusDiagnosticConverter1);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("diagnosticToolListModel"), diagnosticToolComboBox));
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                    patientTable, BeanProperty.create("selectedElement"),
                    patientMedicButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            binding.setConverter(patientStatusDiagnosticConverter1);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                    patientTable, BeanProperty.create("selectedElement"),
                    patientStopButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            binding.setConverter(patientStatusAktivConverter1);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    patientTable, ELProperty.create("${selectedElement != null}"),
                    removePatientButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    sicknessTable, ELProperty.create("${selectedElement != null}"),
                    removeSicknessButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    crewTable, ELProperty.create("${selectedElement != null}"),
                    removeCrewMemberButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    toolTable, ELProperty.create("${selectedElement != null}"),
                    removeMedicToolMemberButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            bindingGroup.addBinding(binding);
        }
        {
            Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    diagnosticTable, ELProperty.create("${selectedElement != null}"),
                    removeDiagnosticToolMemberButton, BeanProperty.create("enabled"));
            binding.setSourceNullValue(false);
            binding.setSourceUnreadableValue(false);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel mainPanel;
    private JTabbedPane tabPanel;
    private JPanel crewsToolsTabPanel;
    private JPanel crewPanel;
    private JToolBar crewToolbar;
    private JButton addCrewButton;
    private JButton removeCrewMemberButton;
    private JScrollPane crewScrollPanel;
    private JTable crewTable;
    private JPanel toolPanel;
    private JToolBar toolToolbar;
    private JButton addToolButton;
    private JButton removeMedicToolMemberButton;
    private JScrollPane toolScrollPanel;
    private JTable toolTable;
    private JPanel diagnosticPanel;
    private JToolBar diagnosticToolbar;
    private JButton addDiagnosticButton;
    private JButton removeDiagnosticToolMemberButton;
    private JScrollPane diagnosticScrollPanel;
    private JTable diagnosticTable;
    private JPanel sicknessToolsTabPanel;
    private JPanel sicknessPanel;
    private JToolBar sicknessToolbar;
    private JButton addSicknessButton;
    private JButton removeSicknessButton;
    private JScrollPane sicknessScrollPanel;
    private JTable sicknessTable;
    private JPanel patientToolsTabPanel;
    private JPanel patientPanel;
    private JToolBar patientToolbar;
    private JButton addPatientButton;
    private JButton removePatientButton;
    private JButton patientDiagnosticButton;
    private JButton patientMedicButton;
    private JButton patientStopButton;
    private JScrollPane patientScrollPanel;
    private JTable patientTable;
    private JToolBar mainToolbar;
    private JButton startServerButton;
    private JButton stopServerButton;
    private JButton saveDataButton;
    private JButton loadDataButton;
    private JButton chatEnableButton;
    private JPanel bottomPanel;
    private JPanel chatPanel;
    private JPanel panel2;
    private JScrollPane chatListPanel;
    private JList chatList;
    private JPanel chatEditPanel;
    private JTextField chatEdit;
    private JButton chatButton;
    private JPanel clientPanel;
    private JScrollPane clinetScrollPane;
    private JTable clientTable;
    private JComboBox medicToolComboBox;
    private ObservableList<server.ServerClient> clientsListModel;
    private ObservableList<java.lang.String> chatListModel;
    private ChatServerData chatServerData;
    private ServerDataController serverDataController;
    private PositiveValidator positiveValidator1;
    private PatientStatusDiagnosticConverter patientStatusDiagnosticConverter1;
    private JComboBox diagnosticToolComboBox;
    private PatientStatusAktivConverter patientStatusAktivConverter1;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
