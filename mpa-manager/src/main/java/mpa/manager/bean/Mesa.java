package mpa.manager.bean;

public class Mesa implements Comparable<Mesa> {

	private int id;
	private MpaConfiguracao mpa;
	private int numero;
	private Objectiviano primeiroObjectiviano;
	private Objectiviano segundoObjectiviano;
	private String time;

	public Mesa(int numero, MpaConfiguracao mpa, Objectiviano primeiroObjectiviano, Objectiviano segundoObjectiviano, String time) {
		if (numero < 1) throw new IllegalArgumentException("Número da mesa não pode ser menor que 1.");
		if (primeiroObjectiviano == null)
			throw new IllegalArgumentException("Primeiro Objectiviano não pode ser nulo.");

		this.mpa = mpa;
		this.numero = numero;
		setPrimeiroObjectiviano(primeiroObjectiviano);
		setSegundoObjectiviano(segundoObjectiviano);
		this.time = time;
	}
	
	public Mesa(int id, int numero, MpaConfiguracao mpa, Objectiviano primeiroObjectiviano, Objectiviano segundoObjectiviano, String time) {
		this(numero, mpa, primeiroObjectiviano, segundoObjectiviano, time);
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public Objectiviano getPrimeiroObjectiviano() {
		return primeiroObjectiviano;
	}

	public void setPrimeiroObjectiviano(Objectiviano primeiroObjectiviano) {
		this.primeiroObjectiviano = primeiroObjectiviano;
	}

	public Objectiviano getSegundoObjectiviano() {
		return segundoObjectiviano;
	}

	public void setSegundoObjectiviano(Objectiviano segundoObjectiviano) {
		this.segundoObjectiviano = segundoObjectiviano;
	}

	public int getId() {
		return id;
	}

	public MpaConfiguracao getMpa() {
		return mpa;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;	
	}

	@Override
	public String toString() {
		return numero +	" - " + getDevsString();
	}

	public String getDevsString() {
		return primeiroObjectiviano + (segundoObjectiviano != null ? " / " + segundoObjectiviano : "");
	}

	public int compareTo(Mesa o) {
		return getNumero() > o.getNumero() ? 1 : -1;
	}


}
