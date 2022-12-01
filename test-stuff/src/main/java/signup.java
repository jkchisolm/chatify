

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class signup extends JFrame implements ActionListener {
	 
    //initialize button, panel, label, and text field  
    JButton b1;  
    JPanel newPanel;  
    JLabel userLabel, passLabel;  
    final JTextField  textField1, textField2;  
      
    //calling constructor  
    signup()  
    {       
        //create label for username   
        userLabel = new JLabel();  
        userLabel.setText("Username");      //set label value for textField1  
          
        //create text field to get username from the user  
        textField1 = new JTextField(15);    //set length of the text  
  
        //create label for password  
        passLabel = new JLabel();  
        passLabel.setText("Password");      //set label value for textField2  
          
        //create text field to get password from the user  
        textField2 = new JPasswordField(15);    //set length for the password  
          
        //create submit button  
        b1 = new JButton("SIGN UP"); //set label to button  
          
        //create panel to put form elements  
        newPanel = new JPanel(new GridLayout(3, 1));  
        newPanel.add(userLabel);    //set username label to panel  
        newPanel.add(textField1);   //set text field to panel  
        newPanel.add(passLabel);    //set password label to panel  
        newPanel.add(textField2);   //set text field to panel  
        newPanel.add(b1);           //set button to panel  
          
        //set border to panel   
        add(newPanel, BorderLayout.CENTER);  
          
        //perform action on button click   
        b1.addActionListener(this);     //add action listener to button  
        setTitle("SIGNUP FORM");         //set title to the login form  
    }  
      
    public void updateData(String user, String pass) {	
//    	
//    	DROP DATABASE if exists Chatify_Users;
//    	CREATE DATABASE Chatify_Users;
//    	USE Chatify_Users;
//    	CREATE TABLE users (
//    		`id` int(11) NOT NULL AUTO_INCREMENT,
//    		`username` varchar(45) NOT NULL,
//    		`password` varchar(45) NOT NULL,
//    		PRIMARY KEY (`id`)
//    	    );
//    	Select* from users;
//    	
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//connects java to mysql
			conn = DriverManager.getConnection("jdbc:mysql://35.236.110.96:3306/Chatify_Users?user=root&password=pass");
			//https://www.herongyang.com/JDBC/Derby-ResultSet-insertRow.html
			st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			        ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery("Select* from users;");
			
			if(!validSignup(user, pass)) {
				System.out.println("Username is taken");
			}
			else if(user.length() >= 4 && pass.length() >= 8) {
				rs.moveToInsertRow();
				rs.updateString("username", user);
				rs.updateString("password", pass);
				rs.insertRow();
				rs.moveToCurrentRow();				
			} 
			else if (pass.length() < 8){
				System.out.println("Password needs to be 8 characters");
			}
			else if (user.length() < 4) {
				System.out.println("Username needs to be 4 characters");
			}
			while (rs.next()) {
				System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
			}
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
    	
    }
    //define abstract method actionPerformed() which will be called on button click   
   
    public boolean validSignup(String user, String pass) {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//connects java to mysql
			conn = DriverManager.getConnection("jdbc:mysql://35.236.110.96:3306/Chatify_Users?user=root&password=pass");
			//https://www.herongyang.com/JDBC/Derby-ResultSet-insertRow.html
			st = conn.createStatement();
			rs = st.executeQuery("Select* from users;");
			
			while (rs.next()) {
				String tempUser = rs.getString("username");
				String tempPass = rs.getString("password");
				
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
    
    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter  
    {  
        String userValue = textField1.getText();        //get user entered username from the textField1  
        String passValue = textField2.getText();        //get user entered pasword from the textField2  
         
        updateData(userValue, passValue);
        
        login login1 = new login();
        login1.setSize(300,100);
        login1.setVisible(true);
        setVisible(false);
    }    
    //main() method start  
    public static void main(String arg[])  
    {  
        try  
        {  
            //create instance of the CreateLoginForm  
            signup form = new signup();  
            form.setSize(300,100);  //set size of the frame  
            form.setVisible(true);  //make form visible to the user  
        }  
        catch(Exception e)  
        {     
            //handle exception   
            JOptionPane.showMessageDialog(null, e.getMessage());  
        }  
    }   
}