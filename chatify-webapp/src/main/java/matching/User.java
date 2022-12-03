package matching;

import java.util.ArrayList;

import com.google.gson.Gson;

public class User {
	private String username;
	private String currMatch;
	private ArtistList tempArtists;
	private SongList tempSongs;
	private GenreList tempGenre;
	
	
	public User(String name, String string_tempArtists, String string_tempSongs, String string_tempGenre) {
		username = name;
		currMatch = null;
		// MAKE SURE YOU PUT GSON.JAR IN WEB-INF FOR DYNAMIC WEB PROJECTS:
		Gson gson = new Gson();
		tempGenre = gson.fromJson(string_tempGenre, GenreList.class);
		tempSongs = gson.fromJson(string_tempSongs, SongList.class);
		tempArtists = gson.fromJson(string_tempArtists, ArtistList.class);
	}
	
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
	
	public ArtistList getArtistList() {
		return tempArtists;
	}
	
	public void setArtistList(ArtistList l) {
		tempArtists = l;
	}
	
	public SongList getSongList() {
		return tempSongs;
	}
	
	public void setSongList(SongList l) {
		tempSongs = l;
	}
	
	public GenreList getGenreList() {
		return tempGenre;
	}
	
	public void setGenreList(GenreList l) {
		tempGenre = l;
	}
}
