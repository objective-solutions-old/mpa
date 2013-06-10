package mpa.manager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import mpa.manager.control.MpaControl;
import net.miginfocom.swing.MigLayout;

public class MpaNovo extends JFrame {

    private static final long serialVersionUID = 2L;
    private JPanel contentPane;
    private JTextArea taDuplas;
    private JFormattedTextField tfDataInicio;
    private JFormattedTextField tfDataFim;
    private MpaControl controller;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private JButton btnSalvar;
    
    public MpaNovo(Date data, String devs) throws ParseException {
        super();
        controller = new MpaControl();

        setTitle("Novo Mpa");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new JPanel(new MigLayout("", "[grow]", "[][grow]"));
        setBounds(100, 100, 400, 450);
        setContentPane(contentPane);
        
        JLabel lblInicio = new JLabel("Início:");
        contentPane.add(lblInicio, "flowx,cell 0 0,alignx center,aligny center");
        
        tfDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataInicio, "cell 0 0,shrinkx 0,alignx center,aligny center");
        
        JLabel lblFim = new JLabel("Fim:");
        contentPane.add(lblFim, "cell 0 0,alignx center,gapx 10,aligny center");
        
        tfDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataFim, "cell 0 0,shrinkx 0,alignx center,aligny center");
        
        btnSalvar = new JButton("Salvar MPA");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() { salvarMpa(); };
                }.start();
            }
        });
        contentPane.add(btnSalvar, "cell 0 0,alignx center,aligny center");

        taDuplas = new JTextArea();
        taDuplas.setText(devs);
        taDuplas.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(taDuplas, "cell 0 1 ,grow");

        inicializaData(data);
    }

    private void inicializaData(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, 1);
        tfDataInicio.setText(format.format(c.getTime()));
        c.add(Calendar.DATE, 6);
        tfDataFim.setText(format.format(c.getTime()));
    }

    private void salvarMpa() {
        try {
            Date dataInicio = format.parse(tfDataInicio.getText());
            Date dataFim = format.parse(tfDataFim.getText());
            controller.criaMpaComMesas(dataInicio, dataFim, taDuplas.getText());
            dispose();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }
}
