/*
 * Created by JFormDesigner on Mon Jan 10 00:15:22 CET 2011
 */

package main;

import action.base.ServerAction;
import client.ClientUtils;
import converter.PatientStatusDiagnosticConverter;
import data.*;
import ini.ClientProperty;
import ini.ErrorLog;
import ini.Theme;
import org.jdesktop.beansbinding.*;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

/**
 * @author Zdenek Vyskocil
 */
public class ClientApplication extends JFrame {

    public static ClientApplication dialog;

    ClientDataController controller;

    public static void main(String[] args) {
        ErrorLog.init(args, "client");
        JFrame.setDefaultLookAndFeelDecorated(true);
        Theme.init();
        dialog = new ClientApplication();
        dialog.setExtendedState(Frame.MAXIMIZED_BOTH);
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        dialog.setVisible(true);

    }


    public ClientApplication() {
        controller = new ClientDataController(this);
        ClientUtils.addClientListener(controller);
        initComponents();
        setAlwaysOnTop(ClientProperty.getOnTop());
        medic1ComboBox.setSelectedIndex(-1);
    }

    public void clearComboBox(ServerAction.DataType type) {
        switch (type) {
            case Sickness:
                sicknessComboBox.setSelectedIndex(-1);
                break;
            case DiagnosticTool:
                diagnosticToolComboBox3.setSelectedIndex(-1);
                break;
            case Crew:
                crewComboBox.setSelectedIndex(-1);
                break;
            case MedicTool:
                medic1ComboBox.setSelectedIndex(-1);
                medic2ComboBox.setSelectedIndex(-1);
                medic3ComboBox.setSelectedIndex(-1);
                break;
        }
    }

    private void thisWindowOpened(WindowEvent e) {
        ClientUtils.start();
    }

    private void thisWindowClosing(WindowEvent e) {
        String pass = JOptionPane.showInputDialog(this, "Zadejte heslo k ukončení", "Konec?", JOptionPane.QUESTION_MESSAGE);
        if (ClientProperty.getClosePassword().equals(pass)) {
            ClientUtils.stop();
            System.exit(0);
        }
    }

    private void chatButtonActionPerformed(ActionEvent e) {
        if (chatEdit.getText().trim().length() > 0) {
            controller.getChatClientData().sendText(chatEdit.getText().trim());
            chatEdit.setText("");
        }
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

    public JPanel getChatPanel() {
        return chatPanel;
    }

    private void chatEditKeyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
            chatButtonActionPerformed(null);
        }
    }

    private void createUIComponents() {

    }

    private void button1ActionPerformed(ActionEvent e) {
        if (crewComboBox.getSelectedIndex() == -1 || sicknessComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si člena posádky nebo nemoc");
            return;
        }
        controller.addCrewMember((CrewMember) crewComboBox.getSelectedItem(), (Sickness) sicknessComboBox.getSelectedItem());
        crewComboBox.setSelectedIndex(-1);
        sicknessComboBox.setSelectedIndex(-1);
    }

    private void button2ActionPerformed(ActionEvent e) {
        if (diagnosticToolComboBox3.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si přístroj");
            return;
        }
        if (patientTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si pacienta");
            return;
        }
        controller.doDiagnostic(controller.getPatient(patientTable.getSelectedRow()), (DiagnosticTool) diagnosticToolComboBox3.getSelectedItem());
        diagnosticToolComboBox3.setSelectedIndex(-1);
        patientTable.clearSelection();
    }

    private void button3ActionPerformed(ActionEvent e) {
        if (medic1ComboBox.getSelectedIndex() == -1 && medic2ComboBox.getSelectedIndex() == -1 && medic3ComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si žádný přístroj");
            return;
        }
        if (patientTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si pacienta");
            return;
        }
        controller.doMedic(controller.getPatient(patientTable.getSelectedRow()),
                (MedicTool) medic1ComboBox.getSelectedItem(),
                (MedicTool) medic2ComboBox.getSelectedItem(),
                (MedicTool) medic3ComboBox.getSelectedItem());
        medic1ComboBox.setSelectedIndex(-1);
        medic2ComboBox.setSelectedIndex(-1);
        medic3ComboBox.setSelectedIndex(-1);
        patientTable.clearSelection();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        scrollPane3 = new JScrollPane();
        panel3 = new JPanel();
        panel4 = new JPanel();
        label1 = new JLabel();
        crewComboBox = new JComboBox();
        label2 = new JLabel();
        sicknessComboBox = new JComboBox();
        button1 = new JButton();
        panel5 = new JPanel();
        label3 = new JLabel();
        diagnosticToolComboBox3 = new JComboBox();
        button2 = new JButton();
        panel6 = new JPanel();
        label4 = new JLabel();
        medic1ComboBox = new JComboBox();
        label5 = new JLabel();
        medic2ComboBox = new JComboBox();
        label6 = new JLabel();
        medic3ComboBox = new JComboBox();
        button3 = new JButton();
        opravnaMainPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        patientTable = new JTable();
        chatPanel = new JPanel();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        list1 = new JList();
        chatEditPanel = new JPanel();
        chatEdit = new JTextField();
        chatButton = new JButton();
        chatListModel = controller.getChatClientData().getChatListModel();
        clientDataController = controller;
        patientStatusDiagnosticConverter1 = new PatientStatusDiagnosticConverter();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());

            //======== scrollPane3 ========
            {
                scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane3.setMinimumSize(new Dimension(200, 8));
                scrollPane3.setMaximumSize(new Dimension(200, 32767));
                scrollPane3.setPreferredSize(new Dimension(200, 385));

                //======== panel3 ========
                {
                    panel3.setMinimumSize(new Dimension(150, 102));
                    panel3.setLayout(new VerticalLayout());

                    //======== panel4 ========
                    {
                        panel4.setBorder(new TitledBorder("P\u0159idat pacienta"));
                        panel4.setLayout(new VerticalLayout());

                        //---- label1 ----
                        label1.setText("Jm\u00e9no");
                        panel4.add(label1);
                        panel4.add(crewComboBox);

                        //---- label2 ----
                        label2.setText("Nemoc");
                        panel4.add(label2);
                        panel4.add(sicknessComboBox);

                        //---- button1 ----
                        button1.setText("P\u0159idat");
                        button1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                button1ActionPerformed(e);
                            }
                        });
                        panel4.add(button1);
                    }
                    panel3.add(panel4);

                    //======== panel5 ========
                    {
                        panel5.setBorder(new TitledBorder("Diagn\u00f3za"));
                        panel5.setLayout(new VerticalLayout());

                        //---- label3 ----
                        label3.setText("P\u0159\u00edstroj");
                        panel5.add(label3);
                        panel5.add(diagnosticToolComboBox3);

                        //---- button2 ----
                        button2.setText("Diagnostikovat");
                        button2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                button2ActionPerformed(e);
                            }
                        });
                        panel5.add(button2);
                    }
                    panel3.add(panel5);

                    //======== panel6 ========
                    {
                        panel6.setBorder(new TitledBorder("L\u00e9\u010dit"));
                        panel6.setLayout(new VerticalLayout());

                        //---- label4 ----
                        label4.setText("P\u0159\u00edstroj 1");
                        panel6.add(label4);

                        //---- medic1ComboBox ----
                        medic1ComboBox.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                comboBoxKeyPressed(e);
                            }
                        });
                        panel6.add(medic1ComboBox);

                        //---- label5 ----
                        label5.setText("P\u0159\u00edstroj 2");
                        panel6.add(label5);

                        //---- medic2ComboBox ----
                        medic2ComboBox.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                comboBoxKeyPressed(e);
                            }
                        });
                        panel6.add(medic2ComboBox);

                        //---- label6 ----
                        label6.setText("P\u0159\u00edstroj 3");
                        panel6.add(label6);

                        //---- medic3ComboBox ----
                        medic3ComboBox.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                comboBoxKeyPressed(e);
                            }
                        });
                        panel6.add(medic3ComboBox);

                        //---- button3 ----
                        button3.setText("L\u00e9\u010dit");
                        button3.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                button3ActionPerformed(e);
                            }
                        });
                        panel6.add(button3);
                    }
                    panel3.add(panel6);
                }
                scrollPane3.setViewportView(panel3);
            }
            panel1.add(scrollPane3, BorderLayout.WEST);

            //======== opravnaMainPanel ========
            {
                opravnaMainPanel.setPreferredSize(new Dimension(600, 0));
                opravnaMainPanel.setLayout(new BorderLayout());

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(patientTable);
                }
                opravnaMainPanel.add(scrollPane1, BorderLayout.CENTER);
            }
            panel1.add(opravnaMainPanel, BorderLayout.CENTER);
        }
        contentPane.add(panel1, BorderLayout.CENTER);

        //======== chatPanel ========
        {
            chatPanel.setBorder(new TitledBorder("Komunik\u00e1tor"));
            chatPanel.setLayout(new BorderLayout());

            //======== panel2 ========
            {
                panel2.setLayout(new BorderLayout());

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(list1);
                }
                panel2.add(scrollPane2, BorderLayout.CENTER);

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
                        @Override
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
        contentPane.add(chatPanel, BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(getOwner());

        //---- bindings ----
        bindingGroup = new BindingGroup();
        bindingGroup.addBinding(SwingBindings.createJListBinding(UpdateStrategy.READ_ONCE,
            chatListModel, list1));
        {
            JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ,
                clientDataController, (BeanProperty) BeanProperty.create("patientListModel"), patientTable);
            binding.setEditable(false);
            binding.addColumnBinding(BeanProperty.create("crewMember"))
                .setColumnName("Jm\u00e9no")
                .setColumnClass(CrewMember.class);
            binding.addColumnBinding(BeanProperty.create("sickness"))
                .setColumnName("Nemoc")
                .setColumnClass(Sickness.class);
            binding.addColumnBinding(BeanProperty.create("status"))
                .setColumnName("Stav")
                .setColumnClass(PatientStatus.class);
            binding.addColumnBinding(BeanProperty.create("deadTime"))
                .setColumnName("\u010cas \u00famrt\u00ed")
                .setColumnClass(Long.class);
            binding.addColumnBinding(BeanProperty.create("time"))
                .setColumnName("\u010cas l\u00e9\u010dby/diag")
                .setColumnClass(Long.class);
            binding.addColumnBinding(BeanProperty.create("diagnosticTool"))
                .setColumnName("Diag. p\u0159\u00edstroj")
                .setColumnClass(DiagnosticTool.class);
            binding.addColumnBinding(BeanProperty.create("tool1"))
                .setColumnName("L\u00e9\u010debn\u00fd p\u0159\u00edstroj")
                .setColumnClass(MedicTool.class);
            binding.addColumnBinding(BeanProperty.create("tool2"))
                .setColumnName("L\u00e9\u010debn\u00fd p\u0159\u00edstroj")
                .setColumnClass(MedicTool.class);
            binding.addColumnBinding(BeanProperty.create("tool3"))
                .setColumnName("L\u00e9\u010debn\u00fd p\u0159\u00edstroj")
                .setColumnClass(MedicTool.class);
            binding.setSourceNullValue(Collections.EMPTY_LIST);
            binding.setSourceUnreadableValue(Collections.EMPTY_LIST);
            bindingGroup.addBinding(binding);
        }
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
            clientDataController, (BeanProperty) BeanProperty.create("crewListModel"), crewComboBox));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
            clientDataController, (BeanProperty) BeanProperty.create("sicknessListModel"), sicknessComboBox));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
            clientDataController, (BeanProperty) BeanProperty.create("diagnosticToolListModel"), diagnosticToolComboBox3));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE,
            clientDataController, (BeanProperty) BeanProperty.create("toolListModel"), medic1ComboBox));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE,
            clientDataController, (BeanProperty) BeanProperty.create("toolListModel"), medic2ComboBox));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
            clientDataController, (BeanProperty) BeanProperty.create("toolListModel"), medic3ComboBox));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
            clientDataController, BeanProperty.create("chatEnableData.enabled"),
            chatPanel, BeanProperty.create("visible")));
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JScrollPane scrollPane3;
    private JPanel panel3;
    private JPanel panel4;
    private JLabel label1;
    private JComboBox crewComboBox;
    private JLabel label2;
    private JComboBox sicknessComboBox;
    private JButton button1;
    private JPanel panel5;
    private JLabel label3;
    private JComboBox diagnosticToolComboBox3;
    private JButton button2;
    private JPanel panel6;
    private JLabel label4;
    private JComboBox medic1ComboBox;
    private JLabel label5;
    private JComboBox medic2ComboBox;
    private JLabel label6;
    private JComboBox medic3ComboBox;
    private JButton button3;
    private JPanel opravnaMainPanel;
    private JScrollPane scrollPane1;
    private JTable patientTable;
    private JPanel chatPanel;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private JList list1;
    private JPanel chatEditPanel;
    private JTextField chatEdit;
    private JButton chatButton;
    private ObservableList<java.lang.String> chatListModel;
    private ClientDataController clientDataController;
    private PatientStatusDiagnosticConverter patientStatusDiagnosticConverter1;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
