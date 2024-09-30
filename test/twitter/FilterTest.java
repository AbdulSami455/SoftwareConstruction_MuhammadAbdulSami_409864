package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

  
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@bob hey there!", d3);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "email me at bob@example.com #contact", d1);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; 
    }

    

    @Test
    public void testWrittenBySingleTweet() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "alyssa");
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected two tweets", 2, writtenBy.size());
        assertTrue("expected list to contain tweet1", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet3", writtenBy.contains(tweet3));
    }

    @Test
    public void testWrittenByNoTweetsFromUser() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2), "alyssa");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "ALYSSA");
        assertEquals("expected one tweet", 1, writtenBy.size());
        assertTrue("expected list to contain tweet1", writtenBy.contains(tweet1));
    }

    // Tests for inTimespan()

    @Test
    public void testInTimespanSingleTweet() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T10:30:00Z");
        Timespan timespan = new Timespan(start, end);
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), timespan);
        
        assertEquals("expected one tweet", 1, inTimespan.size());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
    }

    @Test
    public void testInTimespanMultipleTweets() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T12:30:00Z");
        Timespan timespan = new Timespan(start, end);
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), timespan);
        
        assertEquals("expected three tweets", 3, inTimespan.size());
    }

    @Test
    public void testInTimespanNoTweetsInTimespan() {
        Instant start = Instant.parse("2016-02-17T12:30:00Z");
        Instant end = Instant.parse("2016-02-17T13:00:00Z");
        Timespan timespan = new Timespan(start, end);
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), timespan);
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanBoundaryTweets() {
        Instant start = Instant.parse("2016-02-17T10:00:00Z");
        Instant end = Instant.parse("2016-02-17T11:00:00Z");
        Timespan timespan = new Timespan(start, end);
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), timespan);
        
        assertTrue("expected to contain tweet1", inTimespan.contains(tweet1));
        assertTrue("expected to contain tweet2", inTimespan.contains(tweet2));
    }

    // Tests for containing()

    @Test
    public void testContainingSingleWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("rivest"));
        assertEquals("expected one tweet", 1, containing.size());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
    }

    @Test
    public void testContainingMultipleTweets() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertEquals("expected two tweets", 2, containing.size());
    }

    @Test
    public void testContainingNoMatchingWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("bob"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("TALK"));
        assertEquals("expected one tweet", 1, containing.size());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
    }

    @Test
    public void testContainingMultipleWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet4), Arrays.asList("talk", "email"));
        assertEquals("expected two tweets", 2, containing.size());
    }
}
