package br.com.chai.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorDialog extends JDialog {

	private static final long serialVersionUID = 4427024507131694145L;
	private final JPanel centerPanel;
	private final JButton btnMore;
	private final JLabel messageError;
	private final JPanel northPanel;
	private final JTextArea textMessage;
	private final JPanel panelCenter;
	private final JScrollPane scrollPane;
	private final JButton btnOk;

	public ErrorDialog(final Component parent, final String message, final String errorMessage) {
		super((JDialog) parent, "Erro!");

		setVisible(true);
		setLocationRelativeTo(null);
		setSize(new Dimension(900, 100));
		getContentPane().setLayout(new BorderLayout(0, 0));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		panelCenter = new JPanel();
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));

		northPanel = new JPanel();
		panelCenter.add(northPanel, BorderLayout.NORTH);
		GridBagLayout gbl_northPanel = new GridBagLayout();
		gbl_northPanel.columnWidths = new int[]{0, 0, 0};
		gbl_northPanel.rowHeights = new int[]{0, 0, 0};
		gbl_northPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_northPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		northPanel.setLayout(gbl_northPanel);

		messageError = new JLabel(errorMessage);
		GridBagConstraints gbc_lblOcorreuUmErro = new GridBagConstraints();
		gbc_lblOcorreuUmErro.insets = new Insets(5, 5, 5, 5);
		gbc_lblOcorreuUmErro.gridx = 0;
		gbc_lblOcorreuUmErro.gridy = 0;
		northPanel.add(messageError, gbc_lblOcorreuUmErro);

		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ErrorDialog.this.dispose();
			}
		});
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(5, 5, 5, 5);
		gbc_btnOk.gridx = 0;
		gbc_btnOk.gridy = 1;
		northPanel.add(btnOk, gbc_btnOk);
		SwingUtil.requestFocus(btnOk);

		btnMore = new JButton(">>");
		GridBagConstraints gbc_btnDetails = new GridBagConstraints();
		gbc_btnDetails.gridx = 1;
		gbc_btnDetails.gridy = 1;
		northPanel.add(btnMore, gbc_btnDetails);

		centerPanel = new JPanel();
		panelCenter.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setVisible(!centerPanel.isVisible());
		centerPanel.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();

		textMessage = new JTextArea();
		textMessage.setEditable(false);
		textMessage.setText(message);

		scrollPane.setViewportView(textMessage);

		centerPanel.add(scrollPane);

		btnMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				centerPanel.setVisible(centerPanel.isVisible() ? false : true);
				btnMore.setText(btnMore.getText().equals(">>") ? "<<" : ">>");
				if (centerPanel.isVisible()) {
					setSize(new Dimension(900, 600));
				} else {
					setSize(new Dimension(900, 100));
				}
			}
		});

	}

}
