package mpa.manager.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {
	
	protected final Connection connection;

	Repository(Connection connection) throws SQLException {
		this.connection = connection;
	}
	
	public abstract T materializa(ResultSet rs) throws SQLException;
	
	public abstract String getSql();

	public List<T> todos() {
		List<T> result;
		String sql = this.getSql();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();

			result = materializaEntidades(rs);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { statement.close(); } catch (Exception e) { e.printStackTrace(); }
		}

		return result;
	}
	
	protected List<T> materializaEntidades(ResultSet rs) throws SQLException {
		List<T> result = new ArrayList<T>();

		while (rs.next())
			result.add(this.materializa(rs));

		return result;
	}
}
