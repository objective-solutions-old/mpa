package mpa.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {

	private static Connection connection = null;

	public static Connection getConnection() throws Exception {
		if (connection != null)
			return connection;

		Properties prop = new Properties();
		try {
			prop.load(Connector.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		String driverName = prop.getProperty("drivername");
		Class.forName(driverName);

		String url = prop.getProperty("url");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
