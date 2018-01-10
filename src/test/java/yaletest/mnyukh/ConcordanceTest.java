package yaletest.mnyukh;

import static org.junit.Assert.assertTrue;


import java.io.File;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;
/**
 * Unit test for Concordance class
 */
public class ConcordanceTest
{
 
	Concordance c;
	
	@Before
	public void setup() {
		c = new Concordance();
	}
	
	@Test
    public void simpleTest()
    {
    	String path = "src/test/resources/simpletest/input.txt";
    	File f = new File (path);
    	SortedMap<String, ConcordanceData> map = c.generageSortedMap(f.toPath());
    	assertTrue(map != null);
    	
    	//map was longer than the alphabet. verify it is still the case
    	assertTrue(map.size() > 26);
    	
    	//visually verified that it matched the one in assignment.  
    	c.printConcordance(map);
    }
	
	@Test
    public void shortStoryTest()
    {
    	String path = "src\\test\\resources\\shortstorytest\\input.txt";
    	File f = new File (path);
    	SortedMap<String, ConcordanceData> map = c.generageSortedMap(f.toPath());
    	assertTrue(map != null);
    	
    	//pick a word and make sure it's there.
    	assertTrue(map.containsKey("illustrations"));
    	
    	//this should be in the first sentence. But testing revealed it was not because of "C. Clark"
    	//sentence-matching regular expression was adjusted to account for this test
    	assertTrue(map.get("illustrations").sentences.contains(1));
    	
    	c.printConcordance(map);
    }
}
