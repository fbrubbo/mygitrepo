package br.com.chai.ui;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import org.jdesktop.swingx.JXDatePicker;

import br.com.chai.domain.Contract;
import br.com.chai.domain.YearConsumption;
import br.com.chai.util.DateUtil;

public class AnaliseUnicaPanel extends JPanel
{
    final JXDatePicker beginDatePicker = new JXDatePicker();
    final JXDatePicker endDatePicker = new JXDatePicker();
    JFormattedTextField filePathTextField = null;

    JButton analisar = new JButton("Analisar");
    private final JLabel lblDataInicial = new JLabel("Data Inicial");
    private final JLabel lblDataFinal = new JLabel("Data Final");
    private final JLabel lblValor = new JLabel("Valor");

    private static JFormattedTextField buildFormattedTextFieldNumber() {
        String DEFAULT_MASK = "###,###,###,##0.00";
        DecimalFormat DEFAULT_FORMAT = new DecimalFormat(DEFAULT_MASK);

        NumberFormatter formatter = new NumberFormatter(DEFAULT_FORMAT);
        formatter.setValueClass(Double.class);
        JFormattedTextField input = new JFormattedTextField(formatter);
        input.setFocusLostBehavior(JFormattedTextField.COMMIT);
        return input;
    }


    public AnaliseUnicaPanel()
    {
        setBorder(new TitledBorder(null, "Analise \u00DAnica", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{88, 154, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        this.setLayout(gridBagLayout);

                        GridBagConstraints gbc_lblDataInicial = new GridBagConstraints();
                        gbc_lblDataInicial.insets = new Insets(0, 0, 5, 5);
                        gbc_lblDataInicial.anchor = GridBagConstraints.EAST;
                        gbc_lblDataInicial.gridx = 0;
                        gbc_lblDataInicial.gridy = 0;
                        this.add(lblDataInicial, gbc_lblDataInicial);

                        GridBagConstraints gbc_beginDatePicker = new GridBagConstraints();
                        gbc_beginDatePicker.insets = new Insets(0, 0, 5, 0);
                        gbc_beginDatePicker.fill = GridBagConstraints.HORIZONTAL;
                        gbc_beginDatePicker.gridx = 1;
                        gbc_beginDatePicker.gridy = 0;
                        this.add(beginDatePicker, gbc_beginDatePicker);

                GridBagConstraints gbc_lblDataFinal = new GridBagConstraints();
                gbc_lblDataFinal.insets = new Insets(0, 0, 5, 5);
                gbc_lblDataFinal.anchor = GridBagConstraints.EAST;
                gbc_lblDataFinal.gridx = 0;
                gbc_lblDataFinal.gridy = 1;
                this.add(lblDataFinal, gbc_lblDataFinal);
                beginDatePicker.setFormats(DateFormat.getDateInstance(), new SimpleDateFormat("dd/MM/yyyy"));

                GridBagConstraints gbc_endDatePicker = new GridBagConstraints();
                gbc_endDatePicker.fill = GridBagConstraints.HORIZONTAL;
                gbc_endDatePicker.insets = new Insets(0, 0, 5, 0);
                gbc_endDatePicker.gridx = 1;
                gbc_endDatePicker.gridy = 1;
                this.add(endDatePicker, gbc_endDatePicker);

                GridBagConstraints gbc_lblValor = new GridBagConstraints();
                gbc_lblValor.insets = new Insets(0, 0, 5, 5);
                gbc_lblValor.anchor = GridBagConstraints.EAST;
                gbc_lblValor.gridx = 0;
                gbc_lblValor.gridy = 2;
                this.add(lblValor, gbc_lblValor);
                endDatePicker.setFormats(DateFormat.getDateInstance(), new SimpleDateFormat("dd/MM/yyyy"));

                filePathTextField = buildFormattedTextFieldNumber();
                GridBagConstraints gbc_filePathTextField = new GridBagConstraints();
                gbc_filePathTextField.insets = new Insets(0, 0, 5, 0);
                gbc_filePathTextField.fill = GridBagConstraints.HORIZONTAL;

                        gbc_filePathTextField.gridx = 1;
                        gbc_filePathTextField.gridy = 2;
                        this.add(filePathTextField, gbc_filePathTextField);


                                        GridBagConstraints gbc_analisar = new GridBagConstraints();
                                        gbc_analisar.insets = new Insets(0, 0, 5, 0);
                                        gbc_analisar.anchor = GridBagConstraints.EAST;
                                        gbc_analisar.gridx = 1;
                                        gbc_analisar.gridy = 3;
                                        this.add(analisar, gbc_analisar);


        analisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(beginDatePicker.getDate()==null){
                    throw new RuntimeException("O valor da data inicial é inválido!");
                }
                if(endDatePicker.getDate()==null){
                    throw new RuntimeException("O valor da data final é inválido!");
                }

                Contract c = new Contract();
                c.setInicioConsumoROL(DateUtil.toCalendarBegin(beginDatePicker.getDate()));
                c.setFimConsumoROL(DateUtil.toCalendarEnd(endDatePicker.getDate()));
                c.setVlrFixo(filePathTextField.getText());

                List<YearConsumption> years = c.getYearConsumption();
//                StringBuilder builder = new StringBuilder();
//                for(YearConsumption year : years) {
//                    builder.append(year).append("\n");
//                }
//                JOptionPane.showMessageDialog(AnaliseUnicaPanel.this, builder.toString());

                JDialog d = new JDialog();
                d.setModal(true);
                d.setSize(new Dimension(580, 250));
                d.getContentPane().setLayout(new BorderLayout(0, 0));
                d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                d.add(new ResultPanel(years));
                d.setLocationRelativeTo(AnaliseUnicaPanel.this);
                d.setVisible(true);

            }
        });
    }
}



