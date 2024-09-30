package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.junit.Test;

public class ExtractTest {

   
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@bob hey there!", d1);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "email me at alice@example.com", d3);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "@ALICE @Bob", d1);

    // Test for enabling assertions
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Tests for getTimespan()

    @Test
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanThreeTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet4));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    @Test
    public void testGetTimespanSameTimeTweets() {
        Tweet tweet6 = new Tweet(6, "alyssa", "same time tweet", d1);
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet6));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // Tests for getMentionedUsers()

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        
        assertTrue("expected one user", mentionedUsers.contains("bob"));
        assertEquals("expected set size of 1", 1, mentionedUsers.size());
    }
    
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        
        assertTrue(mentionedUsers.contains("alice"));
        assertTrue(mentionedUsers.contains("bob"));
        assertEquals("expected set size of 2", 2, mentionedUsers.size());
    }
    
    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        
        assertTrue("expected case-insensitive 'alice'", mentionedUsers.contains("alice"));
        assertTrue("expected case-insensitive 'bob'", mentionedUsers.contains("bob"));
    }

    @Test
    public void testGetMentionedUsersInvalidMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertTrue("expected empty set due to no valid mentions", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersMixedValidInvalid() {
        Tweet tweetWithMixed = new Tweet(6, "alyssa", "@bob contact me at alice@example.com @charlie", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMixed));
        
        assertTrue(mentionedUsers.contains("bob"));
        assertTrue(mentionedUsers.contains("charlie"));
        assertFalse("invalid mention 'alice' should not be included", mentionedUsers.contains("alice"));
        assertEquals("expected set size of 2", 2, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersMultipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet5));
        
        assertTrue(mentionedUsers.contains("bob"));
        assertTrue(mentionedUsers.contains("alice"));
        assertEquals("expected set size of 2", 2, mentionedUsers.size());
    }
}
