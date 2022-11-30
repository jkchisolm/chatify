public class fileInfo {
	private String username;
	private String password;
	private String password2;
	
	public fileInfo(String user, String pass, String pass2) {
		username = user;
		password = pass;
		password2 = pass2;
	}
	public fileInfo(String user, String pass) {
		username = user;
		password = pass;
	}
}
