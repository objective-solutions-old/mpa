package mpa.manager.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

	public MpaGerar(String teams) {
		setTitle("Parâmetros para gerar MPA");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][grow][]"));
		
		JPanel teamsPanel = new JPanel();
		teamsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Devs: (equipe / dev1 / dev2...)", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(teamsPanel, "cell 0 0,grow");
		teamsPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		textTeams = new JTextArea(teams);
		
		JScrollPane scrollTeams = new JScrollPane(textTeams, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		teamsPanel.add(scrollTeams, "cell 0 0,grow");
		
		JPanel expectedPanel = new JPanel();
		expectedPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fixar duplas: ( + for\u00E7ar / - rejeitar )", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(expectedPanel, "cell 0 1,grow");
		expectedPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		textExpected = new JTextArea();
		
		JScrollPane scrollExpected = new JScrollPane(textExpected, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		expectedPanel.add(scrollExpected, "cell 0 0,grow");
		
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
