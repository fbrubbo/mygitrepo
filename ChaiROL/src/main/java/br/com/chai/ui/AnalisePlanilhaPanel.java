package br.com.chai.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AnalisePlanilhaPanel extends JPanel
{
    JTextField filePathTextField = new JTextField();
    JButton openButton = new JButton("Abrir Arquivo");
    JButton goButton = new JButton("Analisar");
    JButton cleanButton = new JButton("Limpar");

    JTextField csvTextFiled = new JTextField();
    private final JLabel lblPlanilha = new JLabel("Planilha Original");
    private final JLabel lblPlanilhaGerada = new JLabel("Planilha Gerada");


    public AnalisePlanilhaPanel()
    {
        setBorder(new TitledBorder(null, "Analise de Planilha (xlsx) extra\u00EDda do SAP", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {5, 358, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
        this.setLayout(gridBagLayout);

        GridBagConstraints gbc_lblPlanilha = new GridBagConstraints();
        gbc_lblPlanilha.insets = new Insets(0, 5, 5, 5);
        gbc_lblPlanilha.anchor = GridBagConstraints.EAST;
        gbc_lblPlanilha.gridx = 0;
        gbc_lblPlanilha.gridy = 0;
        this.add(lblPlanilha, gbc_lblPlanilha);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        this.add(filePathTextField, gbc_textField);
        filePathTextField.setColumns(10);

        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 0;
        this.add(openButton, gbc_btnNewButton);

        GridBagConstraints gbc_lblPlanilhaGerada = new GridBagConstraints();
        gbc_lblPlanilhaGerada.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlanilhaGerada.anchor = GridBagConstraints.EAST;
        gbc_lblPlanilhaGerada.gridx = 0;
        gbc_lblPlanilhaGerada.gridy = 1;
        this.add(lblPlanilhaGerada, gbc_lblPlanilhaGerada);

        GridBagConstraints gbc_textField4 = new GridBagConstraints();
        gbc_textField4.insets = new Insets(0, 0, 5, 5);
        gbc_textField4.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField4.gridx = 1;
        gbc_textField4.gridy = 1;
        this.add(csvTextFiled, gbc_textField4);

                GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
                gbc_btnNewButton_1.anchor = GridBagConstraints.EAST;
                gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
                gbc_btnNewButton_1.gridx = 1;
                gbc_btnNewButton_1.gridy = 2;
                goButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                    }
                });
                this.add(goButton, gbc_btnNewButton_1);

                        GridBagConstraints gbc_textField3 = new GridBagConstraints();
                        gbc_textField3.insets = new Insets(0, 0, 0, 5);
                        gbc_textField3.fill = GridBagConstraints.HORIZONTAL;
                        gbc_textField3.gridx = 2;
                        gbc_textField3.gridy = 2;
                        this.add(cleanButton, gbc_textField3);

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

}
