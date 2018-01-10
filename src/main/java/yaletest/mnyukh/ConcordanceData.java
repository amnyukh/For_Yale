package yaletest.mnyukh;

import java.util.ArrayList;
import java.util.List;

public class ConcordanceData {
	
	public final String word;
	public List<Integer> sentences;

	// word included in this class for clarity and easy output.
	public ConcordanceData(String currentWord) {
		this.word = currentWord;
		sentences = new ArrayList<Integer>();
	}

	public int count() {

		return sentences.size();
	}

}