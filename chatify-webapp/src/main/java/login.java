
import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
import java.lang.Exception;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  


//https://www.javatpoint.com/login-form-java
public class login extends JFrame implements ActionListener {
 
	    //initialize button, panel, label, and text field  
	    JButton b1, b2;  
	    JPanel newPanel;  
	    JLabel userLabel, passLabel;  
	    final JTextField  textField1, textField2;
	    
	    //calling constructor  
	    login()  
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
	        b1 = new JButton("SUBMIT"); //set label to button  
	        b2 = new JButton("Sign Up");
	        //create panel to put form elements  
	        newPanel = new JPanel(new GridLayout(3, 1));  
	        newPanel.add(userLabel);    //set username label to panel  
	        newPanel.add(textField1);   //set text field to panel  
	        newPanel.add(passLabel);    //set password label to panel  
	        newPanel.add(textField2);   //set text field to panel  
	        newPanel.add(b1);           //set button to panel  
	        newPanel.add(b2);
	          
	        //set border to panel   
	        add(newPanel, BorderLayout.CENTER);  
	          
	        //perform action on button click   
	        b1.addActionListener(this);     //add action listener to button  
	        b2.addActionListener(this); 
	        setTitle("LOGIN FORM");         //set title to the login form  
	    }  
	      
	    public boolean validLogin(String user, String pass) {
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
	    
	    //define abstract method actionPerformed() which will be called on button click   
	    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter  
	    {  
	        String userValue = textField1.getText();        //get user entered username from the textField1  
	        String passValue = textField2.getText();        //get user entered pasword from the textField2  
	        
	        
	        Object obj = ae.getSource();
	        if (obj == b2) {
		        signup sign = new signup();
		        sign.setSize(300,100);
		        sign.setVisible(true);
		        setVisible(false);
	        }
	        else if(obj == b1) {
		        if (validLogin(userValue, passValue)) {  //if authentic, navigate user to a new page  
		            
		            //create instance of the NewPage  
		            NewPage page = new NewPage();  
		              
		            //make page visible to the user  
		            page.setVisible(true);  
		              
		            //create a welcome label and set it to the new page  
		            JLabel wel_label = new JLabel("Welcome: "+userValue);  
		            page.getContentPane().add(wel_label);  
		            setVisible(false);
		        }  
		        else{  
		            //show error message  
		            System.out.println("Please enter valid username and password");  
		        }  	        	
	        //check whether the credentials are authentic or not  
	        }
	    }    
	    //main() method start  
	    public static void main(String arg[])  
	    {  
	        try  
	        {  
	            //create instance of the CreateLoginForm  
	        	login form = new login();
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
