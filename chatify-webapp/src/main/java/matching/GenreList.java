package matching;

import java.util.List;

public class GenreList {

	private List<Genre> data = null;

	public List<Genre> getData() {
		return data;
	}
	
	public void setData(List<Genre> data) {
		this.data = data;
	}
	
	public Genre getGenre(int i) {
		return data.get(i);
	}
}
