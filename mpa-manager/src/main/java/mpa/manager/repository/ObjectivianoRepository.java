package mpa.manager.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mpa.manager.bean.Objectiviano;


public class ObjectivianoRepository extends Repository<Objectiviano> {

	private static ObjectivianoRepository instance;

	public ObjectivianoRepository(Connection connection) throws SQLException {
		super(connection);
	}

	@Override
	public Objectiviano materializa(ResultSet rs) throws SQLException {
		return new Objectiviano(rs.getInt("id"), rs.getString("nome"), rs.getString("login"));
	}

	@Override
	public String getSql() {
		return "select * from objectiviano";
	}

	public static void createInstance(Connection connection) throws SQLException {
		instance = new ObjectivianoRepository(connection);
	}

	public static ObjectivianoRepository getInstance() {
		if (instance == null)
			throw new IllegalStateException("ObjectivianoRepository não foi instanciado ainda. Utilize createInstance(connection)");

		return instance;
	}

	public Objectiviano getObjectiviano(String nome) throws SQLException {
		if (nome.equals("Sozinho")) return null;
		
		String select = "SELECT * FROM OBJECTIVIANO WHERE NOME = ?";
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Objectiviano objectiviano = null;
		try {
			statement = connection.prepareStatement(select);
			statement.setString(1, nome);
			rs = statement.executeQuery();
			rs.next();
			
			objectiviano = materializa(rs);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Objectiviano '" + nome + "' não encontrado");
		} finally {
			rs.close();
			statement.close();
		}
		return objectiviano;
	}
}
