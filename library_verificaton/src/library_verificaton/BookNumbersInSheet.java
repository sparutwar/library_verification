package library_verificaton;

import java.util.HashMap;
import java.util.Map;

public class BookNumbersInSheet {
	HashMap<Integer, Integer> mapBookNumRowNum = new HashMap<>();

	public HashMap<Integer, Integer> getHashMap() {
		return mapBookNumRowNum;
	}

	public void setHashMap(HashMap<Integer, Integer> map) {
		this.mapBookNumRowNum = map;
	}
		
	
}
