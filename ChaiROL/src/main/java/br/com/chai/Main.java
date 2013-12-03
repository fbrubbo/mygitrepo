package br.com.chai;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame
{
    JTextField filePathTextField = new JTextField();
    JButton openButton = new JButton("Abrir Arquivo");
    JButton goButton = new JButton("Analisar");

    JTextField workingFolderTextFiled = new JTextField();
    JButton cleanButton = new JButton("Limpar");

    JTextField csvTextFiled = new JTextField();


    public Main()
    {
        centralize(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{358, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 1;
        add(filePathTextField, gbc_textField);
        filePathTextField.setColumns(10);

        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton.gridx = 1;
        gbc_btnNewButton.gridy = 1;
        add(openButton, gbc_btnNewButton);

        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_1.gridx = 1;
        gbc_btnNewButton_1.gridy = 2;
        add(goButton, gbc_btnNewButton_1);


        GridBagConstraints gbc_textField2 = new GridBagConstraints();
        gbc_textField2.insets = new Insets(0, 0, 5, 5);
        gbc_textField2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField2.gridx = 0;
        gbc_textField2.gridy = 3;
        add(workingFolderTextFiled, gbc_textField2);

        GridBagConstraints gbc_textField3 = new GridBagConstraints();
        gbc_textField3.insets = new Insets(0, 0, 5, 5);
        gbc_textField3.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField3.gridx = 1;
        gbc_textField3.gridy = 3;
        add(cleanButton, gbc_textField3);

        GridBagConstraints gbc_textField4 = new GridBagConstraints();
        gbc_textField4.insets = new Insets(0, 0, 5, 5);
        gbc_textField4.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField4.gridx = 0;
        gbc_textField4.gridy = 4;
        add(csvTextFiled, gbc_textField4);

        openButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
                chooser.addChoosableFileFilter(filter);
                int returnVal = chooser.showOpenDialog(openButton);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
//                    planilha = new Planilha(file);

                    filePathTextField.setText(file.getAbsolutePath());
                    workingFolderTextFiled.setText(file.getAbsolutePath());
                    csvTextFiled.setText("");
                }
            }
        });

//        goButton.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(final ActionEvent e)
//            {
//                try
//                {
//                    if(planilha!=null){
//                        planilha.toPAA(datePicker.getDate());
//                        JOptionPane.showMessageDialog(PlanilhaGUI.this, "Feito");
//
//                        String csvPath = planilha.getNewCSVFilePath();
//                        csvTextFiled.setText(csvPath);
//                    }
//                }
//                catch(Exception e1)
//                {
//                    JOptionPane.showMessageDialog(PlanilhaGUI.this, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//                    e1.printStackTrace();
//                }
//            }
//        });

//        cleanButton.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(final ActionEvent e)
//            {
//                try
//                {
//                    if(planilha!=null){
//                        planilha.cleanWorkingFolder();
//                        csvTextFiled.setText("");
//                        JOptionPane.showMessageDialog(PlanilhaGUI.this, "cleaned!");
//                    }
//                }
//                catch(Exception e1)
//                {
//                    JOptionPane.showMessageDialog(PlanilhaGUI.this, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//                    e1.printStackTrace();
//                }
//            }
//        });
    }

    public static void centralize(final Component comp) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = comp.getWidth();
        int height = comp.getHeight();
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;

        Rectangle bounds = new Rectangle(x, y, width, height);
        comp.setBounds(bounds);
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    //System.setProperty("user.timezone","GMT-03");
                    Main frame = new Main();
                    frame.setSize(600, 200);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
