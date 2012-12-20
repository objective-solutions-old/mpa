package mpa.manager.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MpaConfiguracao implements Comparable<MpaConfiguracao>{

	private int id;
	private Date dataInicio;
	private Date dataFim;

	public MpaConfiguracao(Date dataInicio, Date dataFim) {
		if (dataInicio.after(dataFim)) throw new IllegalStateException("Data de início maior que data fim.");
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}

	public MpaConfiguracao(int id, Date dataInicio, Date dataFim) {
		this(dataInicio, dataFim);
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return "" + formatter.format(dataInicio) + " -> " + formatter.format(dataFim);
	}

	public int compareTo(MpaConfiguracao o) {
			if (getDataInicio().before(o.getDataInicio()))
				return -1;
			return 1;
	}
	
	public boolean isAtual(){
		Date dataAtual = new Date();
		return dataInicio.before(dataAtual) && dataFim.after(dataAtual);
	}
	
	public boolean isPassado(){
		return dataFim.before(new Date());
	}
	
	
}
