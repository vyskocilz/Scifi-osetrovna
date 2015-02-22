/*
 * Created by JFormDesigner on Sat Nov 03 23:51:36 CET 2012
 */

package dialogs;

import data.CrewMember;
import data.Sickness;
import main.ServerDataController;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Zdenek Vyskocil
 */
public class PatientAddDialog extends JDialog {


    public PatientAddDialog(Frame owner, ServerDataController controller) {
        super(owner);
        this.serverDataController = controller;
        initComponents();
    }

    public PatientAddDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        setVisible(false);
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if (memberBox.getSelectedIndex() == -1 || sicknessBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral si pacienta nebo nemoc");
            return;
        }
        serverDataController.addPatient((CrewMember) memberBox.getSelectedItem(), (Sickness) sicknessBox.getSelectedItem(), false);
        setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        createUIComponents();

        panel1 = new JPanel();
        label1 = new JLabel();
        memberBox = new JComboBox();
        label2 = new JLabel();
        sicknessBox = new JComboBox();
        panel3 = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("P\u0159\u00eddat pacienta");
        setModal(true);
        setMinimumSize(new Dimension(300, 100));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 5, 234};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 5, 0};

            //---- label1 ----
            label1.setText("Jm\u00e9no");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- memberBox ----
            memberBox.setPreferredSize(new Dimension(100, 24));
            memberBox.setMinimumSize(new Dimension(100, 24));
            panel1.add(memberBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- label2 ----
            label2.setText("Nemoc");
            panel1.add(label2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- sicknessBox ----
            sicknessBox.setCursor(null);
            sicknessBox.setMinimumSize(new Dimension(100, 24));
            panel1.add(sicknessBox, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, BorderLayout.CENTER);

        //======== panel3 ========
        {
            panel3.setLayout(new GridBagLayout());

            //---- okButton ----
            okButton.setText("P\u0159idat");
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
                serverDataController, (BeanProperty) BeanProperty.create("crewListModel"), memberBox));
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                serverDataController, (BeanProperty) BeanProperty.create("sicknessListModel"), sicknessBox));
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JComboBox memberBox;
    private JLabel label2;
    private JComboBox sicknessBox;
    private JPanel panel3;
    private JButton okButton;
    private JButton cancelButton;
    private ServerDataController serverDataController;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
