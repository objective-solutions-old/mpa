package mpa.manager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import mpa.manager.bean.MpaConfiguracao;
import mpa.manager.control.MpaControl;
import net.miginfocom.swing.MigLayout;

public class MpaNovo extends JFrame {

    private static final long serialVersionUID = 2L;
    private JPanel contentPane;
	private JScrollPane scrollDuplas;
    private JTextArea taDuplas;
    private JFormattedTextField tfDataInicio;
    private JFormattedTextField tfDataFim;
    private MpaControl controller;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private JButton btnSalvar;
    private JComboBox cbMpaOrigem;
    private JLabel lblInicio;
    private JLabel lblFim;
    private JRadioButton rdbtnNovoMpa;
    private JRadioButton rdbtnAdicionarEm;
    
    public MpaNovo(Date data, String devs) throws ParseException {
        controller = new MpaControl();

        setTitle("Novo Mpa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        contentPane = new JPanel(new MigLayout("", "[grow]", "[][][grow][]"));
        setBounds(100, 100, 420, 580);
        setContentPane(contentPane);
                
        rdbtnNovoMpa = new JRadioButton("Novo Mpa");
        rdbtnNovoMpa.addActionListener(new RefreshRadio());
        rdbtnNovoMpa.setSelected(true);
        contentPane.add(rdbtnNovoMpa, "flowx,cell 0 0,growx,aligny center");

        rdbtnAdicionarEm = new JRadioButton("Adicionar em");
        rdbtnAdicionarEm.addActionListener(new RefreshRadio());
        contentPane.add(rdbtnAdicionarEm, "flowx,cell 0 1,growx,aligny center");
        
        ButtonGroup rdbGroup = new ButtonGroup();
        rdbGroup.add(rdbtnNovoMpa);
        rdbGroup.add(rdbtnAdicionarEm);
        
        lblInicio = new JLabel("Início:");
        contentPane.add(lblInicio, "cell 0 0,alignx center,aligny center");
        
        tfDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataInicio, "cell 0 0,shrinkx 0,alignx center,aligny center");
        
        lblFim = new JLabel("Fim:");
        contentPane.add(lblFim, "cell 0 0,alignx center,gapx 10,aligny center");
        
        tfDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataFim, "cell 0 0,shrinkx 0,alignx center,aligny center");
        
        
        taDuplas = new JTextArea();
        taDuplas.setText(devs);
        taDuplas.setBorder(null);
        
        scrollDuplas = new JScrollPane(taDuplas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(scrollDuplas, "cell 0 2,grow");
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new MpaSave());
        contentPane.add(btnSalvar, "cell 0 3,alignx right,aligny center");
        
        cbMpaOrigem = new JComboBox();
        contentPane.add(cbMpaOrigem, "cell 0 1,alignx center,aligny center");

        inicializaData(data);
        preencheMpasOrigem();
        new RefreshRadio().actionPerformed(null);
    }

    private class RefreshRadio implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean novo = rdbtnNovoMpa.isSelected();
			lblInicio.setEnabled(novo);
			tfDataInicio.setEnabled(novo);
			lblFim.setEnabled(novo);
			tfDataFim.setEnabled(novo);
			cbMpaOrigem.setEnabled(!novo);
		}
    }
    
    private class MpaSave implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			enablementItens(false);
			LoadingBar loadingBar = new LoadingBar("SALVANDO");
	    	try {
	    		loadingBar.setVisible(true);
	        	if (rdbtnNovoMpa.isSelected())
	        		controller.criaMpaComMesas(format.parse(tfDataInicio.getText()), format.parse(tfDataFim.getText()), taDuplas.getText());
	        	else
	        		controller.atualizaMpaComMesas((MpaConfiguracao) cbMpaOrigem.getSelectedItem(), taDuplas.getText());
		            
	            dispose();
	        } catch (ParseException e1) {
	            JOptionPane.showMessageDialog(MpaNovo.this, "Formato de data inválido (Utilize: dd/mm/aaaa)");
	        } catch (Exception e1) {
	            e1.printStackTrace();
	            JOptionPane.showMessageDialog(MpaNovo.this, e1.toString());
	        } finally {
	        	loadingBar.dispose();
	        	enablementItens(true);
	        	new RefreshRadio().actionPerformed(null);
	        }
		}
		
		private void enablementItens(boolean enable) {
			rdbtnNovoMpa.setEnabled(enable);
			rdbtnAdicionarEm.setEnabled(enable);
			lblInicio.setEnabled(enable);
			tfDataInicio.setEnabled(enable);
			lblFim.setEnabled(enable);
			tfDataFim.setEnabled(enable);
			cbMpaOrigem.setEnabled(enable);
		    taDuplas.setEnabled(enable);
		    btnSalvar.setEnabled(enable);
		}
    }

	private void inicializaData(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, 1);
        tfDataInicio.setText(format.format(c.getTime()));
        c.add(Calendar.DATE, 6);
        tfDataFim.setText(format.format(c.getTime()));
    }
	
	private void preencheMpasOrigem() {
		cbMpaOrigem.removeAllItems();
		List<MpaConfiguracao> mpas = controller.getMpasEditaveis();
		
		for (MpaConfiguracao mpaConf : mpas)
			cbMpaOrigem.addItem(mpaConf);
		
		if (mpas.isEmpty())
			rdbtnAdicionarEm.setEnabled(false);
	}	
}
