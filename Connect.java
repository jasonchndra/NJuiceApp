package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Connect {

	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String HOST = "localhost:3306";
	private final String DATABASE = "njuice";
	private final String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	private static Connect instance = new Connect();

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private Connect() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			statement = connection.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connect getInstance() {
		return instance;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	// Metode untuk menjalankan query SELECT
	public void runQuery(String query) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String runUpdate(String query) {
		try {
			statement.executeUpdate(query);
			return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	// Metode untuk mendapatkan daftar pengguna dari tabel msuser
	public List<User> getUsers() {
		String sql = "SELECT * FROM msuser";
		runQuery(sql);
		List<User> users = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String username = resultSet.getString("Username");

				String password = resultSet.getString("Password");

				String role = resultSet.getString("Role");
				users.add(new User(username, password, role));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	// Metode untuk mendapatkan daftar item keranjang pengguna dari tabel cartdetail
	public List<CartItem> getUserCartItems(String username) {
		String sql = "SELECT * FROM cartdetail cd JOIN msjuice mj " + "ON cd.JuiceId = mj.JuiceId "
				+ "WHERE cd.Username = '" + username + "'";
		runQuery(sql);
		List<CartItem> cartItems = new ArrayList<>();

		try {
			while (resultSet.next()) {
				String juiceId = resultSet.getString("juiceId");
				String cartUsername = resultSet.getString("Username");
				String juiceName = resultSet.getString("JuiceName");
				int juicePrice = resultSet.getInt("Price");
				int quantity = resultSet.getInt("Quantity");
				cartItems.add(new CartItem(juiceId, cartUsername, juiceName, juicePrice, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartItems;
	}

	public List<String> getJuiceNames() {
		List<String> juiceNames = new ArrayList<>();
		String query = "SELECT JuiceName FROM msjuice";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String juiceName = resultSet.getString("JuiceName");
				juiceNames.add(juiceName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return juiceNames;
	}

	// Metode untuk membuat prepared statement
	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				statement = connection.createStatement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

}
