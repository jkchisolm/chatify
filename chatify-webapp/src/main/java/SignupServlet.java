
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SignupServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		
		int status = updateData(user,pass,pass2);
		
		System.out.println("username = " + user);
		System.out.println("pass = " + pass);
		System.out.println("pass2 = " + pass2);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"username\":" + "\"" + user + "\",");
		out.println("\"password\":" + "\"" + pass + "\",");
		out.println("\"confirmPassword\":" + "\"" + pass2 + "\",");
		out.println("\"status\":" + "\"" + status + "\"");
		out.println("}");
	}
	
    public int updateData(String user, String pass, String pass2) {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//connects java to mysql
			try {

			    Class.forName("com.mysql.cj.jdbc.Driver");

			} catch (ClassNotFoundException cnfe) {

			    System.out.println(cnfe.getMessage());

			}
			conn = DriverManager.getConnection("jdbc:mysql://35.226.126.153:3306/userBase?user=root");
			//https://www.herongyang.com/JDBC/Derby-ResultSet-insertRow.html
			st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			        ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery("Select* from users;");
			
			if(!validSignup(user, pass, pass2)) {
				System.out.println("Username is taken");
				return -1;
			}
			else if(user.length() >= 4 && pass.length() >= 8) {
				rs.moveToInsertRow();
				rs.updateString("username", user);
				rs.updateString("password", pass);
				rs.insertRow();
				rs.moveToCurrentRow();
				return 1;
			} 
			else if (pass.length() < 8){
				System.out.println("Password needs to be 8 characters");
				return -2;
			}
			else if (user.length() < 4) {
				System.out.println("Username needs to be 4 characters");
				return -3;
			}
//			while (rs.next()) {
//				System.out.println(rs.getString("username"));
//				System.out.println(rs.getString("password"));
//			}
			System.out.println();
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
				
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return 1;    	
    	
    }
   
    public boolean validSignup(String user, String pass, String pass2) {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		if(!pass.equals(pass2)) {
			return false;
		}
		try {
			try {

			    Class.forName("com.mysql.cj.jdbc.Driver");

			} catch (ClassNotFoundException cnfe) {

			    System.out.println(cnfe.getMessage());

			} 
			//connects java to mysql
			conn = DriverManager.getConnection("jdbc:mysql://35.226.126.153:3306/userBase?user=root");
			//https://www.herongyang.com/JDBC/Derby-ResultSet-insertRow.html
			st = conn.createStatement();
			rs = st.executeQuery("Select* from users;");
			
			while (rs.next()) {
				String tempUser = rs.getString("username");
				String tempPass = rs.getString("password");
				System.out.println(tempUser);
				
				if(tempUser.equals(user)) {
					return false;
				}
			}
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
				
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
    	
    	return true;
    }
}
