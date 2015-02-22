/*
 * Created by JFormDesigner on Sat Nov 03 23:51:36 CET 2012
 */

package dialogs;

import data.DiagnosticTool;
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
public class DiagnosticDialog extends JDialog {
    private DialogCallback<DiagnosticTool> dialogCallback;


    public DiagnosticDialog(Frame owner, ServerDataController controller) {
        super(owner);
        this.serverDataController = controller;
        initComponents();
    }

    public DiagnosticDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    public void show(DialogCallback<DiagnosticTool> dialogCallback) {
        this.dialogCallback = dialogCallback;
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dialogCallback = null;
        setVisible(false);
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if (diagnoticToolBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Nevybral sis přístroj");
            return;
        }
        setVisible(false);
        dialogCallback.onSelected((DiagnosticTool) diagnoticToolBox.getSelectedItem());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        createUIComponents();

        panel1 = new JPanel();
        label1 = new JLabel();
        diagnoticToolBox = new JComboBox();
        panel3 = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Diagnostikovat pacienta");
        setModal(true);
        setMinimumSize(new Dimension(300, 100));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 5, 244};

            //---- label1 ----
            label1.setText("P\u0159\u00edstroj");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- diagnoticToolBox ----
            diagnoticToolBox.setPreferredSize(new Dimension(100, 24));
            diagnoticToolBox.setMinimumSize(new Dimension(100, 24));
            panel1.add(diagnoticToolBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, BorderLayout.CENTER);

        //======== panel3 ========
        {
            panel3.setLayout(new GridBagLayout());

            //---- okButton ----
            okButton.setText("Diagnostikovat");
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
                serverDataController, (BeanProperty) BeanProperty.create("diagnosticToolListModel"), diagnoticToolBox));
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JComboBox diagnoticToolBox;
    private JPanel panel3;
    private JButton okButton;
    private JButton cancelButton;
    private ServerDataController serverDataController;
    private BindingGroup bindingGroup;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
