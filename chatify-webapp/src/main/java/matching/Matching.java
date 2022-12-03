package matching;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Matching {
	public static ArrayList<User> allUsers;
	private static Date today = new Date();
	private static SimpleDateFormat formatter;
	private static long currTime;
	private static String output = "";
	
	private static void makeMatches() {
		//random stuff
		Random rand = new Random();
		
		int temp;
		//for odd numbered user
		if(allUsers.size() % 2 != 0) {
			temp = rand.nextInt(allUsers.size());
			allUsers.get(temp).setCurrMatch("link");
		}

		for(int i = 0; i < allUsers.size(); i++) {
			temp = rand.nextInt(allUsers.size());
			while(allUsers.get(i).getCurrMatch() == null) {
				if(allUsers.get(temp).getCurrMatch() == null) {
					if(!allUsers.get(temp).getUsername().equals(allUsers.get(i).getUsername())) {
						allUsers.get(i).setCurrMatch(allUsers.get(temp).getUsername());
						allUsers.get(temp).setCurrMatch(allUsers.get(i).getUsername());
						break;
					}
				}
				temp = rand.nextInt(allUsers.size());
			}
		}
	}
	
	private static void clearMatches() {
		for(int i = 0; i < allUsers.size(); i++) {
			allUsers.get(i).setCurrMatch(null);
		}
	}
	
	//gives the name of the match
	public static String getMatch(String username) {
		String matchname = "";
		int userIndex = 0;
		for(int i = 0; i < allUsers.size(); i++) {
			if(allUsers.get(i).getUsername().equals(username)) {
				userIndex = i;
				break;
			}
		}
		matchname = allUsers.get(userIndex).getCurrMatch();
		return matchname;
	}
	
	//gives the name of song of the match
	public static String getMatchSong(String username) {
		String matchname = "";
		int userIndex = 0;
		for(int i = 0; i < allUsers.size(); i++) {
			if(allUsers.get(i).getUsername().equals(username)) {
				userIndex = i;
				break;
			}
		}
		matchname = allUsers.get(userIndex).getSongList().getSong(0).getTrack();
		return matchname;
	}
	
	//gives the artist of song of the match
	public static String getMatchSongArtist(String username) {
		String matchname = "";
		int userIndex = 0;
		for(int i = 0; i < allUsers.size(); i++) {
			if(allUsers.get(i).getUsername().equals(username)) {
				userIndex = i;
				break;
			}
		}
		matchname = allUsers.get(userIndex).getSongList().getSong(0).getArtist();
		return matchname;
	}
		
	//gives the name of song of the match
	public static String getMatchArtist(String username) {
		String matchname = "";
		int userIndex = 0;
		for(int i = 0; i < allUsers.size(); i++) {
			if(allUsers.get(i).getUsername().equals(username)) {
				userIndex = i;
				break;
			}
		}
		matchname = allUsers.get(userIndex).getArtistList().getArtist(0).getName();
		return matchname;
	}

	public static void main (String[] args) {
		//NOTE: timing stuff is not implemented
		//remove break line at the end and add if statements to run on the while loop
		allUsers = new ArrayList<User>();
		ArrayList<User> temp = new ArrayList<User>(); //for finding out how many users there are
		
		formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("US/Pacific"));
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		while(true) {
			currTime = System.currentTimeMillis();
			output = formatter.format(new Date(currTime));
			//if(output.equals("17:45:00:000")){ //if output is the time listed
				temp.clear();
				try {
					conn = DriverManager.getConnection("jdbc:mysql://35.226.126.153:3306/userBase?user=root");
					st = conn.createStatement();
					//get user name and/or password from database
					rs = st.executeQuery("Select* from users;");
					System.out.println ("Data:");
					while (rs.next()) {
						String tempUser = rs.getString("username");
						String tempPass = rs.getString("password"); //not needed
						String tempArtists = rs.getString("Artists");
						String tempSongs = rs.getString("Tracks");
						String tempGenre = rs.getString("Genres");
						//create new user and add to array list of temp
						temp.add(new User(tempUser, tempArtists, tempSongs, tempGenre));
					}
					rs.close();
				}
				catch (SQLException sqle) {
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
				//check to see if new users, and add them when necessary
				if(temp.size() != allUsers.size()) {
					for(int i = allUsers.size(); i < temp.size(); i++) {
						allUsers.add(temp.get(i));
					}
				}
				//now we must clear all the matches made
				clearMatches();
				//now we can create the matches
				makeMatches();
				break; //this makes it currently only run once
			}
		//}
	}
}
