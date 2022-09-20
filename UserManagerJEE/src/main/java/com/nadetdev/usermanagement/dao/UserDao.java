package com.nadetdev.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nadetdev.usermanagement.bean.User;

public class UserDao {
	// DAO Connection strings
	private String jdbcUrl = "jdbc:mysql://localhost:3306/usermanagement?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "P@ssword01";
	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

	// DAO queries
	private static final String INSERT_USER_SQL = "insert into users(name, email, country)" + " values (?, ?, ?)";
	private static final String SELECT_USER_BY_ID = "select id, name, email, country from users where id = ?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USER_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USER_SQL = "update users set name= ?, email= ?, country= ? where id= ?;";

	// Constructor
	public UserDao() {

	}

	// Connection
	protected Connection getConnection() {

		Connection conn = null;

		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return conn;
	}

	// Insert user
	public void insertUser(User user) throws SQLException {
		
		System.out.print(INSERT_USER_SQL);
		try (
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER_SQL);
			) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());

			System.out.println(preparedStatement);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Select user by id
	public User selectUserbyId(int id) {
		
		User user = null;

		try (
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BY_ID);
			) {

			preparedStatement.setInt(1, id);

			System.out.println(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
			}

		} catch (SQLException e) {
			printSQLException(e);
		}

		return user;
	}

	// Select all users
	public List<User> selectAllUsers() {
		
		List<User> users = new ArrayList<>();

		try (
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_USERS);
			) {

			System.out.println(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}

		} catch (SQLException e) {
			printSQLException(e);
		}

		return users;
	}

	// Update user
	public Boolean updateUser(User user) throws SQLException {

		Boolean rowUpdated;

		try (
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_SQL);
			) {

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());

			System.out.println(preparedStatement);

			rowUpdated = preparedStatement.executeUpdate() > 0;

			return rowUpdated;
		}
	}

	// Delete user
	public Boolean deleteUser(int id) throws SQLException {

		Boolean rowDeleted;

		try (
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_SQL);
			) {

			preparedStatement.setInt(1, id);

			System.out.println(preparedStatement);

			rowDeleted = preparedStatement.executeUpdate() > 0;

			return rowDeleted;
		}
	}

	// Handle SQL Exception
	private void printSQLException(SQLException ex) {
		
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);

				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());

				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}
