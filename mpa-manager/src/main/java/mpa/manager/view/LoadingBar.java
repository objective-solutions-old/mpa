package mpa.manager.view;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class LoadingBar extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public LoadingBar(String texto) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 220, 120);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel lblCarregando = new JLabel(texto);
		lblCarregando.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblCarregando, "cell 0 0,alignx center,aligny center");
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true); 
		contentPane.add(progressBar, "cell 0 1,grow");
	}

}
