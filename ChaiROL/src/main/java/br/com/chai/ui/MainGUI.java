package br.com.chai.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainGUI extends JFrame {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu mnSelecionar = new JMenu("Selecionar");
    private final JMenuItem mntmAnalisenica = new JMenuItem("Analise \u00DAnica");
    private final JMenuItem mntmAnaliseDePlanilha = new JMenuItem("Analise de Planilha (xlsx) extra\u00EDda do SAP");


    public MainGUI()
    {
        super("Facilitador de Análise ROL");
        this.setSize(600, 200);
        this.setMinimumSize(new Dimension(600, 200));
        SwingUtil.setDefaultUncaughtExceptionHandler();
        SwingUtil.centralize(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().add(new AnaliseUnicaPanel());

        setJMenuBar(menuBar);
        menuBar.add(mnSelecionar);
        mnSelecionar.add(mntmAnalisenica);
        mntmAnalisenica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                MainGUI.this.getContentPane().removeAll();
                MainGUI.this.getContentPane().add(new AnaliseUnicaPanel());
                MainGUI.this.getContentPane().revalidate();
            }
        });
        mnSelecionar.add(mntmAnaliseDePlanilha);
        mntmAnaliseDePlanilha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                MainGUI.this.getContentPane().removeAll();
                MainGUI.this.getContentPane().add(new AnalisePlanilhaPanel());
                MainGUI.this.getContentPane().revalidate();
            }
        });

    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainGUI frame = new MainGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
