package twitter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SocialNetwork {

   
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            Set<String> mentions = Extract.getMentionedUsers(List.of(tweet));

            if (!followsGraph.containsKey(author)) {
                followsGraph.put(author, new HashSet<>());
            }

            for (String mention : mentions) {
                if (!mention.equals(author)) {
                    followsGraph.get(author).add(mention);
                }
            }
        }

        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();

        for (Set<String> followees : followsGraph.values()) {
            for (String followee : followees) {
                followerCount.put(followee, followerCount.getOrDefault(followee, 0) + 1);
            }
        }

        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((a, b) -> followerCount.get(b) - followerCount.get(a));

        return influencers;
    }
}
