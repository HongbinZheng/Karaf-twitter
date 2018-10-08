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
			statues = twitter.getHomeTimeline();
			out.println("<html>");
			out.println("<head>");
			out.println("<title> Twitter </title>");
			out.println("<body>");
			out.println("<h1>Get home timeline</h1>");
			  for(Status status : statues ) {
				  out.println(status.getUser().getName() + " - " + status.getText());
			  }
			out.println("<h1>Search Method</h1>");
			sc = twitter.search(q);
			for(Status s : sc.getTweets()) {
				out.println("@" + s.getUser() + ": " + s.getText());
			}
			out.println("<h1>Posting a Tweet</h1>");
			tweet = twitter.updateStatus(req.getParameter("tweet"));
			out.println("Successfully tweeted ----" + tweet.getText());
			out.println("<h1>Direct Message</h1>");
			directMessage = twitter.sendDirectMessage(twitter.getScreenName(), req.getParameter("dm"));	
			out.println("[ " +directMessage.getText() + " ] " + " was sent out");
			out.println("</body>");
			out.println("</html>");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   }
	
	
/*
	 public void doPost(HttpServletRequest req,HttpServletResponse resp) throws TwitterException, IOException {
		 ApiConf t1 = new ApiConf();
		 String testReturn = t1.getTimeline();
		 
		 req.setAttribute("URL", testReturn);
		 
		 RequestDispatcher rd = req.getRequestDispatcher("test.jsp");
		 try {
			 rd.forward(req,resp);
		 }catch (ServletException e){
			 e.printStackTrace();
		 }
	 }
*/
}
