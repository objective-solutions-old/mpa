package mpa.manager.view;

import java.awt.Dimension;
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
    private JButton btnGerar;
    private JButton btnOk;

    public MpaNovo(Date data) throws ParseException {
        super();
        controller = new MpaControl();

        setTitle("Novo Mpa");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new JPanel(new MigLayout("", "[][][][grow][][]", "[][grow]"));
        setContentPane(contentPane);

        initializeDateFields();
        initializeButtons();

        taDuplas = new JTextArea();
        taDuplas.setPreferredSize(new Dimension(450, 600));
        taDuplas.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(taDuplas, "span, grow");

        pack();

        inicializaData(data);
    }

    private void initializeButtons() {
        btnOk = new JButton("Criar");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() { criaNovoMpa(); };
                }.start();
            }
        });
        contentPane.add(btnOk, "gapleft push, align right");

        btnGerar = new JButton("Gerar MPA");
        btnGerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() { gerarNovoMpa(); };
                }.start();
            }
        });
        contentPane.add(btnGerar, "align right, wrap");
    }

    private void initializeDateFields() throws ParseException {
        contentPane.add(new JLabel("Data Início"));
        tfDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataInicio, "shrink 0");

        contentPane.add(new JLabel("Data Fim"), "gapleft 10");
        tfDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
        contentPane.add(tfDataFim, "shrink 0");
    }

    private void inicializaData(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, 1);
        tfDataInicio.setText(format.format(c.getTime()));
        c.add(Calendar.DATE, 6);
        tfDataFim.setText(format.format(c.getTime()));
    }

    private void criaNovoMpa() {
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

    private void gerarNovoMpa() {
        try {
            setComponentsEnabled(false);
            taDuplas.setText(controller.gerarNovoMpa());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        } finally {
            setComponentsEnabled(true);
        }
    }

    private void setComponentsEnabled(boolean enabled) {
        taDuplas.setEnabled(enabled);
        btnGerar.setEnabled(enabled);
        btnOk.setEnabled(enabled);
        tfDataFim.setEnabled(enabled);
        tfDataInicio.setEnabled(enabled);
    }
}
