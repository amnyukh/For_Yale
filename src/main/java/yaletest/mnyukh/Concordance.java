package yaletest.mnyukh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

/***************************************************
 * 
 * Class Tested with JUNIT in src/test/java
 * 
 * Also contains main method for running stand-alone
 * 
 * 
 * @author AMNYUKH
 *
 */
public class Concordance 
{

	//map sorts words as they are entered into it. 
	private SortedMap<String, ConcordanceData> sortedMap = new TreeMap<String, ConcordanceData> (); 
	


	/**
	 * Takes a Path to a file containing English text and generates an alphabetical sorted map of concordance data.
	 * 
	 * @param p path to file
	 * @return all the words in the file, sorted alphabetically and counted
	 */
	public SortedMap<String, ConcordanceData> generageSortedMap(Path p) {
		
		Stream<String> lineStream = null;
		String lastWord = "";
		int sentenceCount = 1; //start at sentence #1.
		try {
			lineStream = Files.lines(p);
		
		
		//for each line, split on empty space.
		//if last line ends with "." and next one is UpperCase assume it is a sentence. 
		
		//streaming turning into iterator for clarity and ease of use
		Iterator<String> it = lineStream.iterator();
		
		while (it.hasNext()) {
			String line = it.next();
			String[] words = line.split("[\\s+|\\—+|\\-+]"); //split on spaces and dashes. two kinds of dashes.
			
			//not using streams for quicker execution
			for (int i = 0; i < words.length; i++)
			{
				String currentWord = words[i];
				
				//some "" came out of shortstorytest
				if (currentWord.isEmpty()) continue;
				
				//if last word contains sentence-ending punctuation and current word starts with a capital letter
				//Unit Test found "C. Clark" was two sentences. So there needs to be more than one letter followed by punctuation.
				//this will not count "O! O! O!" as three sentences, but it works better with standard English text. 
				if (lastWord.matches(".{2,}[!|?|.].*") && currentWord.matches("^[A-Z].*")) {
					//increment sentence count
					sentenceCount++;
				}
				lastWord = currentWord; //done with the last lastWord. save current one.
				
				//if it's not a letter or an postraphe like in "don't" remove it then lower case the word
				currentWord = currentWord.replaceAll("[^a-zA-Z\\']", "").toLowerCase(); 
				
				//some words in shortstorytest are empty now.
				if (currentWord.isEmpty()) continue;
				
				ConcordanceData locationCount = sortedMap.get(currentWord);
				if (locationCount == null) {
					//new word map does not already have make a new one and add it to the list
					
					locationCount = new ConcordanceData(currentWord);
					sortedMap.put(currentWord, locationCount);
				}
				locationCount.sentences.add(sentenceCount);
					
			}
		}
		
		} catch (IOException e) {
			//here is where logging or inserting into database would occur
			e.printStackTrace();
			return null;
		} finally {
			if (lineStream != null)	lineStream.close();
		}
		
		
		return sortedMap;
	}
	
	
	/**
	 * Given a sorted map of concordance data from Concordance.generageSortedMap(), gives the ability to print 
	 * it to System.out
	 * 
	 * @param map
	 */
	public void printConcordance(SortedMap<String, ConcordanceData> map) {
		
		System.out.println("Count   Word            Locations");
		System.out.println("=================================");
		map.values().stream().forEach(x -> {
			System.out.printf("%6d %-15s  %s %n" , x.count(), x.word, x.sentences.toString() );
		});
		
	}

	
	public static void main(String[] args) {
		
		/***************************************************
		 * 
		 * Class Tested with JUNIT in src/test/java
		 * 
		 * 
		 */
		
		
		if (args.length != 1) {
			System.out.println("Usage: yaletest.mnyukh.Concordance <English text filename>");
			System.exit(0);
		}
		
		File f = new File(args[0]);
		if (!f.exists()) {
			System.out.println("File does not exist");
			System.exit(0);
		}
		
		Concordance concordance = new Concordance();
		SortedMap<String, ConcordanceData> map = concordance.generageSortedMap(f.toPath());
		concordance.printConcordance(map);
			
	}
	
	
}

