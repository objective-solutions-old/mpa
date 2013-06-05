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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
import net.miginfocom.swing.MigLayout;


public class MpaManager extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField numMesa; 
	private JComboBox cbDev1;
	private JComboBox cbDev2;
	private MpaControl controller;
	private JList listMesas;
	private Mesa mesaSelecionada;
	private JComboBox cbMpas;
	private JButton btnMesaSave;
	private JButton btnMpaNew;
	private JButton btnMpaClone;
	private JButton btnMesaDelete;
	private JScrollPane scrollMesas;
	private JButton btnMesaNew;

	public MpaManager() {
		controller = new MpaControl();

		setResizable(false);
		setTitle("Mpa Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 529);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				controller.fechaConexao();
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel mpaPanel = new JPanel();
		mpaPanel.setBorder(new TitledBorder(null, "Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mpaPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		mpaPanel.setBounds(10, 0, 424, 88);
		contentPane.add(mpaPanel);
		
		btnMpaNew = new JButton("Novo MPA");
		btnMpaNew.setToolTipText("Cria um novo mpa com mesas.");
		btnMpaNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreTelaMpaNovo(null);
			}
		});
		
		btnMpaClone = new JButton("Clonar MPA");
		btnMpaClone.setToolTipText("Cria um novo mpa com as mesas atuais.");
		btnMpaClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					abreTelaMpaNovo(controller.getDevs(getSelectedMpa()));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		cbMpas = new JComboBox();
		cbMpas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preencheMesasComMpaSelecionado(getSelectedMpa());
			}
		});
		mpaPanel.add(cbMpas, "cell 0 0,growx,aligny top");
		mpaPanel.add(btnMpaNew, "flowx,cell 0 1,alignx left,aligny top");
		mpaPanel.add(btnMpaClone, "cell 0 1,alignx left,aligny top");
		
		JPanel mesasPanel = new JPanel();
		mesasPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesas do Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mesasPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		mesasPanel.setBounds(10, 88, 424, 330);
		contentPane.add(mesasPanel);
		
		listMesas = new JList();
		listMesas.setBounds(5, 44, 418, 307);
		listMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMesas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				populaCamposEdicao();
			}
		});
		
		scrollMesas = new JScrollPane(listMesas, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mesasPanel.add(scrollMesas, "cell 0 0,grow");
		
		JPanel mesaPanel = new JPanel();
		mesaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mesaPanel.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		mesaPanel.setBounds(10, 415, 424, 82);
		contentPane.add(mesaPanel);
		
		numMesa = new JTextField();
		numMesa.setHorizontalAlignment(SwingConstants.CENTER);
		numMesa.setEditable(false);
		numMesa.setColumns(2);
		
		cbDev1 = new JComboBox(controller.getObjectivianos().toArray());
		cbDev1.setSelectedIndex(-1);
		
		ArrayList<Objectiviano> obj = new ArrayList<Objectiviano>();
		obj.add(null);
		obj.addAll(controller.getObjectivianos());
		cbDev2 = new JComboBox(obj.toArray());
		cbDev2.setSelectedIndex(-1);
		mesaPanel.add(numMesa, "cell 0 0,aligny top");
		mesaPanel.add(cbDev1, "cell 0 0,growx,aligny top");
		mesaPanel.add(cbDev2, "cell 0 0,growx,aligny top");
		
		btnMesaNew = new JButton("Nova Mesa");
		btnMesaNew.setEnabled(false);
		btnMesaNew.setToolTipText("Cria uma nova mesa no mpa selecionado.");
		btnMesaNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novaMesa();
			}
		});
		mesaPanel.add(btnMesaNew, "flowx,cell 0 1,growx,aligny top");
		
		btnMesaSave = new JButton("Salva Mesa");
		btnMesaSave.setToolTipText("Atualiza uma mesa, salvando mesas novas ou alteradas.");
		btnMesaSave.addActionListener(new ActionListener() {
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
		mesaPanel.add(btnMesaSave, "cell 0 1,growx,aligny top");
		
		btnMesaDelete = new JButton("Exclui Mesa");
		btnMesaDelete.setToolTipText("Exclui uma mesa selecionada.");
		btnMesaDelete.addActionListener(new ActionListener() {
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
		mesaPanel.add(btnMesaDelete, "cell 0 1,growx,aligny top");
		
		refreshMpas();
	}
	
	private void preencheMpas() {
		cbMpas.removeAllItems();
		for (MpaConfiguracao mpaConf : controller.getMpasDisponiveis())
			cbMpas.addItem(mpaConf);
	}

	private void populaCamposEdicao(){
		mesaSelecionada = (Mesa) listMesas.getSelectedValue();
		if (mesaSelecionada == null){
			limpaCamposMesa();
			return;
		}
		habilitaEdicaoDeMesas(true);
		numMesa.setText(String.valueOf(mesaSelecionada.getNumero()));
		cbDev1.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getPrimeiroObjectiviano()));
		cbDev2.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getSegundoObjectiviano()));
	}
	
	private void limpaCamposMesa() {
		numMesa.setText("");
		cbDev1.setSelectedItem(null);
		cbDev2.setSelectedItem(null);
	}

	private void updateMesaSelecionada() throws SQLException{
		if (mesaSelecionada == null) return;
		mesaSelecionada.setPrimeiroObjectiviano((Objectiviano)cbDev1.getSelectedItem());
		mesaSelecionada.setSegundoObjectiviano((Objectiviano)cbDev2.getSelectedItem());
		controller.updateMesa(mesaSelecionada);
		listMesas.setSelectedValue(mesaSelecionada, false);
	}
	
	private void abreTelaMpaNovo(String devs) {
		try {
			MpaNovo mpaNovo = new MpaNovo(controller.getMaiorMpa().getDataFim(), devs);
			mpaNovo.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					refreshMpas();
				}
			});
			mpaNovo.setVisible(true);	
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void preencheMesasComMpaSelecionado(MpaConfiguracao mpa) {
		if (mpa == null) {
			listMesas.setListData(new Object[] {});
			return;
		}
		
		boolean isPassado = mpa.getDataFim().before(new Date());
		listMesas.setEnabled(!isPassado);
		btnMesaNew.setEnabled(!isPassado);
		
		habilitaEdicaoDeMesas(false);
		
		try {
			listMesas.setListData(controller.getMesas(mpa).toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void habilitaEdicaoDeMesas(boolean b) {
		cbDev1.setEnabled(b);
		cbDev2.setEnabled(b);
		btnMesaSave.setEnabled(b);
		btnMesaDelete.setEnabled(b);
	}
	
	private void novaMesa() {
		listMesas.clearSelection();
		mesaSelecionada = null;
		limpaCamposMesa();
		cbDev1.setEnabled(true);
		cbDev2.setEnabled(true);
		btnMesaSave.setEnabled(true);
		btnMesaDelete.setEnabled(false);
		
		atribuiNumeroDaMesa();
	}
	
	private void atribuiNumeroDaMesa() {
		Integer numeroProximaMesa = listMesas.getModel().getSize() + 1;
		numMesa.setText(numeroProximaMesa.toString());
	}

	private void salvaNovaMesa() throws NumberFormatException, SQLException{
		controller.criaMesa(getSelectedMpa(),
				Integer.parseInt(numMesa.getText()),
				(Objectiviano)cbDev1.getSelectedItem(),
				(Objectiviano)cbDev2.getSelectedItem());
		preencheMesasComMpaSelecionado(getSelectedMpa());
	}

	private void refreshMpas() {
		preencheMpas();
		cbMpas.setSelectedItem(controller.getMpaAtual());
		preencheMesasComMpaSelecionado(controller.getMpaAtual());
		habilitaEdicaoDeMesas(false);
	}
	
	private void excluiMesa() throws SQLException {
		Mesa mesa = (Mesa)listMesas.getSelectedValue();
		controller.excluiMesa(mesa);
		controller.atualizaNumeroDasMesas(mesa);
	}

	private MpaConfiguracao getSelectedMpa() {
		return (MpaConfiguracao) cbMpas.getSelectedItem();
	}
}
