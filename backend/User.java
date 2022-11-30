public class User {
	private String username;
	private String currMatch;
	
	public User(String name) {
		username = name;
		currMatch = null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getCurrMatch() {
		return currMatch;
	}
	
	public void setCurrMatch(String match) {
		currMatch = match;
	}
}