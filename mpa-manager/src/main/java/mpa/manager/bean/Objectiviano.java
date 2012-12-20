package mpa.manager.bean;

public class Objectiviano implements Comparable<Objectiviano>{
	
	private int id;
	private String nome;
	private String login;
	
	public Objectiviano(int id, String nome, String login){
		this.id = id;
		this.nome = nome;
		this.login = login;
	}
	
	public boolean equals(Objectiviano obj) {
		return this.id == obj.id;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public int compareTo(Objectiviano o) {
		return getNome().compareTo(o.getNome());
	}

}
