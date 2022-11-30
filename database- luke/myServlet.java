
import java.io.FileWriter;
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
import java.io.File;

@WebServlet("/myForm")
public class myServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public myServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"Username\":" + "\"" + user + "\",");
		out.println("\"Password\":" + "\"" + pass + "\"");
		out.println("}");
	}
	  
    public boolean validLogin(String user, String pass) {
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
			
			conn = DriverManager.getConnection("jdbc:mysql://35.226.126.153:3306/userBase?user=luke?");
			//https://www.herongyang.com/JDBC/Derby-ResultSet-insertRow.html
			st = conn.createStatement();
			rs = st.executeQuery("Select* from users;");
			
			while (rs.next()) {
				String tempUser = rs.getString("username");
				String tempPass = rs.getString("password");
				
				if(tempUser.equals(user)) {
					if (tempPass.equals(pass)){
						return true;
					}
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
    	
    	return false;
    }
    
}
