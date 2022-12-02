
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

@WebServlet("/myForm3")
public class myServlet3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public myServlet3() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in servlet3");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		String artists = request.getParameter("artists");
		String tracks = request.getParameter("tracks");
		String genres = request.getParameter("genres");
		String user = "joshc2";
		
		if(updateData(user,artists,tracks,genres)) {
			response.sendRedirect("login.html");	
		}
		else {
			response.sendRedirect("api.html");	
		}

	}
	
    public boolean updateData(String user, String artists, String tracks, String genres) {	
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
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			        ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery("Select* from users;");
			
			if(userExists(user)) {
				while (rs.next()) {
					String curr = rs.getString("username");
					if(curr.equals(user)) {
						rs.moveToCurrentRow();
						rs.updateString("artists", artists);
						rs.updateString("tracks", tracks);
						rs.updateString("genres", genres);
						rs.updateRow();
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
   
    public boolean userExists(String user) {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

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
				if(tempUser == null) {
					
				}
				else if(tempUser.equals(user)) {
					System.out.println("user exists");
					return true;
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
