package temp1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter; 

/*
 * Some USYD majors have no available courses to enroll
 * when put those data into SQL/MANGO, pay attention
 */

public class usydCourseSearch {
	public static final String mainpage = "";
	public static final String className = "b-tag-list__item col-md-2 col-xs-6";
	public static final String prefix = "https://sydney.edu.au";
	private static String programHref = "";
	public static ArrayList<ArrayList<String>> programList = new ArrayList<ArrayList<String>>();
	private static Scanner sc;

	public static ArrayList<ArrayList<String>> getUSYDSearchResults(String query){
		ArrayList<String> fileString = new ArrayList<String>();
		try {
			fileString = readFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int a=0; a<fileString.size(); a++) {
			
			programHref = fileString.get(a);
			if(!programHref.contains("https:") || programHref.contains("\"")) {
				programHref = programHref.replaceAll("\"", "");
				programHref = programHref.replace("href=", prefix);
				//System.out.println(programHref);
			}
			ArrayList<String> coursesFromPrograms = getUSYDCourses(programHref);
			programList.add(coursesFromPrograms);
		}
		String path = "/Users/Richrad/Documents/workspace/CourseFinder/tmp/usydstats.csv";
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(path));
			List<ArrayList<String>> tmplist = programList;
			
			//for(ArrayList<String> m : tmplist) {
				//System.out.println(tmplist.get(0));
				int k = 0;
				while(k<tmplist.size()) {
					ArrayList<String> tmpAS = tmplist.get(k);
					String[] stringset = new String[tmpAS.size()];

					for(int a = 0; a<tmpAS.size();a++) {
						stringset[a] = tmpAS.get(a);
					}
					String[] entries = stringset;
					//entries have to be String[]
				    writer.writeNext(entries);

					k++;
				}
			
			writer.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return programList;
	}


	public static ArrayList<String> readFile() throws Exception 
	{ 
	  // pass the path to the file as a parameter 
	  File file = 
	    new File("/Users/Richrad/Documents/workspace/CourseFinder/tmp/usydprograms"); 
	  sc = new Scanner(file); 
	  ArrayList<String> tempString = new ArrayList<String>();
	  while (sc.hasNextLine()) {
		  tempString.add(sc.nextLine());
	  }
	  
	  return tempString;
	} 
	
	public static ArrayList<String> getUSYDCourses(String query){
		ArrayList<String> courses = new ArrayList<String>();
		courses.add(query);
		Document doc = null;
		try {
			doc = Jsoup.connect(programHref).get();
			Elements title = doc.getElementsByClass("pageTitle");
			Elements coursehrefResults = doc.getElementsByClass(usydCourseSearch.className);
			String tempTitle = "";
			for (Element t : title) {
				tempTitle = t.text();
				courses.add(tempTitle);
			}
	
			for (Element link : coursehrefResults) {
				String hrefLink = link.text();
				if(hrefLink.matches(".*\\s\\w{4}\\d{4}.*")) {
					String[] onlycodes = hrefLink.split("\\s");
					for(String onlycode : onlycodes) {
						if(onlycode.matches("\\w{4}\\d{4}")) 
							courses.add(onlycode);
					}
				}
				else continue;
			}
			
		}catch (IOException e) {
			e.printStackTrace(); 
		}
		return courses;
	}

}