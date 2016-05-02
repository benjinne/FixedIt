package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.TreeMap;
import java.lang.Object;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.*;

import fixedIt.controllers.DisplayScheduleController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Schedule;
import fixedIt.modelComponents.SortCoursesComparator;


public class DisplayScheduleServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		if(session==null){
			resp.sendRedirect("login");
			return;
		}
		Schedule s;
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		try{
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		}
		catch(NullPointerException e){
			controller.initializeSchedule();
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
			//System.out.print(s.getName());
		}
		
		for(Course c : s.getCourses()){
			if(req.getParameter("" + c.getCRN())!=null){
				s.deleteCourse(c.getCRN());
			}
		}
		
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		if(session==null){
			resp.sendRedirect("login");
			return;
		}
		
		Schedule s;
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		try{
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		}
		catch(NullPointerException e){
			controller.initializeSchedule();
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		}
		
		if(req.getParameter("dlAsCSV")!=null){
			resp.setContentType("text/csv");
			resp.setHeader("Content-Disposition", "attachment; filename=\"schedule.csv\"");
			try{
				ServletOutputStream os=resp.getOutputStream();
				String csv=controller.getScheduleAsCSV(s);
				os.write(csv.getBytes());
				os.flush();
				os.close();
				return;
			}catch(IOException e){
				errorMessage="Download failed. Please try again.";
				e.printStackTrace();
			}
		}
		
		if(req.getParameter("dlAsHtml")!=null){
			resp.setContentType("text/html");
			resp.setHeader("Content-Disposition", "attachment; filename=\"schedule.html\"");
			try{
				ServletOutputStream os=resp.getOutputStream();
				String html=generateHTMLScheduleTableForDownload(s);
				os.write(html.getBytes());
				os.flush();
				os.close();
				return;
			}catch(IOException e){
				errorMessage="Download failed. Please try again.";
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<s.getCourses().size(); i++){
			Course c=s.getCourses().get(i);
			if(req.getParameter("" + c.getCRN())!=null){
				session.getCurrentUser().getSchedules().firstEntry().getValue().deleteCourse(c.getCRN());
			}
			if(req.getParameter(c.getCRN() + "View")!=null){
				String returnedCourses="<tr><td>CRN</td><td>Course</td><td>Title</td>" +
						"<td>Credits</td><td>Type</td><td>Days</td><td>Time</td><td>Location 1</td>" +
						"<td>Location 2</td><td>Instructor 1</td><td>Instructor 2</td><td>Capacity</td> " +
						"<td>Seats Open</td><td>Enrolled</td><td>Begin-End</td><td>Add to Schedule</td></tr>";
				returnedCourses=returnedCourses+("<tr><td>" + c.getCRN() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getCourseAndSection() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getTitle() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getCredits() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getType() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getDays() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getTime() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getLocation().get(0) + "</td>");
				if(c.getLocation().size()>1){
					returnedCourses=returnedCourses+("<td>" + c.getLocation().get(1) + "</td>");
				}
				else{
					returnedCourses=returnedCourses+("<td> </td>");
				}
				returnedCourses=returnedCourses+("<td>" + c.getInstructors().get(0) + "</td>");
				if(c.getInstructors().size()>1){
					returnedCourses=returnedCourses+("<td>" + c.getInstructors().get(1) + "</td>");
				}
				else{
					returnedCourses=returnedCourses+("<td> </td>");
				}
				returnedCourses=returnedCourses+("<td>" + c.getCapacity() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getSeatsRemain() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getSeatsFilled() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getBeginEnd() + "</td>");
				returnedCourses=returnedCourses+"<td><input type=\"submit\" name=\"" 
						+ c.getCRN() + "\" value=\"Add to Schedule\"</tr>";
				
				req.setAttribute("returnedCourses", returnedCourses);
				RequestDispatcher rd=req.getRequestDispatcher("search");
				rd.forward(req, resp);
				return;
			}
		}
		
		try {
			session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
		} catch (SQLException e) {
			errorMessage="Error saving data.";
			e.printStackTrace();
		}
		
		// Add parameters as request attributes
		//req.getSession().setAttribute("userSession", session);
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.setAttribute("errorMessage", errorMessage);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}

	/*private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}*/
	
	public String generateHTMLScheduleTable(Schedule s){
		Collections.sort(s.getCourses(), new SortCoursesComparator());
		
		Table scheduleTable=new Table();
		scheduleTable.setCSSClass("scheduleTable");
		Tr dayRow=new Tr();
		Th time=new Th(); time.appendText("Time");
		Th mon=new Th(); mon.appendText("Monday");
		Th tue=new Th(); tue.appendText("Tuesday");
		Th wed=new Th(); wed.appendText("Wednesday");
		Th th=new Th(); th.appendText("Thursday");
		Th fri=new Th(); fri.appendText("Friday");
		dayRow.appendChild(time, mon, tue, wed, th, fri);
		scheduleTable.appendChild(dayRow);
		TreeMap<Integer, String> colors=mapCoursesToColors(s);
		
		String[] days={"m", "t", "w", "r", "f"};
		
		Object[][] coordinates=new Object[5][28];
		
		for(Course c : s.getCourses()){
			for(int i=0; i<days.length; i++){
				if(c.getDays().toLowerCase().contains(days[i])){
					int startHr=Integer.parseInt(c.getTime().substring(0, c.getTime().indexOf(':')));
					if(c.getTime().substring(0, c.getTime().indexOf('-')).toLowerCase().contains("pm") && startHr!=12){
						startHr+=12;
					}
					
					int startMin=Integer.parseInt(c.getTime().substring(c.getTime().indexOf(':')+1, c.getTime().indexOf(':')+3));
					
					String timeLastHalf=c.getTime().substring(c.getTime().indexOf('-')+1);
					int endHr=Integer.parseInt(timeLastHalf.substring(0, timeLastHalf.indexOf(':')));
					if(timeLastHalf.toLowerCase().contains("pm") && endHr!=12){
						endHr+=12;
					}
					
					int endMin=Integer.parseInt(timeLastHalf.substring(timeLastHalf.indexOf(':')+1, timeLastHalf.indexOf(':')+3));
					
					int xCoord=i;
					int yCoord=(startHr-8)*2;
					if(startMin!=0){
						yCoord+=1;
					}
					
					int rowspan=(startHr-endHr)*2;
					if(startMin==0){
						if(endMin!=0){
							rowspan+=1;
						}
					} else{
						if(endMin==0){
							rowspan-=1;
						}
					}
					
					if(rowspan<2){
						rowspan=2;
					}
					
					Td newCell=new Td();
					newCell.setId("" + c.getCRN());
					newCell.setBgcolor(colors.get(c.getCRN()));
					newCell.setRowspan("" + rowspan);
					newCell.appendText(c.getTitle());
					newCell.appendChild(new Br());
					newCell.appendText(c.getCourseAndSection());
					newCell.appendChild(new Br());
					newCell.appendText(c.getTime());
					Input submit=new Input();
					submit.setType("submit");
					submit.setCSSClass("btn");
					submit.setName("" + c.getCRN());
					submit.setValue("Remove");
					Input moreInfo=new Input();
					moreInfo.setType("submit");
					moreInfo.setCSSClass("btn");
					moreInfo.setName(c.getCRN() + "View");
					moreInfo.setValue("More Info");
					newCell.appendChild(new Br());
					newCell.appendChild(submit);
					newCell.appendChild(moreInfo);
					
					coordinates[xCoord][yCoord]=newCell;
					for(int r=1; r<rowspan; r++){
						coordinates[xCoord][yCoord+r]="filled";
					}
				}
			}
		}
		
		Td[] timeCells=new Td[28];
		
		int counter=0;
		for(int i=8; i<22; i++){
			for(int j=0; j<2; j++){
				Td timeCell=new Td();
				String timeText="";
				String amPm="";
				if(i>=12){
					amPm="PM";
				} else{
					amPm="AM";
				}
				if(i>12){
					timeText="" + (i-12);
				} else{
					timeText="" + i;
				}
				if(j==0){
					timeText=timeText + ":00" + amPm;
				} else{
					timeText=timeText + ":30" + amPm;
				}
				
				timeCell.appendText(timeText);
				timeCells[counter]=timeCell;
				counter++;
			}
		}
		
		for(int y=0; y<28; y++){
			Tr currRow=new Tr();
			Td timeCell=timeCells[y];
			if(timeCell!=null){
				currRow.appendChild(timeCell);
			}
			for(int x=0; x<5; x++){
				if(coordinates[x][y] instanceof Td){
					Td td=(Td)coordinates[x][y];
					currRow.appendChild(td);
				} else if(coordinates[x][y]==null){
					Td empty=new Td();
					empty.appendText("&nbsp;");
					currRow.appendChild(empty);
				}
			}
			scheduleTable.appendChild(currRow);
		}
		
//		for(int y=0; y<28; y++){
//			for(int x=0; x<5; x++){
//				if(coordinates[x][y] instanceof Td){
//					Td td=(Td)coordinates[x][y];
//					System.out.print(td.getId() + "     ");
//				} else{
//					System.out.print(coordinates[x][y] + "     ");
//				}
//			}
//			System.out.println("\n");
//		}
		
		String html=scheduleTable.write();
		//System.out.println(html);
		
		return html;
	}
	
	public String generateHTMLScheduleTableForDownload(Schedule s){
		String css=".scheduleTable {\n" + 
				"	font-family:Arial;\n" + 
				"	width: 95%;\n" + 
				"	text-align:center;\n" + 
				"	margin:0 auto;\n" + 
				"}\n" + 
				".scheduleTable td{\n" + 
				"	line-height:20px;\n" + 
				"	font-family:Arial;\n" + 
				"	width:18%;\n" + 
				"	padding:0px;\n" + 
				"}\n" + 
				".scheduleTable td:first-child{\n" + 
				"	line-height:20px;\n" + 
				"	border-style: solid;\n" + 
				"	border-width:1px;\n" + 
				"	border-color:#000000;\n" + 
				"	font-family:Arial;\n" + 
				"	width: 10%;\n" + 
				"	padding:10px;\n" + 
				"}\n" + 
				".scheduleTable th{\n" + 
				"	text-align: center;\n" + 
				"	background: #008080;\n" + 
				"}";
		
		TreeMap<Integer, String> colors=mapCoursesToColors(s);
		String html="<style>" + css + "</style>";
		html=html+"<table class=\"scheduleTable\">" + 
				"<tr>" + 
				"	<th>Time</th>" + 
				"	<th>Monday</th>" + 
				"	<th>Tuesday</th>" + 
				"	<th>Wednesday</th>" + 
				"	<th>Thursday</th>" + 
				"	<th>Friday</th>" + 
				"</tr>";
		String[] days={"m", "t", "w", "r", "f"};
		
		for(int j=8; j<=22; j++){
			int timeInt=j%12;
			if(timeInt==0){
				timeInt=12;
			}
			String timeHr="" + timeInt;
			String amPm;
			if(j>11){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
			for(int k=0; k<2; k++){
				String timeMin="";
				if(k==0){
					timeMin="00";
					
				}
				else{
					timeMin="30";
				}
				html=html + "<tr>" +
						"<td>" + timeHr + ":" + timeMin + amPm + "</td>";
				
				for(int i=0; i<days.length; i++){
					for(Course c : s.getCourses()){
						String currentColor=colors.get(c.getCRN());
						if((c.getTime().substring(0, c.getTime().indexOf(':')).equals(timeHr) || c.getTime().substring(0, c.getTime().indexOf(':')).equals("0" + timeHr)) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeMin) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
							if(c.getDays().toLowerCase().contains(days[i])){
								String timeLastHalf=c.getTime().substring(c.getTime().indexOf('-')+1);
								int startHr=Integer.parseInt(timeHr);
								int endHr=Integer.parseInt(timeLastHalf.substring(0, timeLastHalf.indexOf(':')));
								int numCells=endHr-startHr;
								numCells=numCells*2;
								if(c.getTime().substring(0, c.getTime().indexOf('-')).contains("30")){
									if(!timeLastHalf.contains("30")){
										numCells=numCells-1;
									}
								}
								else{
									if(timeLastHalf.contains("30")){
										numCells=numCells+1;
									}
								}
								if(numCells<1){
									numCells=2;
								}
								
								html=html + "<td style=\"background:" + currentColor + ";\" rowspan=\"" + numCells + "\">" + c.getTitle() + "<br>" +  c.getCourseAndSection() + "<br>" + c.getTime() + "<br>";
								html=html + "</td>";
							}
							else{
								html=html+"<td></td>";
							}
						}
					}
					
				}
				html=html + "</tr>";
			}
		}
		html=html + "</table>";
		
		html=css+html;
		
		return html;
	}
	
	public TreeMap<Integer, String> mapCoursesToColors(Schedule s){
		String[] colors={"#FF5733", "#FFBD33", "#33FF57", "#33FFBD", "#71FF33",
				 		 "#5B33FF", "#C133FF", "#FF5B33", "#FFC300"};
		TreeMap<Integer, String> result=new TreeMap<Integer, String>();
		int i=0;
		for(Course c : s.getCourses()){
			result.put(c.getCRN(), colors[i]);
			i++;
		}
		return result;
	}
}
