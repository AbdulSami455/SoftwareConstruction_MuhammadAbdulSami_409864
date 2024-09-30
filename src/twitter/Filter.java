package twitter;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant; 

public class Filter {

  // tweets written by user 
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.getAuthor().equalsIgnoreCase(username)) {
                result.add(tweet);
            }
        }
        return result;
    }

    
     // Find tweets that were sent during a particular timespan.
     
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (!timestamp.isBefore(timespan.getStart()) && !timestamp.isAfter(timespan.getEnd())) {
                result.add(tweet);
            }
        }
        return result;
    }

    
     // Find tweets that contain certain words.
     
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            String text = tweet.getText().toLowerCase();
            for (String word : words) {
                if (text.contains(word.toLowerCase())) {
                    result.add(tweet);
                    break;  // Once a match is found, stop checking other words for this tweet.
                }
            }
        }
        return result;
    }
}
