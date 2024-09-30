package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;

public class SocialNetworkTest {

   
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "@bob @charlie good morning!", d1);
    private static final Tweet tweet2 = new Tweet(2, "bob", "@alyssa you're right", d2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "hello world", d1);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@ALYSSA self-mention!", d2); // Self-mention should be ignored

    // Test for enabling assertions
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Tests for guessFollowsGraph()

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

   

    @Test
    public void testGuessFollowsGraphSingleMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(List.of(tweet2));
        
        assertTrue("expected 'bob' in graph", followsGraph.containsKey("bob"));
        assertTrue("expected 'alyssa' to be mentioned", followsGraph.get("bob").contains("alyssa"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(List.of(tweet1));
        
        assertTrue("expected 'alyssa' in graph", followsGraph.containsKey("alyssa"));
        assertTrue("expected 'bob' to be mentioned", followsGraph.get("alyssa").contains("bob"));
        assertTrue("expected 'charlie' to be mentioned", followsGraph.get("alyssa").contains("charlie"));
    }

    @Test
    public void testGuessFollowsGraphSelfMentionIgnored() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(List.of(tweet4));
        
        assertTrue("expected 'alyssa' in graph", followsGraph.containsKey("alyssa"));
        assertFalse("self-mention should not be included", followsGraph.get("alyssa").contains("alyssa"));
    }

    @Test
    public void testGuessFollowsGraphCaseInsensitive() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(List.of(tweet4));

        assertTrue("expected 'alyssa' in graph", followsGraph.containsKey("alyssa"));
        assertFalse("self-mention should not be included", followsGraph.get("alyssa").contains("alyssa"));
    }

    // Tests for influencers()

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUser() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", Set.of("bob"));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected one influencer", 1, influencers.size());
        assertEquals("expected 'bob' to be the influencer", "bob", influencers.get(0));
    }

    @Test
    public void testInfluencersMultipleUsers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", Set.of("bob", "charlie"));
        followsGraph.put("bob", Set.of("charlie"));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected two influencers", 2, influencers.size());
        assertEquals("expected 'charlie' to be the most influential", "charlie", influencers.get(0));
        assertEquals("expected 'bob' to be second most influential", "bob", influencers.get(1));
    }

    @Test
    public void testInfluencersEqualFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", Set.of("bob"));
        followsGraph.put("bob", Set.of("charlie"));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected two influencers", 2, influencers.size());
        assertEquals("expected 'bob' to be the first influencer", "bob", influencers.get(0));
        assertEquals("expected 'charlie' to be the second influencer", "charlie", influencers.get(1));
    }
}
