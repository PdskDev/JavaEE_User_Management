package com.nadetdev.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nadetdev.usermanagement.bean.User;
import com.nadetdev.usermanagement.dao.UserDao;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserDao userDao;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		userDao = new UserDao();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String action = request.getServletPath();

		switch (action) {

		case "/new":
			showNewForm(request, response);
			break;

		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			break;

		case "/delete":

			try {
				deleteUser(request, response);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;

		case "/edit":
			try {
				showEditForm(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "/update":
			try {
				updateUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			try {
				listUser(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	// Show new form
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	// Insert new user
	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		User newUser = new User(name, email, country);

		userDao.insertUser(newUser);

		response.sendRedirect("list");
	}

	// Delete user
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		try {
			userDao.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.sendRedirect("list");
	}

	// Delete user
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, SQLException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		User existingUser;

		try {
			existingUser = userDao.selectUserbyId(id);
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", existingUser);
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Update user
	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		User existingUser = new User(id, name, email, country);

		userDao.updateUser(existingUser);

		response.sendRedirect("list");
	}
	
	//Default case
	private void listUser(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, SQLException, IOException {
		
		try {
			List<User> listUsers = userDao.selectAllUsers();
			request.setAttribute("listUsers", listUsers);
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
