package mpa.manager.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mpa.manager.bean.Mesa;
import mpa.manager.bean.MpaConfiguracao;
import mpa.manager.bean.Objectiviano;
import mpa.manager.control.MpaControl;


public class MpaAtual extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField mesaNumero; 
	private JComboBox dev1;
	private JComboBox dev2;
	private MpaControl controller;
	private JList mesas;
	private Mesa mesaSelecionada;
	private JComboBox mpas;
	private JButton btnSalvaMesa;
	private JButton btnNovoMpa;
	private JButton btnClonarMpa;
	private JButton btnExcluiMesa;
	private JScrollPane scrollMesas;
	private JButton btnNovaMesa;

	public MpaAtual() {
		setTitle("Mpa Manager");
		controller = new MpaControl();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				controller.fechaConexao();
			}
		});
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 529);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel mesaPanel = new JPanel();
		mesaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mesaPanel.setBounds(10, 415, 424, 82);
		contentPane.add(mesaPanel);
		mesaPanel.setLayout(null);
		
		mesaNumero = new JTextField();
		mesaNumero.setHorizontalAlignment(SwingConstants.CENTER);
		mesaNumero.setEditable(false);
		mesaNumero.setBounds(10, 23, 23, 20);
		mesaPanel.add(mesaNumero);
		mesaNumero.setColumns(10);
		
		dev1 = new JComboBox(controller.getObjectivianos().toArray());
		dev1.setBounds(43, 23, 175, 20);
		dev1.setSelectedIndex(-1);
		mesaPanel.add(dev1);
		
		ArrayList<Objectiviano> obj = new ArrayList<Objectiviano>();
		obj.add(null);
		obj.addAll(controller.getObjectivianos());
		dev2 = new JComboBox(obj.toArray());
		dev2.setBounds(239, 23, 175, 20);
		dev2.setSelectedIndex(-1);
		mesaPanel.add(dev2);
		
		btnNovaMesa = new JButton("Nova Mesa");
		btnNovaMesa.setEnabled(false);
		btnNovaMesa.setBounds(10, 48, 120, 23);
		btnNovaMesa.setToolTipText("Cria uma nova mesa no mpa selecionado.");
		btnNovaMesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novaMesa();
			}
		});
		mesaPanel.add(btnNovaMesa);
		
		btnSalvaMesa = new JButton("Salva Mesa");
		btnSalvaMesa.setBounds(151, 48, 120, 23);
		btnSalvaMesa.setToolTipText("Atualiza uma mesa, salvando mesas novas ou alteradas.");
		btnSalvaMesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (mesaSelecionada != null)
						updateMesaSelecionada();
					else
						salvaNovaMesa();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		mesaPanel.add(btnSalvaMesa);
		
		btnExcluiMesa = new JButton("Exclui Mesa");
		btnExcluiMesa.setBounds(294, 48, 120, 23);
		btnExcluiMesa.setToolTipText("Exclui uma mesa selecionada.");
		btnExcluiMesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resposta = JOptionPane.showConfirmDialog(null, "Dejesa realmente apagar a mesa?", "", JOptionPane.YES_NO_OPTION);
				if (resposta == 1) return;
				
				try {
					excluiMesa();
					preencheMesasComMpaSelecionado(getSelectedMpa());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mesaPanel.add(btnExcluiMesa);
		
		mpas = new JComboBox();
		mpas.setToolTipText("Atualiza os mpas buscando do banco.");
		mpas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preencheMesasComMpaSelecionado(getSelectedMpa());
			}
		});
		mpas.setBounds(10, 11, 424, 20);
		contentPane.add(mpas);
	
		mesas = new JList();
		mesas.setBounds(5, 44, 418, 307);
		mesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mesas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				populaCamposEdicao();
			}
		});
		
		scrollMesas = new JScrollPane(mesas, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMesas.setSize(424, 307);
		scrollMesas.setLocation(10, 97);
		contentPane.add(scrollMesas);
		
		JPanel mpaPanel = new JPanel();
		mpaPanel.setBorder(new TitledBorder(null, "Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mpaPanel.setBounds(10, 40, 424, 52);
		contentPane.add(mpaPanel);
		
		btnNovoMpa = new JButton("Novo MPA");
		btnNovoMpa.setToolTipText("Cria um novo mpa com mesas.");
		btnNovoMpa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreTelaMpaNovo();
			}
		});
		
		btnClonarMpa = new JButton("Clonar MPA");
		btnClonarMpa.setToolTipText("Cria um novo mpa com as mesas atuais.");
		btnClonarMpa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					abreTelaMpaNovo(controller.getDevs(getSelectedMpa()));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		GroupLayout gl_mpaPanel = new GroupLayout(mpaPanel);
		gl_mpaPanel.setHorizontalGroup(
			gl_mpaPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mpaPanel.createSequentialGroup()
					.addGap(7)
					.addComponent(btnNovoMpa)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClonarMpa)
					.addGap(231))
		);
		gl_mpaPanel.setVerticalGroup(
			gl_mpaPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mpaPanel.createSequentialGroup()
					.addGroup(gl_mpaPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNovoMpa)
						.addComponent(btnClonarMpa))
					.addContainerGap(7, Short.MAX_VALUE))
		);
		
		mpaPanel.setLayout(gl_mpaPanel);
		
		refreshMpas();
		mpas.setSelectedItem(controller.getMpaAtual());
		
		preencheMesasComMpaSelecionado(controller.getMpaAtual());
	}
	
	private void preencheMpas() {
		mpas.removeAllItems();
		for (MpaConfiguracao mpaConf : controller.getMpasDisponiveis())
			mpas.addItem(mpaConf);
	}

	private void populaCamposEdicao(){
		mesaSelecionada = (Mesa) mesas.getSelectedValue();
		if (mesaSelecionada == null){
			limpaCamposMesa();
			return;
		}
		habilitaEdicaoDeMesas(true);
		mesaNumero.setText(String.valueOf(mesaSelecionada.getNumero()));
		dev1.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getPrimeiroObjectiviano()));
		dev2.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getSegundoObjectiviano()));
	}
	
	private void limpaCamposMesa() {
		mesaNumero.setText("");
		dev1.setSelectedItem(null);
		dev2.setSelectedItem(null);
	}

	private void updateMesaSelecionada() throws SQLException{
		if (mesaSelecionada == null) return;
		mesaSelecionada.setPrimeiroObjectiviano((Objectiviano)dev1.getSelectedItem());
		mesaSelecionada.setSegundoObjectiviano((Objectiviano)dev2.getSelectedItem());
		controller.updateMesa(mesaSelecionada);
		mesas.setSelectedValue(mesaSelecionada, false);
	}
	
	private void abreTelaMpaNovo() {
		abreTelaMpaNovo(null);
	}
	
	private void abreTelaMpaNovo(String devs) {
		try {
			MpaNovo mpaNovo = new MpaNovo(controller.getMaiorMpa().getDataFim(), devs);
			mpaNovo.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					refreshMpas();
					mpas.setSelectedItem(controller.getMpaAtual());
				}
			});
			mpaNovo.setVisible(true);	
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void preencheMesasComMpaSelecionado(MpaConfiguracao mpa) {
		if (mpa == null) return;
		
		boolean isPassado = mpa.getDataFim().before(new Date());
		mesas.setEnabled(!isPassado);
		btnNovaMesa.setEnabled(!isPassado);
		
		habilitaEdicaoDeMesas(false);
		
		try {
			mesas.setListData(controller.getMesas(mpa).toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void habilitaEdicaoDeMesas(boolean b) {
		dev1.setEnabled(b);
		dev2.setEnabled(b);
		btnSalvaMesa.setEnabled(b);
		btnExcluiMesa.setEnabled(b);
	}
	
	private void novaMesa() {
		mesas.clearSelection();
		mesaSelecionada = null;
		limpaCamposMesa();
		dev1.setEnabled(true);
		dev2.setEnabled(true);
		btnSalvaMesa.setEnabled(true);
		btnExcluiMesa.setEnabled(false);
		
		atribuiNumeroDaMesa();
	}
	
	private void atribuiNumeroDaMesa() {
		Integer numeroProximaMesa = mesas.getModel().getSize() + 1;
		mesaNumero.setText(numeroProximaMesa.toString());
	}

	private void salvaNovaMesa() throws NumberFormatException, SQLException{
		controller.criaMesa(getSelectedMpa(),
				Integer.parseInt(mesaNumero.getText()),
				(Objectiviano)dev1.getSelectedItem(),
				(Objectiviano)dev2.getSelectedItem());
		preencheMesasComMpaSelecionado(getSelectedMpa());
	}

	private void refreshMpas() {
		preencheMpas();
		habilitaEdicaoDeMesas(false);
	}
	
	private void excluiMesa() throws SQLException {
		Mesa mesa = (Mesa)mesas.getSelectedValue();
		controller.excluiMesa(mesa);
		controller.atualizaNumeroDasMesas(mesa);
	}

	private MpaConfiguracao getSelectedMpa() {
		return (MpaConfiguracao) mpas.getSelectedItem();
	}
}
