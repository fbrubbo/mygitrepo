package br.com.chai.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import br.com.chai.domain.YearConsumption;
import br.com.chai.util.NumberUtil;
import br.com.chai.util.StringUtil;

public class ResultPanel extends JPanel {
    private static final String[] HEADERS = new String[] {"Ano", "N\u00BA Meses", "Valor Previsto", "Limite ROL para o ano"};
    private JTable table;

    /**
     * Create the panel.
     */
    public ResultPanel(final List<YearConsumption> years) {
        setBorder(new TitledBorder(null, "Resultado da An\u00E1lise", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        DecimalFormat df = new DecimalFormat("00.00");
        final String[][] lines = new String[years.size()][4];
        for(int i=0; i<years.size(); i++) {
            YearConsumption year = years.get(i);
            lines[i][0] = "" + year.getYear();
            lines[i][1] = NumberUtil.format(year.getNumMonths(), 2);
            lines[i][2] = NumberUtil.format(year.getPredictedConsumption(), 2);
            lines[i][3] = "";
        }

        table = new JTable();

        table.setModel(new DefaultTableModel(lines, HEADERS) {
            @Override
            public Class getColumnClass(final int columnIndex) {
                return String.class;
            }
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return new boolean[] {false, false, false, true}[column];
            }
        });
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(0).setMinWidth(75);
        table.getColumnModel().getColumn(0).setMaxWidth(75);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(1).setMinWidth(75);
        table.getColumnModel().getColumn(1).setMaxWidth(75);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if(column==3) {
                    TableModel model = table.getModel();
                    if (model instanceof DefaultTableModel) {
                        DefaultTableModel myModel = (DefaultTableModel) model;

                        if(!StringUtil.isBlank((String)myModel.getValueAt(row, 3))) {
                            BigDecimal v1 =  NumberUtil.parse((String)myModel.getValueAt(row, 2));
                            BigDecimal v2 =  NumberUtil.parse((String)myModel.getValueAt(row, 3));

                            if(v1.compareTo(v2)<0){
                                setBackground(Color.GREEN);
                            } else {
                                setBackground(Color.RED);
                            }
                        } else {
                            setBackground(Color.WHITE);
                        }
                    }
                } else {
                    setBackground(Color.WHITE);
                }

                return this;
            }
        };
        rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
        table.getColumnModel().getColumn(2).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn(2).setResizable(false);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(200);
        table.getColumnModel().getColumn(2).setMaxWidth(200);
        table.getColumnModel().getColumn(3).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn(3).setResizable(false);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(200);
        table.getColumnModel().getColumn(3).setMaxWidth(200);
        scrollPane.setViewportView(table);


        DefaultCellEditor celEditor = new DefaultCellEditor(new JTextField()){
            @Override
            public Object getCellEditorValue() {
                // throws exception if parsing failes, and it's catched on stopCellEditing
                String str = (String) super.getCellEditorValue();
                if(!StringUtil.isBlank(str)) {
                    BigDecimal parse = NumberUtil.parse(str);
                    return NumberUtil.format(parse, 2);
                }
                return str;
            }

            @Override
            public boolean stopCellEditing() {
                boolean result = false;
                try {
                    result = super.stopCellEditing();
                    ((JTextField) getComponent()).setBackground(Color.WHITE);
                } catch (Exception e) {
                    ((JTextField) getComponent()).setBackground(Color.YELLOW);
                    result = false;
                }
                return result;
            }

            @Override
            public boolean isCellEditable(final EventObject anEvent) {
                // reset color when begin editing
                ((JTextField) getComponent()).setBackground(Color.WHITE);
                return super.isCellEditable(anEvent);
            }
        };
        table.getColumnModel().getColumn(3).setCellEditor(celEditor);



    }

}
