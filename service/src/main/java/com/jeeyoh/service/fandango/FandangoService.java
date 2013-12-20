package com.jeeyoh.service.fandango;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeeyoh.persistence.dao.fandango.IFandangoDAO;
import com.jeeyoh.persistence.domain.FandangoCommingSoon;
import com.jeeyoh.persistence.domain.FandangoNearMe;
import com.jeeyoh.persistence.domain.FandangoOpenning;
import com.jeeyoh.persistence.domain.FandangoTopTen;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component("fandangoService")
public class FandangoService implements IFandangoService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	private String topTenURL = "http://www.fandango.com/rss/top10boxoffice.rss";
	private String newMoviesURL = "http://www.fandango.com/rss/newmovies.rss";
	private String commingSoonURL = "http://www.fandango.com/rss/comingsoonmovies.rss";
	private String moviesNearMe = "http://www.fandango.com/rss/moviesnearme_10005.rss";
	
	@Autowired
	private IFandangoDAO fandangoDAO;

	@Override
	public void topTen() {
		// TODO Auto-generated method stub
		try
		{
			logger.debug("Top Ten :: == > ");
			URL url = new URL(topTenURL);
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<SyndEntry> entries = feed.getEntries();
			Iterator<SyndEntry> itEntries = entries.iterator();
			
			while (itEntries.hasNext()) {
				SyndEntry entry = itEntries.next();
				logger.debug("TITLE ===== > "+entry.getTitle());
				FandangoTopTen fandango = new FandangoTopTen();
				fandango.setTitle(entry.getTitle());
				fandango.setAuthor(entry.getAuthor());
				fandango.setLink(entry.getLink());
				fandango.setPublishDate(entry.getPublishedDate()+"");
				fandango.setDescription(entry.getDescription().getValue());
				fandangoDAO.saveTopTen(fandango);
				
			}
			
			
		}
			
		catch(Exception e)
		{
			logger.error("ERROR ::: == >"+e);
		}
		commingSoon();
	}

	@Override
	public void commingSoon() {
		// TODO Auto-generated method stub
		try
		{
			logger.debug("commingSoon :: == > ");
			URL url = new URL(commingSoonURL);
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<SyndEntry> entries = feed.getEntries();
			Iterator<SyndEntry> itEntries = entries.iterator();
			
			while (itEntries.hasNext()) {
				SyndEntry entry = itEntries.next();
				logger.debug("TITLE ===== > "+entry.getTitle());
				FandangoCommingSoon fandango = new FandangoCommingSoon();
				fandango.setTitle(entry.getTitle());
				fandango.setAuthor(entry.getAuthor());
				fandango.setLink(entry.getLink());
				fandango.setPublishDate(entry.getPublishedDate()+"");
				fandango.setDescription(entry.getDescription().getValue());
				fandangoDAO.saveCommingSoon(fandango);
				
			}
			
			
		}
			
		catch(Exception e)
		{
			logger.error("ERROR ::: == >"+e);
		}
		moviesNearMe();
	}

	@Override
	public void moviesNearMe() {
		// TODO Auto-generated method stub
		try
		{
			logger.debug("moviesNearMe :: == > ");
			URL url = new URL(moviesNearMe);
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<SyndEntry> entries = feed.getEntries();
			Iterator<SyndEntry> itEntries = entries.iterator();
			
			while (itEntries.hasNext()) {
				SyndEntry entry = itEntries.next();
				logger.debug("TITLE ===== > "+entry.getTitle());
				FandangoNearMe fandango = new FandangoNearMe();
				fandango.setTitle(entry.getTitle());
				fandango.setAuthor(entry.getAuthor());
				fandango.setLink(entry.getLink());
				fandango.setPublishDate(entry.getPublishedDate()+"");
				fandango.setDescription(entry.getDescription().getValue());
				fandangoDAO.saveMovieNearMe(fandango);
				
			}
			
			
		}
			
		catch(Exception e)
		{
			logger.error("ERROR ::: == >"+e);
		}
		newMovies();
	}

	@Override
	public void newMovies() {
		// TODO Auto-generated method stub
		try
		{
			logger.debug("newMovies :: == > ");
			URL url = new URL(newMoviesURL);
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<SyndEntry> entries = feed.getEntries();
			Iterator<SyndEntry> itEntries = entries.iterator();
			
			while (itEntries.hasNext()) {
				SyndEntry entry = itEntries.next();
				logger.debug("TITLE ===== > "+entry.getTitle());
				FandangoOpenning fandango = new FandangoOpenning();
				fandango.setTitle(entry.getTitle());
				fandango.setAuthor(entry.getAuthor());
				fandango.setLink(entry.getLink());
				fandango.setPublishDate(entry.getPublishedDate()+"");
				fandango.setDescription(entry.getDescription().getValue());
				fandangoDAO.saveNewOpning(fandango);
				
			}
			
			
		}
			
		catch(Exception e)
		{
			logger.error("ERROR ::: == >"+e);
		}
		
	}
	
	
}
