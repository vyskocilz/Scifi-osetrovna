/*
 * Created by JFormDesigner on Sat Nov 03 23:51:36 CET 2012
 */

package dialogs;

import data.MedicTool;
import main.ServerDataController;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Zdenek Vyskocil
 */
public class MedicsDialog extends JDialog {
    private DialogCallback<MedicTool> dialogCallback;


    public MedicsDialog(Frame owner, ServerDataController controller) {
        super(owner);
        this.serverDataController = controller;
        initComponents();
    }

    public MedicsDialog(Dialog owner) {
        super(owner);
        initComponents();

    }

    public void show(DialogCallback<MedicTool> dialogCallback) {
        this.dialogCallback = dialogCallback;
        medicToolBox1.setSelectedItem(null);
        medicToolBox2.setSelectedItem(null);
        medicToolBox3.setSelectedItem(null);
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
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

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dialogCallback = null;
        setVisible(false);
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if (medicToolBox1.getSelectedIndex() == -1 && medicToolBox2.getSelectedIndex() == -1 && medicToolBox3.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si žádný přístroj");
            return;
        }
        setVisible(false);
        dialogCallback.onSelected((MedicTool) medicToolBox1.getSelectedItem(), (MedicTool) medicToolBox2.getSelectedItem(), (MedicTool) medicToolBox3.getSelectedItem());
        medicToolBox1.setSelectedItem(null);
        medicToolBox2.setSelectedItem(null);
        medicToolBox3.setSelectedItem(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        createUIComponents();

        panel1 = new JPanel();
        label1 = new JLabel();
        medicToolBox1 = new JComboBox();
        medicToolBox2 = new JComboBox();
        label2 = new JLabel();
        medicToolBox3 = new JComboBox();
        label3 = new JLabel();
        panel3 = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("L\u00e9\u010dit pacienta");
        setModal(true);
        setMinimumSize(new Dimension(300, 100));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 256};

            //---- label1 ----
            label1.setText("P\u0159\u00edstroj 1");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- medicToolBox1 ----
            medicToolBox1.setPreferredSize(new Dimension(100, 24));
            medicToolBox1.setMinimumSize(new Dimension(100, 24));
            medicToolBox1.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    comboBoxKeyPressed(e);
                }
            });
            panel1.add(medicToolBox1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- medicToolBox2 ----
            medicToolBox2.setPreferredSize(new Dimension(100, 24));
            medicToolBox2.setMinimumSize(new Dimension(100, 24));
            medicToolBox2.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    comboBoxKeyPressed(e);
                }
            });
            panel1.add(medicToolBox2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- label2 ----
            label2.setText("P\u0159\u00edstroj 2");
            panel1.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- medicToolBox3 ----
            medicToolBox3.setPreferredSize(new Dimension(100, 24));
            medicToolBox3.setMinimumSize(new Dimension(100, 24));
            medicToolBox3.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    comboBoxKeyPressed(e);
                }
            });
            panel1.add(medicToolBox3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- label3 ----
            label3.setText("P\u0159\u00edstroj 3");
            panel1.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, BorderLayout.CENTER);

        //======== panel3 ========
        {
            panel3.setLayout(new GridBagLayout());

            //---- okButton ----
            okButton.setText("L\u00e9\u010dit");
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButtonActionPerformed(e);
                }
            });
            panel3.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- cancelButton ----
            cancelButton.setText("Zru\u0161it");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButtonActionPerformed(e);
                }
            });
            panel3.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
        }
        contentPane.add(panel3, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());

        //---- bindings ----
        bindingGroup = new BindingGroup();
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("toolListModel"), medicToolBox1));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("toolListModel"), medicToolBox2));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("toolListModel"), medicToolBox3));
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JComboBox medicToolBox1;
    private JComboBox medicToolBox2;
    private JLabel label2;
    private JComboBox medicToolBox3;
    private JLabel label3;
    private JPanel panel3;
    private JButton okButton;
    private JButton cancelButton;
    private ServerDataController serverDataController;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
