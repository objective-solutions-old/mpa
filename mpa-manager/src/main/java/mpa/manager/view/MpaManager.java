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
import javax.swing.JLabel;


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
	private JTextField textTeam;
	private JLabel lblNmero;
	private JLabel lblTime;
	private JButton btnMpaGenerate;

	public MpaManager() {
		controller = new MpaControl();
		setTitle("Mpa Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 475);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				controller.fechaConexao();
			}
		});
		
		
		contentPane = new JPanel(new MigLayout("", "[grow]", "[][grow][]"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel mpaPanel = new JPanel(new MigLayout("", "[grow]", "[][grow]"));
		mpaPanel.setBorder(new TitledBorder(null, "Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(mpaPanel, "cell 0 0,grow");
		
		btnMpaNew = new JButton("Novo");
		btnMpaNew.setToolTipText("Cria um novo mpa com mesas.");
		btnMpaNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					abreTelaMpaNovo(null);
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		
		btnMpaClone = new JButton("Clonar");
		btnMpaClone.setToolTipText("Cria um novo mpa com as mesas atuais.");
		btnMpaClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					abreTelaMpaNovo(controller.getDevsStringFormatted(getSelectedMpa()));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		
		
		cbMpas = new JComboBox();
		cbMpas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					preencheMesasComMpaSelecionado(getSelectedMpa());
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		mpaPanel.add(cbMpas, "cell 0 0,growx,aligny center");
		mpaPanel.add(btnMpaNew, "flowx,cell 0 1,growx,aligny top");
		mpaPanel.add(btnMpaClone, "cell 0 1,growx,aligny top");
		
		btnMpaGenerate = new JButton("Gerar");
		btnMpaGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	try {
					gerarNovoMpa();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		mpaPanel.add(btnMpaGenerate, "cell 0 1,growx,aligny top");
		
		JPanel mesasPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		mesasPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesas do Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(mesasPanel, "cell 0 1,grow");
		
		listMesas = new JList();
		listMesas.setBounds(5, 44, 418, 307);
		listMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMesas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				populaCamposEdicao();
			}
		});
		
		scrollMesas = new JScrollPane(listMesas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mesasPanel.add(scrollMesas, "cell 0 0,grow");
		
		JPanel mesaPanel = new JPanel(new MigLayout("", "[grow]", "[][][]"));
		mesaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(mesaPanel, "cell 0 2,grow");
		
		lblNmero = new JLabel("Número:");
		mesaPanel.add(lblNmero, "flowx,cell 0 0,alignx center,aligny center");
		
		numMesa = new JTextField();
		numMesa.setHorizontalAlignment(SwingConstants.CENTER);
		numMesa.setEditable(false);
		numMesa.setColumns(2);
		mesaPanel.add(numMesa, "cell 0 0,alignx center,aligny center");

		cbDev1 = new JComboBox(controller.getObjectivianos().toArray());
		cbDev1.setSelectedIndex(-1);
		mesaPanel.add(cbDev1, "cell 0 1,growx,aligny center");
		
		ArrayList<Objectiviano> obj = new ArrayList<Objectiviano>();
		obj.add(null);
		obj.addAll(controller.getObjectivianos());
		cbDev2 = new JComboBox(obj.toArray());
		cbDev2.setSelectedIndex(-1);
		mesaPanel.add(cbDev2, "cell 0 1,growx,aligny center");
		
		btnMesaNew = new JButton("Nova Mesa");
		btnMesaNew.setEnabled(false);
		btnMesaNew.setToolTipText("Cria uma nova mesa no mpa selecionado.");
		btnMesaNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novaMesa();
			}
		});
		mesaPanel.add(btnMesaNew, "cell 0 2,growx,aligny top");
		
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
		mesaPanel.add(btnMesaSave, "cell 0 2,growx,aligny top");
		
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
		mesaPanel.add(btnMesaDelete, "cell 0 2,growx,aligny top");
		
		lblTime = new JLabel("Equipe:");
		mesaPanel.add(lblTime, "cell 0 0,alignx center,aligny center");
		
		textTeam = new JTextField();
		mesaPanel.add(textTeam, "cell 0 0,alignx center,aligny center");
		textTeam.setColumns(10);
		
		try {
			refreshMpas();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
			e1.printStackTrace();
		}
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
		textTeam.setText(mesaSelecionada.getEquipe());
	}
	
	private void limpaCamposMesa() {
		numMesa.setText("");
		cbDev1.setSelectedItem(null);
		cbDev2.setSelectedItem(null);
		textTeam.setText("");
	}

	private void updateMesaSelecionada() throws SQLException{
		if (mesaSelecionada == null) return;
		mesaSelecionada.setPrimeiroObjectiviano((Objectiviano)cbDev1.getSelectedItem());
		mesaSelecionada.setSegundoObjectiviano((Objectiviano)cbDev2.getSelectedItem());
		mesaSelecionada.setEquipe(textTeam.getText());
		controller.updateMesa(mesaSelecionada);
		listMesas.setSelectedValue(mesaSelecionada, false);
	}
	
	private void abreTelaMpaNovo(String devs) throws ParseException {
		MpaNovo mpaNovo = new MpaNovo(controller.getMaiorMpa().getDataFim(), devs);
		mpaNovo.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					refreshMpas();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		mpaNovo.setVisible(true);	
	}
	
	private void preencheMesasComMpaSelecionado(MpaConfiguracao mpa) throws SQLException {
		if (mpa == null) {
			listMesas.setListData(new Object[] {});
			return;
		}
		
		boolean isPassado = mpa.getDataFim().before(new Date());
		listMesas.setEnabled(!isPassado);
		btnMesaNew.setEnabled(!isPassado);
		
		habilitaEdicaoDeMesas(false);
		
		listMesas.setListData(controller.getMesas(mpa).toArray());
	}

	private void habilitaEdicaoDeMesas(boolean b) {
		cbDev1.setEnabled(b);
		cbDev2.setEnabled(b);
		btnMesaSave.setEnabled(b);
		btnMesaDelete.setEnabled(b);
		textTeam.setEnabled(b);
	}
	
	private void novaMesa() {
		listMesas.clearSelection();
		mesaSelecionada = null;
		limpaCamposMesa();
		cbDev1.setEnabled(true);
		cbDev2.setEnabled(true);
		btnMesaSave.setEnabled(true);
		btnMesaDelete.setEnabled(false);
		textTeam.setEnabled(true);
		
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
				(Objectiviano)cbDev2.getSelectedItem(),
				textTeam.getText());
		preencheMesasComMpaSelecionado(getSelectedMpa());
	}

	private void refreshMpas() throws SQLException {
		preencheMpas();
		cbMpas.setSelectedItem(controller.getMpaAtual());
		preencheMesasComMpaSelecionado(controller.getMpaAtual());
		habilitaEdicaoDeMesas(false);
	}
	
	private void excluiMesa() throws SQLException {
		controller.excluiMesa((Mesa)listMesas.getSelectedValue());
	}

	private MpaConfiguracao getSelectedMpa() {
		return (MpaConfiguracao) cbMpas.getSelectedItem();
	}
	
    private void gerarNovoMpa() throws SQLException {
        final MpaGerar mpaGerar = new MpaGerar(controller.getDevsTeamSeparated(getSelectedMpa()));
        
        mpaGerar.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (mpaGerar.getParams() != null)
					new Thread() {
					public void run() {
						try {
							abreTelaMpaNovo(controller.gerarNovoMpa(mpaGerar.getParams()));
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(MpaManager.this, ex.toString());
							ex.printStackTrace();
						}						
					};
				}.start();
			}
		});
        
        mpaGerar.setVisible(true);
    }
}
