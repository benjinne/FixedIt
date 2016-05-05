//jsoup License
//The jsoup code-base (include source and compiled packages) are 
//distributed under the open source MIT license as described below.
//
//The MIT License
//Copyright � 2009 - 2013 Jonathan Hedley (jonathan@hedley.net)
//
//Permission is hereby granted, free of charge, to any person obtaining
//a copy of this software and associated documentation files (the "Software"),
//to deal in the Software without restriction, including without limitation the
//rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is furnished
//to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
//CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
//OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package fixedIt.modelComponents; 

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Registrar {
	String URL;
	/**
	 * Constructor. SHOULD NEVER BE CALLED DIRECTLY. Registrar object should be
	 * created via a parent Query object.
	 * @param URL the URL generated by the parent Query object
	 */
	public Registrar(String URL){
		this.URL=URL;
	}
	
	/**
	 * Fetches course data based on this object's parent query and populates
	 * a List of Course objects.
	 * @return Courses an ArrayList of Course objects fetched based on Query
	 * @throws IOException if the Query is invalid/the webpage source cannot be retrieved
	 */
	public ArrayList<Course> fetch() throws IOException {
		String sourceHTML;
		String[] lines=null;
		sourceHTML = getUrlSource(URL);
		lines=getLinesFromHTML(sourceHTML);
		return parseCSVLines(lines);
	}
	
	/**
	 * Fetches the HTML source code from the webpage designated by the URL.
	 * SHOULD NEVER AND CANNOT BE CALLED DIRECTLY.
	 * @param url the URL of the webpage to fetch the source for
	 * @return String the HTML source code as a String
	 * @throws IOException if the HTML source code cannot be fetched or the webpage does not exist
	 */
	private String getUrlSource(String url) throws IOException {
        Connection.Response html=Jsoup.connect(url).execute();
        return html.body();
    }
	
	/**
	 * Takes a String[] containing lines in CSV format of course data,
	 * parses each line into a Course object, adds all Course objects
	 * parsed into an ArrayList<Course>. Never needs to be and cannot be
	 * called directly. This method uses a library called JSoup,
	 * licensed under the MIT License, which can be found at the top of
	 * this file.
	 * NOTE: If the course is "TO BE ARRANGED", Course.days will be null
	 * @param lines a String array containing Strings in CSV format
	 * @return courses an ArrayList<Course> parsed from lines of CSV
	 */
	private ArrayList<Course> parseCSVLines(String[] lines){
		ArrayList<Course> courses=new ArrayList<Course>();
		for(int i=0; i<lines.length; i++){
			String[] data=lines[i].split(",");
			Course course=new Course();
			if(data[0].matches("[A-Za-z0-9]+")){
				if(data[5].toLowerCase().contains("arranged")){
					course.setCRN(Integer.parseInt(data[0]));
					course.setCourseAndSection(data[1]);
					course.setTitle(data[2]);
					course.setCredits(Float.parseFloat(data[3]));
					course.setType(data[4]);
					course.setTime("TO BE ARRANGED");
					course.addLocation(data[6]);
					course.addInstructor(data[7]);
					course.setCapacity(Integer.parseInt(data[8].substring(1)));
					course.setSeatsRemain(Integer.parseInt(data[9].substring(1)));
					course.setSeatsFilled(Integer.parseInt(data[10].substring(1)));
					course.setBeginEnd(data[11]);
				}
				else{
					course.setCRN(Integer.parseInt(data[0]));
					course.setCourseAndSection(data[1].replaceAll(" ", ""));
					course.setTitle(data[2]);
					course.setCredits(Float.parseFloat(data[3]));
					course.setType(data[4]);
					course.setDays(data[5]);
					course.setTime(data[6].replace(" ", ""));
					course.addLocation(data[7]);
					course.addInstructor(data[8]);
					course.setCapacity(Integer.parseInt(data[9].substring(1)));
					course.setSeatsRemain(Integer.parseInt(data[10].substring(1)));
					course.setSeatsFilled(Integer.parseInt(data[11].substring(1)));
					course.setBeginEnd(data[12]);
				}
				
				courses.add(course);
			}
			else {
				if(data[8].matches("[A-Za-z0-9]+")){
					courses.get(i-1).addInstructor(data[8]);	
				}
				if(data[7].matches("[A-Za-z0-9]+")){
					courses.get(i-1).addLocation(data[7]);
				}
			}
		}
		
		return courses;
	}
	
	/**
	 * Takes the HTML source code as a single string and parses into separated lines
	 * of data in CSV format. Should never and cannot be called directly.
	 * @param sourceHTML the HTML source code to be parsed.
	 * @return lines a String array containing data in CSV format
	 */
	private String[] getLinesFromHTML(String sourceHTML){
		sourceHTML=sourceHTML.substring(sourceHTML.lastIndexOf("<table"));
		sourceHTML=sourceHTML.substring(0, sourceHTML.lastIndexOf("</table>")+8);
		 Document doc = Jsoup.parseBodyFragment(sourceHTML);
		 Elements rows = doc.getElementsByTag("tr");
		 String csv="";
		 for (Element row : rows) {
			 Elements cells = row.getElementsByTag("td");
			 for (Element cell : cells) {
				 Elements viewBookInfo=cell.getElementsByTag("div");
				 if(!viewBookInfo.isEmpty()){
					for(Element d : viewBookInfo){
						d.remove();
					}
				 }
				 csv=csv+cell.text().replace(",", "").concat(", ");
			 }
			 csv=csv+"\n";
		 }
		 csv=csv.substring(csv.indexOf('\n'));
		 csv=csv.replace(" view�book�info", "");
		 csv=csv.substring(1);
		 String[] lines=csv.split("\\n");
		 return lines;
	}
}
