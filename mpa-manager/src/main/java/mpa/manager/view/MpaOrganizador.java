package mpa.manager.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class MpaOrganizador extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableMesas;
	private int rowOrigem;
	private int colOrigem;
	private JPanel buttonsPanel;
	private JButton btnReordenar;
	String mesasString;
	private JTable tableCemiterio;
	
	public MpaOrganizador(String mesasString) {
		this.mesasString = mesasString;
		
		setTitle("Reordenar mesas do Mpa");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 530);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][]", "[grow]"));

		tableMesas = new JTable();
		tableMesas.setBorder(null);
		contentPane.add(tableMesas, "cell 0 0,grow");
		tableMesas.setCellSelectionEnabled(true);
		tableMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableMesas.setModel(estruturaTabelaMesas());
		tableMesas.setDragEnabled(true);
		tableMesas.setDropMode(DropMode.USE_SELECTION);
		tableMesas.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			public int getSourceActions(JComponent c) {
				return DnDConstants.ACTION_COPY_OR_MOVE;
			}

			public Transferable createTransferable(JComponent component) {
				JTable table = (JTable) component;
				rowOrigem = table.getSelectedRow();
				colOrigem = table.getSelectedColumn();
				String dev = (String) table.getModel().getValueAt(rowOrigem,colOrigem);
				return new StringSelection(dev);
			}

			public boolean canImport(TransferHandler.TransferSupport info) {
				return info.isDataFlavorSupported(DataFlavor.stringFlavor);
			}

			public boolean importData(TransferSupport support) {
				if (!support.isDrop() || !canImport(support))
					return false;

				JTable.DropLocation dropLocal = (JTable.DropLocation) support.getDropLocation();

				int row = dropLocal.getRow();
				int col = dropLocal.getColumn();

				DefaultTableModel mesas = (DefaultTableModel) tableMesas.getModel();
				DefaultTableModel cemiterio = (DefaultTableModel) tableCemiterio.getModel();
				String devOrigem = (String) mesas.getValueAt(row, col);
				if (devOrigem != null && !devOrigem.equals(""))
					cemiterio.addRow(new String[] { devOrigem });

				String dev;
				try {
					dev = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				mesas.setValueAt(null, rowOrigem, colOrigem);
				if (dev != null && !dev.equals(""))
					mesas.setValueAt(dev, row, col);

				return true;
			}

		});
		
		buttonsPanel = new JPanel();
		contentPane.add(buttonsPanel, "cell 1 0,grow");
		buttonsPanel.setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		btnReordenar = new JButton("Reordenar");
		btnReordenar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reordenarMesas();
				dispose();
			}
		});
		
		tableCemiterio = new JTable();
		tableCemiterio.setBorder(null);
		tableCemiterio.setCellSelectionEnabled(true);
		tableCemiterio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCemiterio.setModel(estruturaCemiterio());
		tableCemiterio.setDragEnabled(true);
		tableCemiterio.setDropMode(DropMode.USE_SELECTION);
		tableCemiterio.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			public int getSourceActions(JComponent c) {
				return DnDConstants.ACTION_COPY_OR_MOVE;
			}

			public Transferable createTransferable(JComponent component) {
				JTable table = (JTable) component;
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				String dev = (String) model.getValueAt(table.getSelectedRow(),table.getSelectedColumn());
				model.removeRow(table.getSelectedRow());
				return new StringSelection(dev);
			}

			public boolean canImport(TransferHandler.TransferSupport info) {
				return info.isDataFlavorSupported(DataFlavor.stringFlavor);
			}

			public boolean importData(TransferSupport support) {
				if (!support.isDrop() || !canImport(support))
					return false;

				String dev = null;
				try {
					dev = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				DefaultTableModel cemiterio = (DefaultTableModel) tableCemiterio.getModel();				
				if (dev != null && !dev.equals(""))
					cemiterio.addRow(new String[] { dev });
				
				DefaultTableModel mesas = (DefaultTableModel) tableMesas.getModel();
				mesas.setValueAt(null, tableMesas.getSelectedRow(), tableMesas.getSelectedColumn());

				return true;
			}
		});
		
		buttonsPanel.add(tableCemiterio, "cell 0 0,grow");
		buttonsPanel.add(btnReordenar, "cell 0 15,growx,aligny bottom");
	}
	
	public String  getMesasString() {
		return mesasString;
	}

	private DefaultTableModel estruturaTabelaMesas() {
		DefaultTableModel mesasModel = new DefaultTableModel();
		mesasModel.setColumnCount(2);
		for (String mesa : mesasString.split("\n")) {
			mesasModel.addRow(mesa.split(" / "));
		}
		return mesasModel;
	}

	private DefaultTableModel estruturaCemiterio() {
		DefaultTableModel mesasModel = new DefaultTableModel();
		mesasModel.setColumnCount(1);
		return mesasModel;
	}
	
	private void reordenarMesas() {
		mesasString = "";
		int i;
		for (i = 0; i < tableMesas.getRowCount(); i++) {
			mesasString += formataLinha(i);
		}
	}
	
	private String formataLinha(int row) {
		String dev1 = (String) tableMesas.getValueAt(row, 0);
		String dev2 = (String) tableMesas.getValueAt(row, 1);
		
		if (dev1 == null && dev2 == null)
			return "";
		
		else if (dev1 != null && dev2 != null)
			return dev1 + " / " + dev2 + "\n";
		
		return dev1 != null ? dev1 + "\n" : dev2 + "\n";
	}
}
