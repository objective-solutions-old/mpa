package mpa.manager.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import mpa.main.loader.ConfigStream;
import net.miginfocom.swing.MigLayout;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MpaGerar extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textTeams;
	private JTextArea textExpected;
	private ConfigStream params;

	public MpaGerar() {
		setTitle("Parâmetros para gerar MPA");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][grow][]"));
		
		JPanel teamsPanel = new JPanel();
		teamsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Devs: (time / dev1 / dev2...)", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(teamsPanel, "cell 0 0,grow");
		teamsPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		textTeams = new JTextArea();
		teamsPanel.add(textTeams, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fixar duplas: ( + for\u00E7ar / - rejeitar )", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		textExpected = new JTextArea();
		panel.add(textExpected, "cell 0 0,grow");
		
		JButton btnGerar = new JButton("Gerar");
		btnGerar.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			params = new ConfigStream(textTeams.getText(), textExpected.getText());
			dispose();
		}
		});
		contentPane.add(btnGerar, "cell 0 2,alignx right");
	}
	
	public ConfigStream getParams() {
		return params;
	}

}
