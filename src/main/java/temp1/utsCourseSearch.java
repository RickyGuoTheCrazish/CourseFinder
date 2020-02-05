package temp1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class utsCourseSearch {
	public static final String mainpage = "https://www.handbook.uts.edu.au/directory/majors.html";
	public static final String className = "ie-images";
	public static ArrayList<ArrayList<String>> programList = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> getUTSSearchResults(String query){
		Document doc = null;
	 
		try {
	   
			doc = Jsoup.connect(query).get();	
			Elements programResults = doc.getElementsByClass(utsCourseSearch.className);
			Elements newProgramResults = programResults.select("a[href]");

			for (Element programs : newProgramResults) {
				String program = programs.attr("href");
				//System.out.println(program);
				
				//String programAbb = programs.getElementsByTag("a").text();
				//System.out.println(programAbb);
				if(program.matches("https://.*")) {
					
					ArrayList<String> programName = new ArrayList<String>();
					programName.add(program);
					//programName.add(programAbb);
					String findCourseQuery = program;
					programName = getUTSCourses(findCourseQuery,programName);
					utsCourseSearch.programList.add(programName);
					
					//System.out.println(programList); 
					
				}else {
					//System.out.println("oho");
				}
				
			}
			
			
	   		
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		String path = "/Users/Richrad/Documents/workspace/CourseFinder/tmp/developer.csv";
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


			
			
		   

		

		//}

		
		return programList;
	}
	
	public static ArrayList<String> getUTSCourses(String query,ArrayList<String> temp) {
		Document doc = null;
		try {
			doc = Jsoup.connect(query).get();
			Elements courseResults = doc.getElementsByClass(utsCourseSearch.className);
			String programName = courseResults.select("h1").text();
			if(programName.matches(".*,.*")) {
				programName = programName.replaceAll(",", "\\s");
			}else {
				//System.out.println("uts program name shown error");
			}
			temp.add(programName);
			Elements courseCodes = courseResults.select("td").select("a[href]");
			
			for(Element course: courseCodes) {
				String tempCourseName = course.getElementsByTag("a").text();
				if(tempCourseName.matches(".*\\d{5}.*")) {
					temp.add(tempCourseName);
					//System.out.println(tempCourseName);
				}
			}
			//System.out.println(courseResults);
			
		}catch (IOException e) {
			e.printStackTrace(); 
		}
		return temp;
	} 
}