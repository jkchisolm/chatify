import java.io.*;
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

@WebServlet("/APIServlet")
public class APIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public APIServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        PrintWriter out = response.getWriter();
        String artists = request.getParameter("artists");
        String tracks = request.getParameter("tracks");
        String genres = request.getParameter("genres");
        String user = request.getParameter("user");

        if(updateData(user,artists,tracks,genres)) {
            response.sendRedirect("countDown.html");
        }
        else {
            response.sendRedirect("api.html");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
//        doGet(request, response);
    	PrintWriter out = response.getWriter();
        String payloadRequest = getBody(request);
        String[] splitPayload = payloadRequest.split(", NEXT, ");
        String topTracks = splitPayload[0];
        String topArtists = splitPayload[1];
        String topGenres = splitPayload[2];
        String user = splitPayload[3];
        if (updateData(user, topArtists, topTracks, topGenres)) {
        	out.println("{");
            out.println("\"status\": \"success\"");
            out.println("}");
        } else {
            out.println("{");
            out.println("\"status\": \"failure\"");
            out.println("}");
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

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}