package temp1;




import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class codescraping {
 

	public static void main(String[] args) {
		String query ="";
			
		//for uts
		query = utsCourseSearch.mainpage;
		utsCourseSearch.getUTSSearchResults(query);
		System.out.println(utsCourseSearch.programList);
		
		//for unsw
		query =	unswCourseSearch.mainpage;
		//String query = "https://sydney.edu.au/courses/subject-areas/major/banking-non-commerce.html";
		unswCourseSearch.getUNSWSearchResults(query);
		//print out all unsw courses according to its subject name in a format of a nested arraylist
		System.out.println(unswCourseSearch.programList);
		


		
		//for uni of syd
		

	}
}