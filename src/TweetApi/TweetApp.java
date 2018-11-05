package TweetApi;
import twitter4j.*;

import twitter4j.conf.ConfigurationBuilder;
import java.io.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TweetApp
 * @author Hongbin Zheng
 */
@WebServlet("/TwitterApp")
public class TweetApp extends HttpServlet {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
	      // Do required initialization
		  ConfigurationBuilder cf = new ConfigurationBuilder();
		   cf.setDebugEnabled(true)
			.setOAuthConsumerKey("8PYMcpjPM4QqhHvVZqswd491y")
			.setOAuthConsumerSecret("qG1leVxCwF8jzDzyF9igM8A5d9ZlJEdHO7Pb79WVRhzYXRgxWY")
			.setOAuthAccessToken("1046949435819749377-kvtBUPPJbQEF7XXvZdgD4Iz7XoRQkG")
			.setOAuthAccessTokenSecret("YnkYdOehWh6uYJzbPNYjNb0nIev7bB4hqJvkv0cKnZ6te");
	      	   
	      // Set response content type
		   resp.setContentType("text/html");
	      // Actual logic goes here.  
	      TwitterFactory factory = new TwitterFactory(cf.build());
		  twitter4j.Twitter twitter = factory.getInstance();
		  PrintWriter out = resp.getWriter();
		  Query q = new Query(req.getParameter("search"));
		  QueryResult sc;
		  List<Status> statues;
		  DirectMessage directMessage;
		  Status tweet;
		  try {	 
			  
			  // 1.Get home timeline
			statues = twitter.getHomeTimeline();
			out.println("<html>");
			out.println("<head>");
			out.println("<title> Twitter </title>");
			out.println("<body>");
			out.println("<h2>Get home timeline</h2>");
			  for(Status status : statues ) {
				  out.println(status.getUser().getName() + " - " + status.getText());
			  }
			  
			  //2. Search method///////////
			out.println("<h2>Search Method</h2>");
			sc = twitter.search(q);
			for(Status s : sc.getTweets()) {
				out.println("@" + s.getUser() + ": " + s.getText());
			}
			
			//3.Post a tweet
			out.println("<h1>Posting a Tweet</h1>");
			tweet = twitter.updateStatus(req.getParameter("tweet"));
			out.println("Successfully tweeted ----" + tweet.getText());
			
			// 4.Send direct message
			out.println("<h1>Direct Message</h1>");
			directMessage = twitter.sendDirectMessage(twitter.getScreenName(), req.getParameter("dm"));	
			out.println("[ " +directMessage.getText() + " ] " + " was sent out");
			
			//////5.Getting followers 
			out.println("<h1>Getting followers</h1>");
			IDs id = twitter.getFollowersIDs(twitter.getScreenName(), -1);
			long[] idlists= id.getIDs();
			out.println("<h1>" + twitter.getScreenName() + " is followed by: </h1>");
			int num = 1;
			if(idlists.length == 0) {
				out.print("no onne is following");
			}else {
			for (long i : idlists) {
				twitter4j.User user = twitter.showUser(i);
				out.print(num + " " + user.getScreenName()+ " is following");
			}
			}
			
//			///6.get suggest user/////
			out.println("<h1>Get suggest categories</h1>");
			out.println("Showing suggested " + twitter.getScreenName() + " category.");
			out.println("Showing suggested user categories.");
            ResponseList<Category> categories = twitter.getSuggestedUserCategories();
            for (Category category : categories) {
                out.println(category.getName() + ":" + category.getSlug());
            }
            out.println("done.");
				
			///7.getting followings
			out.println("<h1>Getting following</h1>");
			 long cursor = -1;
	            IDs ids;
	            out.println("Listing following ids.");
	            do {
	                if (0 < twitter.getScreenName().length()) {
	                    ids = twitter.getFriendsIDs(twitter.getScreenName(), cursor);
	                } else {
	                    ids = twitter.getFriendsIDs(cursor);
	                }
	                for (long idss : ids.getIDs()) {
	                	twitter4j.User userss = twitter.showUser(idss);
	                    out.println(" You are following " + userss.getScreenName()+ "!");
	                }
	            } while ((cursor = ids.getNextCursor()) != 0);
			
			//////8.getting location for trending topic information
			out.println("<h1>Getting Top trending's location</h1>");
			ResponseList<Location> location;
			location = twitter.getAvailableTrends();
			out.println("Twitter has trending information for the following cities:");
			int count = 1;
			for(Location p : location) {
				out.println(count +". "+ p.getName() + " in "+ p.getCountryName());
	            count++; 
			}
			
			out.println("</body>");
			out.println("</html>");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            out.println("Failed to follow: " + e.getMessage());
            System.exit(-1);
		}

	   }
	
}
