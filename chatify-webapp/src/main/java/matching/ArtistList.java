package matching;

import java.util.List;

public class ArtistList {

	private List<Artist> data = null;

	public List<Artist> getData() {
		return data;
	}
	
	public void setData(List<Artist> data) {
		this.data = data;
	}
	
	public Artist getArtist(int i) {
		return data.get(i);
	}
}
