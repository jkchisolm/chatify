package matching;

import java.util.List;

public class SongList {

	private List<Song> data = null;

	public List<Song> getData() {
		return data;
	}
	
	public void setData(List<Song> data) {
		this.data = data;
	}
	
	public Song getSong(int i) {
		return data.get(i);
	}
}
