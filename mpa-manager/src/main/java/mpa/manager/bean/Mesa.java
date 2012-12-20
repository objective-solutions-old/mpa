package mpa.manager.bean;

public class Mesa implements Comparable<Mesa> {

	private int id;
	private MpaConfiguracao mpa;
	private int numero;
	private Objectiviano primeiroObjectiviano;
	private Objectiviano segundoObjectiviano;

	public Mesa(int numero, MpaConfiguracao mpa, Objectiviano primeiroObjectiviano, Objectiviano segundoObjectiviano) {
		setMpa(mpa);
		if (numero < 1) throw new IllegalArgumentException("Número da mesa não pode ser menor que 1.");
		this.numero = numero;
		if (primeiroObjectiviano == null)
			throw new IllegalArgumentException("Primeiro Objectiviano não pode ser nulo.");
		setPrimeiroObjectiviano(primeiroObjectiviano);
		setSegundoObjectiviano(segundoObjectiviano);
	}
	
	public Mesa(int id, int numero, MpaConfiguracao mpa, Objectiviano primeiroObjectiviano, Objectiviano segundoObjectiviano) {
		this(numero, mpa, primeiroObjectiviano, segundoObjectiviano);
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

	public void setMpa(MpaConfiguracao mpa) {
		this.mpa = mpa;
	}
	
	@Override
	public String toString() {
		return numero +	" - " + primeiroObjectiviano + (segundoObjectiviano != null ? " / " + segundoObjectiviano : "");
	}

	public int compareTo(Mesa o) {
		return getNumero() > o.getNumero() ? 1 : -1;
	}
}
