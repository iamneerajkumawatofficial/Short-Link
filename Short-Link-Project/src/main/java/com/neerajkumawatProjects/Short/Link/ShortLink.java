package com.neerajkumawatProjects.Short.Link;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ShortLink {
	@GetMapping("/shorturl")
	public String shorturl(String longUrl, String customUrl) {
		String newUrl="";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/project","root","0230");
			if(customUrl == null || customUrl.isEmpty() && customUrl.equals("")) {
				while(true) {
				newUrl = createNewUrl(6);
				PreparedStatement stmt = con.prepareStatement("Select * from urls where shortUrl=?");
				stmt.setString(1, newUrl);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					continue;
				}else {
					PreparedStatement stmt1 = con.prepareStatement("insert into urls values(?,?)");
					stmt1.setString(1, longUrl);
					stmt1.setString(2, newUrl);
					int i = stmt1.executeUpdate();
					if(i==1) {
						return"Your new Short Url is tiny.cc/"+newUrl;
					}
				}
				}
		} else {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM urls where shortUrl= ?");
			stmt.setString(1, customUrl);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return "Already this custom url available";
			} else {
				PreparedStatement stmt1 = con.prepareStatement(
						"insert into urls values(?,?)");
				stmt1.setString(1, longUrl);
				stmt1.setString(2, customUrl);
				int i  = stmt1.executeUpdate();
				if(i==1 ) {
					return "Your new short Url is tiny.cc/"+customUrl;
				}
			}

		}
	  } catch(Exception ex) {
		  
	  }
	  return null;
	}
	public  String createNewUrl(int targetStringLength) {
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	     Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}

	@GetMapping("/{url}")
	public ModelAndView goTOMainWeb(@PathVariable("url")String url) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection
				("jdbc:mysql://localhost:3306/project","root","0230");
		PreparedStatement stmt = con.prepareStatement("Select * from urls where shortUrl=?");
		stmt.setString(1, url);
		ResultSet rs = stmt.executeQuery();
		String longUrl = "";
		if(rs.next()) {
			longUrl = rs.getString("longUrl");
		}
		return new ModelAndView("redirect:"+longUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}