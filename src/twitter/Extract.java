/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;
import java.time.Instant; 
import java.util.HashSet;   
import java.util.List;     
import java.util.Set;      


public class Extract {

   
	public static Timespan getTimespan(List<Tweet> tweets) {
	    Instant start = tweets.get(0).getTimestamp();
	    Instant end = tweets.get(0).getTimestamp();

	    for (Tweet tweet : tweets) {
	        if (tweet.getTimestamp().isBefore(start)) {
	            start = tweet.getTimestamp();
	        }
	        if (tweet.getTimestamp().isAfter(end)) {
	            end = tweet.getTimestamp();
	        }
	    }

	    return new Timespan(start, end);
	}

   
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	 Set<String> mentionedUsers = new HashSet<>();
    	    
    	    for (Tweet tweet : tweets) {
    	        String[] words = tweet.getText().split("\\s+");
    	        for (String word : words) {
    	            if (word.startsWith("@")) {
    	                String username = word.substring(1).toLowerCase();
    	                if (username.matches("^[a-zA-Z0-9_]+$")) {
    	                    mentionedUsers.add(username);
    	                }
    	            }
    	        }
    	    }
    	    
    	    return mentionedUsers;
    }

}
