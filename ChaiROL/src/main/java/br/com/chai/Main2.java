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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import br.com.chai.domain.Contract;
import br.com.chai.domain.YearConsumption;
import br.com.chai.util.DateUtil;

public class Main2 extends JFrame
{
    final JXDatePicker beginDatePicker = new JXDatePicker();
    final JXDatePicker endDatePicker = new JXDatePicker();
    final JTextField filePathTextField = new JTextField();

    JButton analisar = new JButton("Analisar");


    public Main2()
    {
        centralize(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        GridBagConstraints gbc_beginDatePicker = new GridBagConstraints();
        gbc_beginDatePicker.insets = new Insets(0, 0, 5, 5);
        gbc_beginDatePicker.fill = GridBagConstraints.HORIZONTAL;
        gbc_beginDatePicker.gridx = 0;
        gbc_beginDatePicker.gridy = 1;
        add(beginDatePicker, gbc_beginDatePicker);
        beginDatePicker.setFormats(DateFormat.getDateInstance(), new SimpleDateFormat("dd/MM/yyyy"));

        GridBagConstraints gbc_endDatePicker = new GridBagConstraints();
        gbc_endDatePicker.fill = GridBagConstraints.HORIZONTAL;
        gbc_endDatePicker.insets = new Insets(0, 0, 5, 5);
        gbc_endDatePicker.gridx = 1;
        gbc_endDatePicker.gridy = 1;
        add(endDatePicker, gbc_endDatePicker);
        endDatePicker.setFormats(DateFormat.getDateInstance(), new SimpleDateFormat("dd/MM/yyyy"));

        GridBagConstraints gbc_filePathTextField = new GridBagConstraints();
        gbc_filePathTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_endDatePicker.insets = new Insets(0, 0, 5, 0);
        gbc_filePathTextField.gridx = 2;
        gbc_filePathTextField.gridy = 1;
        add(filePathTextField, gbc_filePathTextField);


        GridBagConstraints gbc_analisar = new GridBagConstraints();
        gbc_analisar.insets = new Insets(0, 0, 5, 0);
        gbc_analisar.fill = GridBagConstraints.HORIZONTAL;
        gbc_analisar.gridx = 2;
        gbc_analisar.gridy = 2;
        add(analisar, gbc_analisar);

        analisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Contract c = new Contract();
                c.setInicioConsumoROL(DateUtil.toCalendarBegin(beginDatePicker.getDate()));
                c.setFimConsumoROL(DateUtil.toCalendarEnd(endDatePicker.getDate()));
                c.setVlrFixo(filePathTextField.getText());

//                c1.setInicioConsumoROL("01.01.2011");   //01.01.2011 00:00:00
//                c1.setFimConsumoROL("31.03.2013");      //31.03.2013 23:59:59
//                c1.setVlrFixo("2.665.205,97");

                List<YearConsumption> years = c.getYearConsumption();
                StringBuilder builder = new StringBuilder();
                for(YearConsumption year : years) {
                    builder.append(year).append("\n");
                }
                JOptionPane.showMessageDialog(Main2.this, builder.toString());

            }
        });
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
                    Main2 frame = new Main2();
                    frame.setSize(600, 200);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
