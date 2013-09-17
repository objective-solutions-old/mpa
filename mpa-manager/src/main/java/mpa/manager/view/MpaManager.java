package mpa.manager.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JButton btnMpaClone;
	private JButton btnMesaDelete;
	private JScrollPane scrollMesas;
	private JButton btnMesaNew;
	private JLabel lblNmero;
	private JLabel lblTime;
	private JButton btnMpaGenerate;
	private JComboBox cbTeamActions;
	private JLabel lblEquipe;
	private JButton btnMesaSubir;
	private JButton btnMesaDescer;
	private JComboBox cbTeam;

	public MpaManager() {
		controller = new MpaControl();
		setTitle("Mpa Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 580);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				controller.fechaConexao();
			}
		});
		
		
		contentPane = new JPanel(new MigLayout("", "[grow]", "[][][grow][]"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel mpaPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		mpaPanel.setBorder(new TitledBorder(null, "Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(mpaPanel, "cell 0 0,growx");
		
		
		cbMpas = new JComboBox();
		cbMpas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					preencheMesasComMpaSelecionado();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		mpaPanel.add(cbMpas, "cell 0 0,growx,aligny center");
		
		JPanel operacoesPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		operacoesPanel.setBorder(new TitledBorder(null, "Opera\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(operacoesPanel, "cell 0 1,growx");
		
		lblEquipe = new JLabel("Equipe:");
		operacoesPanel.add(lblEquipe, "flowx,cell 0 0,aligny center");
		
		cbTeamActions = new JComboBox();
		operacoesPanel.add(cbTeamActions, "cell 0 0,growx,aligny center");
		
		btnMpaClone = new JButton("Clonar");
		operacoesPanel.add(btnMpaClone, "cell 0 0,growx,aligny center");
		btnMpaClone.setToolTipText("Cria um novo mpa com as mesas atuais.");
		
		btnMpaGenerate = new JButton("Gerar");
		operacoesPanel.add(btnMpaGenerate, "cell 0 0,growx,aligny center");
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
		btnMpaClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					abreTelaMpaNovo(controller.getDevsTeamSeparated(getSelectedMpa(), getSelectedTeam(), "\n"));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MpaManager.this, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		
		JPanel mesasPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		mesasPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesas do Mpa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(mesasPanel, "cell 0 2,grow");
		
		listMesas = new JList();
		listMesas.setBounds(5, 44, 418, 307);
		listMesas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listMesas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				populaCamposEdicao();
				
				if (e.getClickCount() == 1) {
					habilitaEdicaoDeMesas(false);
					habilitaMoverMesas(true);
					
					if (getSelectedMpa().isAtual())
						btnMesaDelete.setEnabled(true);
				}
					
				if (e.getClickCount() == 2) {
					habilitaEdicaoDeMesas(true);
					habilitaMoverMesas(false);
				}
			}
		});
		
		scrollMesas = new JScrollPane(listMesas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mesasPanel.add(scrollMesas, "cell 0 0,grow");
		
		JPanel mesaPanel = new JPanel(new MigLayout("", "[grow]", "[][][]"));
		mesaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mesa", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(mesaPanel, "cell 0 3,growx");
		
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
		
		btnMesaDelete = new JButton("Exclui Mesa(s)");
		btnMesaDelete.setToolTipText("Exclui as mesas selecionadas.");
		btnMesaDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Dejesa realmente apagar a(s) mesa(s) selecionada(s)?", "", JOptionPane.YES_NO_OPTION) == 1) return;
				
				try {
					excluiMesas();
					limpaCamposMesa();
					preencheMesasComMpaSelecionado();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mesaPanel.add(btnMesaDelete, "cell 0 2,growx,aligny top");
		
		lblTime = new JLabel("Equipe:");
		mesaPanel.add(lblTime, "cell 0 0,alignx center,aligny center");
		
		cbTeam = new JComboBox();
		cbTeam.setEditable(true);
		mesaPanel.add(cbTeam, "cell 0 0,alignx center,aligny center");
		
		btnMesaSubir = new JButton("Subir");
		btnMesaSubir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subirMesas();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		mesaPanel.add(btnMesaSubir, "cell 0 0,growx,aligny center");
		
		btnMesaDescer = new JButton("Descer");
		btnMesaDescer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					descerMesas();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		mesaPanel.add(btnMesaDescer, "cell 0 0,growx,aligny center");
		
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
		if (listMesas.getSelectedValues().length != 1) {			
			limpaCamposMesa();
			return;
		}
		
		mesaSelecionada = (Mesa) listMesas.getSelectedValue();

		numMesa.setText(String.valueOf(mesaSelecionada.getNumero()));
		cbDev1.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getPrimeiroObjectiviano()));
		cbDev2.setSelectedItem(controller.objectivianoSelecionado(mesaSelecionada.getSegundoObjectiviano()));
		cbTeam.setSelectedItem(mesaSelecionada.getEquipe());
	}
	
	private void limpaCamposMesa() {
		numMesa.setText("");
		cbDev1.setSelectedItem(null);
		cbDev2.setSelectedItem(null);
		cbTeam.setSelectedItem(null);
	}

	private void updateMesaSelecionada() throws SQLException{
		if (mesaSelecionada == null) return;
		mesaSelecionada.setPrimeiroObjectiviano((Objectiviano)cbDev1.getSelectedItem());
		mesaSelecionada.setSegundoObjectiviano((Objectiviano)cbDev2.getSelectedItem());
		mesaSelecionada.setEquipe((String)cbTeam.getSelectedItem());
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
		mpaNovo.setLocationRelativeTo(null);
		mpaNovo.setVisible(true);	
	}
	
	private void preencheMesasComMpaSelecionado() throws SQLException {
		if (getSelectedMpa() == null) {
			listMesas.setListData(new Object[] {});
			return;
		}
		
		boolean isPassado = getSelectedMpa().getDataFim().before(new Date());
		listMesas.setEnabled(!isPassado);
		btnMesaNew.setEnabled(!isPassado);
		
		habilitaEdicaoDeMesas(false);
		habilitaMoverMesas(false);
		
		listMesas.setListData(controller.getMesas(getSelectedMpa()).toArray());
		
		refreshTeams();
	}

	private void habilitaEdicaoDeMesas(boolean b) {
		cbDev1.setEnabled(b);
		cbDev2.setEnabled(b);
		btnMesaSave.setEnabled(b);
		btnMesaDelete.setEnabled(b);
		cbTeam.setEnabled(b);
	}
	
	private void habilitaMoverMesas(boolean b) {
		btnMesaSubir.setEnabled(b);
		btnMesaDescer.setEnabled(b);
	}
	
	private void novaMesa() {
		listMesas.clearSelection();
		mesaSelecionada = null;
		limpaCamposMesa();
		habilitaEdicaoDeMesas(true);
		habilitaMoverMesas(false);
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
				(Objectiviano)cbDev2.getSelectedItem(),
				(String)cbTeam.getSelectedItem());
		preencheMesasComMpaSelecionado();
	}

	private void refreshMpas() throws SQLException {
		preencheMpas();
		cbMpas.setSelectedItem(controller.getMpaAtual());
		preencheMesasComMpaSelecionado();
	}
	
	private void refreshTeams() throws SQLException {
		cbTeamActions.removeAllItems();
		cbTeam.removeAllItems();
		
		cbTeamActions.addItem("Todas");
		cbTeam.addItem(null);
		
		for (String team : controller.getTeams(getSelectedMpa()))
			if (team != null) {
				cbTeamActions.addItem(team);
				cbTeam.addItem(team);
			}
	}

	private void excluiMesas() throws SQLException {
		for (Object mesaSelecionada : listMesas.getSelectedValues())
			controller.excluiMesa((Mesa) mesaSelecionada);
	}

	private MpaConfiguracao getSelectedMpa() {
		return (MpaConfiguracao) cbMpas.getSelectedItem();
	}

	private String getSelectedTeam() {
		return "Todas".equals((String) cbTeamActions.getSelectedItem()) ? null : (String) cbTeamActions.getSelectedItem();
	}
	
    private void gerarNovoMpa() throws SQLException {
        final MpaGerar mpaGerar = new MpaGerar(controller.getDevsTeamSeparated(getSelectedMpa(), getSelectedTeam(), " / "));
        
        mpaGerar.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (mpaGerar.getParams() != null)
					new Thread() {
						public void run() {
							LoadingBar loadingBar = new LoadingBar("GERANDO");
							
							try {
								loadingBar.setVisible(true);
								String mesasGeradas = controller.gerarNovoMpa(mpaGerar.getParams());
								abreTelaMpaNovo(mesasGeradas);
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(MpaManager.this, ex.toString());
								ex.printStackTrace();
							} finally {
								loadingBar.dispose();	
							}							
						};
					}.start();
			}
		});
        
        mpaGerar.setVisible(true);
    }
    
    private void subirMesas() throws SQLException {
		for (Object mesa : listMesas.getSelectedValues())
			controller.sobeMesa((Mesa) mesa);
		
		reordenaMesas(-1);
	}
	
	private void descerMesas() throws SQLException {
		List<Object> mesas = Arrays.asList(listMesas.getSelectedValues());
		Collections.reverse(mesas);
		
		for (Object mesa : mesas)
			controller.desceMesa((Mesa) mesa);
		
		reordenaMesas(1);
	}
	
	private void reordenaMesas(int direcao) throws SQLException {
		int[] indices = listMesas.getSelectedIndices();
		for (int i = 0; i < indices.length; i++)
			indices[i] = indices[i] + direcao;
		
		preencheMesasComMpaSelecionado();
		habilitaMoverMesas(true);
		
		listMesas.setSelectedIndices(indices);
	}
}
