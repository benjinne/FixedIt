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

import com.hp.gagawa.java.elements.*;

import fixedIt.controllers.DisplayScheduleController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Schedule;
import fixedIt.modelComponents.Session;
import fixedIt.modelComponents.SortCoursesComparator;


public class DisplayScheduleServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String errorMessage="";
		if((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession")!=null){
			try {
				((Session) req.getSession().getAttribute("userSession")).getAuth().saveExistingUserNewDataToDB(((Session) req.getSession().getAttribute("userSession")).getCurrentUser());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		if(session==null){
			resp.sendRedirect("login");
			return;
		}
		Schedule s=null;
		String scheduleName="";
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		
		if(controller.getUser().getSchedules().size()!=0){
			if(controller.getUser().getActiveSchedule()!=null){
				s=controller.getUser().getActiveSchedule();
				scheduleName=s.getName();
			} else{
				errorMessage="Active schedule not set.";
			}
		} else{
			errorMessage="No schedules exits for user; create a new one first.";
		}
		
		String html=generateHTMLScheduleTable(s);
		
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("scheduleHTML", html);
		req.setAttribute("scheduleName", scheduleName);
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		
		Schedule s;
		String scheduleName="";
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		try{
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
			scheduleName=s.getName();
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
		
		if(req.getParameter("delete")!=null){
			controller.getUser().getSchedules().remove(scheduleName);
			controller.getUser().setActiveSchedule(null);
			try {
				session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
				resp.sendRedirect("userInfo");
				return;
			} catch (SQLException e) {
				errorMessage="Error saving user data. Please try again.";
				e.printStackTrace();
			}
			
		}
		
		try {
			session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
		} catch (SQLException e) {
			errorMessage="Error saving data.";
			e.printStackTrace();
		}
		
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("scheduleName", scheduleName);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}
	
	public String generateHTMLScheduleTable(Schedule s){
		if(s==null){
			return null;
		}
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
		int numRowsUsed=0, index=0;
		
		for(Course c : s.getCourses()){
			int xCoord=0, yCoord=0, rowspan=0;
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
					
					xCoord=i;
					yCoord=(startHr-8)*2;
					if(startMin!=0){
						yCoord+=1;
					}
					rowspan=(Math.abs(startHr-endHr))*2;
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
					submit.setCSSClass("btn courseBlock");
					submit.setName("" + c.getCRN());
					submit.setValue("Remove");
					Input moreInfo=new Input();
					moreInfo.setType("submit");
					moreInfo.setCSSClass("btn courseBlock");
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
			if(index==s.getCourses().size()-1){
				numRowsUsed=yCoord+(rowspan-1);
			}
			index++;
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
			if(numRowsUsed>0){
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
						currRow.appendChild(empty);
					}
				}
				if(y<=numRowsUsed+2){
					scheduleTable.appendChild(currRow);
				}
			}
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
		String html="<div class=\"styled-body\">";
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
		int numRowsUsed=0, index=0;
		
		for(Course c : s.getCourses()){
			int xCoord=0, yCoord=0, rowspan=0;
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
					
					xCoord=i;
					yCoord=(startHr-8)*2;
					if(startMin!=0){
						yCoord+=1;
					}
					rowspan=(Math.abs(startHr-endHr))*2;
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
					
					coordinates[xCoord][yCoord]=newCell;
					for(int r=1; r<rowspan; r++){
						coordinates[xCoord][yCoord+r]="filled";
					}
				}
			}
			if(index==s.getCourses().size()-1){
				numRowsUsed=yCoord+(rowspan-1);
			}
			index++;
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
			if(numRowsUsed>0){
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
						currRow.appendChild(empty);
					}
				}
				if(y<=numRowsUsed+2){
					scheduleTable.appendChild(currRow);
				}
			}
		}
		
		html=html+scheduleTable.write();
		//System.out.println(html);
		
		html=CSS+html;
		html=html+"</div>";
		
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
	
	public static String CSS="<style type=\"text/css\">" +
			".option {\r\n" + 
			"	color: black;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".textInput {\r\n" + 
			"	margin: 0 auto;\r\n" + 
			"	width: 200px;\r\n" + 
			"	border-radius: 25px;\r\n" + 
			"    border: 5px solid white; \r\n" + 
			"    -webkit-box-shadow: \r\n" + 
			"      inset 0 0 8px  rgba(0,0,0,0.1),\r\n" + 
			"            0 0 16px rgba(0,0,0,0.1); \r\n" + 
			"    -moz-box-shadow: \r\n" + 
			"      inset 0 0 8px  rgba(0,0,0,0.1),\r\n" + 
			"            0 0 16px rgba(0,0,0,0.1); \r\n" + 
			"    box-shadow: \r\n" + 
			"      inset 0 0 8px  rgba(0,0,0,0.1),\r\n" + 
			"            0 0 16px rgba(0,0,0,0.1); \r\n" + 
			"    padding: 15px;\r\n" + 
			"    background: rgba(255,255,255,0.5);\r\n" + 
			"    margin: 0 0 10px 0;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".sideBar {\r\n" + 
			"	position: absolute;\r\n" + 
			"	border-radius: 0.3rem;\r\n" + 
			"	top: 10px;\r\n" + 
			"	right: 0px;\r\n" + 
			"	background-color: rgba(0, 0, 0, 0.9);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".sideBarBtn {\r\n" + 
			"  display: inline-block;\r\n" + 
			"  width: 180px;\r\n" + 
			"  height: 40px;\r\n" + 
			"  margin-bottom: 0px;\r\n" + 
			"  color: rgba(255, 255, 255, 0.7);\r\n" + 
			"  background-color: rgba(255, 255, 255, 0.08);\r\n" + 
			"  border-color: rgba(255, 255, 255, 0.2);\r\n" + 
			"  border-style: solid;\r\n" + 
			"  border-width: 1px;\r\n" + 
			"  border-radius: 0.3rem;\r\n" + 
			"  transition: color 0.2s, background-color 0.2s, border-color 0.2s; }\r\n" + 
			"  .btn + .btn {\r\n" + 
			"    margin-left: 1rem; }\r\n" + 
			"    \r\n" + 
			".sideBarBtn:hover {\r\n" + 
			"  color: rgba(255, 255, 255, 0.8);\r\n" + 
			"  text-decoration: none;\r\n" + 
			"  background-color: rgba(255, 255, 255, 0.2);\r\n" + 
			"  border-color: rgba(255, 255, 255, 0.3); }\r\n" + 
			"\r\n" + 
			".sideBarTable {\r\n" + 
			"	display: table-cell;\r\n" + 
			"	vertical-align: middle;\r\n" + 
			"	padding: 10px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".scheduleTable {\r\n" + 
			"	font-family:Arial;\r\n" + 
			"	width: 95%;\r\n" + 
			"	border-left-style:solid;\r\n" + 
			"	border-left-width:1px;\r\n" + 
			"	border-left-color:#000000;\r\n" + 
			"	border-right-style:solid;\r\n" + 
			"	border-right-width:1px;\r\n" + 
			"	border-right-color:#000000;\r\n" + 
			"	border-bottom-style:solid;\r\n" + 
			"	border-bottom-width:1px;\r\n" + 
			"	border-bottom-color:#000000;	\r\n" + 
			"	text-align:center;\r\n" + 
			"	margin:0 auto;\r\n" + 
			"}\r\n" + 
			".scheduleTable td{\r\n" + 
			"	width:19%;\r\n" + 
			"	color: #1a1a1a;\r\n" + 
			"	line-height:20px;\r\n" + 
			"	font-family:Arial;\r\n" + 
			"	padding:5px;\r\n" + 
			"	border-left-style:solid;\r\n" + 
			"	border-left-width:1px;\r\n" + 
			"	border-left-color:#000000;\r\n" + 
			"	border-right-style:solid;\r\n" + 
			"	border-right-width:1px;\r\n" + 
			"	border-right-color:#000000;\r\n" + 
			"	border-radius:8px;\r\n" + 
			"    -moz-border-radius:8px;\r\n" + 
			"}\r\n" + 
			".scheduleTable td:first-child{\r\n" + 
			"	color: #ffffff;\r\n" + 
			"	font-weight:bold;\r\n" + 
			"	line-height:50px;\r\n" + 
			"	width:5%;\r\n" + 
			"}\r\n" + 
			".scheduleTable td:empty{\r\n" + 
			"	background-color:rgba(255, 255, 255, 0.2);\r\n" + 
			"	border-radius:0px;\r\n" + 
			"}\r\n" + 
			".scheduleTable th{\r\n" + 
			"	text-align: center;\r\n" + 
			"	line-height:20px;\r\n" + 
			"	border-style: solid;\r\n" + 
			"	border-width:1px;\r\n" + 
			"	border-color:#000000;\r\n" + 
			"}\r\n" + 
			".scheduleTable th:first-child{\r\n" + 
			"	width:5%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".scheduleTable tr:nth-child(odd){\r\n" + 
			"	border-bottom-style:solid;\r\n" + 
			"	border-bottom-width:1px;\r\n" + 
			"	border-color:#000000;\r\n" + 
			"}\r\n" + 
			".scheduleTable tr:last-child{\r\n" + 
			"	border-bottom-color:#000000;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".course-table {\r\n" + 
			"	width:100%;\r\n" + 
			"}\r\n" + 
			"  \r\n" + 
			".styled-body {\r\n" + 
			"  margin:0px;\r\n" + 
			"  height: 207vh;\r\n" + 
			"  color: #fff;\r\n" + 
			"  text-align: center;\r\n" + 
			"  background-color: #159957;\r\n" + 
			"  background-image: linear-gradient(120deg, #155799, #159957);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".courseTable {\r\n" + 
			"	font-family: arial;\r\n" + 
			"	margin:0px;\r\n" + 
			"	padding:5px;\r\n" + 
			"	width:100%;\r\n" + 
			"	box-shadow: 10px 10px 5px #888888;\r\n" + 
			"	border:1px solid #000000;\r\n" + 
			"	\r\n" + 
			"	-moz-border-radius-bottomleft:0px;\r\n" + 
			"	-webkit-border-bottom-left-radius:0px;\r\n" + 
			"	border-bottom-left-radius:0px;\r\n" + 
			"	\r\n" + 
			"	-moz-border-radius-bottomright:0px;\r\n" + 
			"	-webkit-border-bottom-right-radius:0px;\r\n" + 
			"	border-bottom-right-radius:0px;\r\n" + 
			"	\r\n" + 
			"	-moz-border-radius-topright:0px;\r\n" + 
			"	-webkit-border-top-right-radius:0px;\r\n" + 
			"	border-top-right-radius:0px;\r\n" + 
			"	\r\n" + 
			"	-moz-border-radius-topleft:0px;\r\n" + 
			"	-webkit-border-top-left-radius:0px;\r\n" + 
			"	border-top-left-radius:0px;\r\n" + 
			"}.courseTable table{\r\n" + 
			"    border-collapse: collapse;\r\n" + 
			"    border-spacing: 0;\r\n" + 
			"    text-align: center;\r\n" + 
			"	width:100%;\r\n" + 
			"	height:100%;\r\n" + 
			"	margin:0px;padding:0px;\r\n" + 
			"}.courseTable tr:last-child td:last-child {\r\n" + 
			"	-moz-border-radius-bottomright:0px;\r\n" + 
			"	-webkit-border-bottom-right-radius:0px;\r\n" + 
			"	border-bottom-right-radius:0px;\r\n" + 
			"}\r\n" + 
			".courseTable table tr:first-child td:first-child {\r\n" + 
			"	-moz-border-radius-topleft:0px;\r\n" + 
			"	-webkit-border-top-left-radius:0px;\r\n" + 
			"	border-top-left-radius:0px;\r\n" + 
			"}\r\n" + 
			".courseTable table tr:first-child td:last-child {\r\n" + 
			"	-moz-border-radius-topright:0px;\r\n" + 
			"	-webkit-border-top-right-radius:0px;\r\n" + 
			"	border-top-right-radius:0px;\r\n" + 
			"}.courseTable tr:last-child td:first-child{\r\n" + 
			"	-moz-border-radius-bottomleft:0px;\r\n" + 
			"	-webkit-border-bottom-left-radius:0px;\r\n" + 
			"	border-bottom-left-radius:0px;\r\n" + 
			"}.courseTable tr:hover td{\r\n" + 
			"	\r\n" + 
			"}\r\n" + 
			".courseTable tr:nth-child(odd){ background-color:#aad4ff; }\r\n" + 
			".courseTable tr:nth-child(even)    { background-color:#ffffff; }.courseTable td{\r\n" + 
			"	vertical-align:middle;\r\n" + 
			"	width:16.66%;\r\n" + 
			"	border:1px solid #000000;\r\n" + 
			"	border-width:0px 1px 1px 0px;\r\n" + 
			"	text-align:left;\r\n" + 
			"	padding:7px;\r\n" + 
			"	font-size:10px;\r\n" + 
			"	font-family:Arial;\r\n" + 
			"	font-weight:normal;\r\n" + 
			"	color:#000000;\r\n" + 
			"}.courseTable tr:last-child td{\r\n" + 
			"	border-width:0px 1px 0px 0px;\r\n" + 
			"}.courseTable tr td:last-child{\r\n" + 
			"	border-width:0px 0px 1px 0px;\r\n" + 
			"}.courseTable tr:last-child td:last-child{\r\n" + 
			"	border-width:0px 0px 0px 0px;\r\n" + 
			"}\r\n" + 
			".courseTable tr:first-child td{\r\n" + 
			"		background:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );\r\n" + 
			"	background:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );\r\n" + 
			"	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");	background: -o-linear-gradient(top,#005fbf,003f7f);\r\n" + 
			"\r\n" + 
			"	background-color:#005fbf;\r\n" + 
			"	border:0px solid #000000;\r\n" + 
			"	text-align:center;\r\n" + 
			"	border-width:0px 0px 1px 1px;\r\n" + 
			"	font-size:14px;\r\n" + 
			"	font-family:Arial;\r\n" + 
			"	font-weight:bold;\r\n" + 
			"	color:#ffffff;\r\n" + 
			"}\r\n" + 
			".courseTable tr:first-child:hover td{\r\n" + 
			"	background:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );\r\n" + 
			"	background:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );\r\n" + 
			"	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");	background: -o-linear-gradient(top,#005fbf,003f7f);\r\n" + 
			"\r\n" + 
			"	background-color:#005fbf;\r\n" + 
			"}\r\n" + 
			".courseTable tr:first-child td:first-child{\r\n" + 
			"	border-width:0px 0px 1px 0px;\r\n" + 
			"}\r\n" + 
			".courseTable tr:first-child td:last-child{\r\n" + 
			"	border-width:0px 0px 1px 1px;\r\n" + 
			"}" + 
			"/*\r\n" + 
			"* This CSS theme is based on the Github Pages \"Cayman\" theme (github.com/jasonlong/cayman-theme),\r\n" + 
			"*	licensed under a Creative Commons Attribution 4.0 International license.\r\n" + 
			"*/\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"* {\r\n" + 
			"  box-sizing: border-box; }\r\n" + 
			"\r\n" + 
			"body {\r\n" + 
			"  padding: 0;\r\n" + 
			"  margin: 0;\r\n" + 
			"  font-family: \"Open Sans\", \"Helvetica Neue\", Helvetica, Arial, sans-serif;\r\n" + 
			"  font-size: 16px;\r\n" + 
			"  line-height: 1.5;\r\n" + 
			"  color: #606c71; }\r\n" + 
			"\r\n" + 
			"a {\r\n" + 
			"  color: #1e6bb8;\r\n" + 
			"  text-decoration: none; }\r\n" + 
			"  a:hover {\r\n" + 
			"    text-decoration: underline; }\r\n" + 
			"\r\n" + 
			".btn {\r\n" + 
			"  display: inline-block;\r\n" + 
			"  margin-bottom: 1rem;\r\n" + 
			"  color: rgba(255, 255, 255, 0.7);\r\n" + 
			"  background-color: rgba(255, 255, 255, 0.08);\r\n" + 
			"  border-color: rgba(255, 255, 255, 0.2);\r\n" + 
			"  border-style: solid;\r\n" + 
			"  border-width: 1px;\r\n" + 
			"  border-radius: 0.3rem;\r\n" + 
			"  transition: color 0.2s, background-color 0.2s, border-color 0.2s; }\r\n" + 
			"  .btn + .btn {\r\n" + 
			"    margin-left: 1rem; }\r\n" + 
			"\r\n" + 
			".btn:hover {\r\n" + 
			"  color: rgba(255, 255, 255, 0.8);\r\n" + 
			"  text-decoration: none;\r\n" + 
			"  background-color: rgba(255, 255, 255, 0.2);\r\n" + 
			"  border-color: rgba(255, 255, 255, 0.3); }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .btn {\r\n" + 
			"    padding: 0.75rem 1rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .btn {\r\n" + 
			"    padding: 0.6rem 0.9rem;\r\n" + 
			"    font-size: 0.9rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .btn {\r\n" + 
			"    display: block;\r\n" + 
			"    width: 100%;\r\n" + 
			"    padding: 0.75rem;\r\n" + 
			"    font-size: 0.9rem; }\r\n" + 
			"    .btn + .btn {\r\n" + 
			"      margin-top: 1rem;\r\n" + 
			"      margin-left: 0; } }\r\n" + 
			"\r\n" + 
			".page-header {\r\n" + 
			"  color: #fff;\r\n" + 
			"  text-align: center;\r\n" + 
			"  background-color: #159957;\r\n" + 
			"  background-image: linear-gradient(120deg, #155799, #159957); }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .page-header {\r\n" + 
			"    padding: 5rem 6rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .page-header {\r\n" + 
			"    padding: 3rem 4rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .page-header {\r\n" + 
			"    padding: 2rem 1rem; } }\r\n" + 
			"\r\n" + 
			".project-name {\r\n" + 
			"  margin-top: 0;\r\n" + 
			"  margin-bottom: 0.1rem; }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .project-name {\r\n" + 
			"    font-size: 3.25rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .project-name {\r\n" + 
			"    font-size: 2.25rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .project-name {\r\n" + 
			"    font-size: 1.75rem; } }\r\n" + 
			"\r\n" + 
			".project-tagline {\r\n" + 
			"  margin-bottom: 2rem;\r\n" + 
			"  font-weight: normal;\r\n" + 
			"  opacity: 0.7; }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .project-tagline {\r\n" + 
			"    font-size: 1.25rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .project-tagline {\r\n" + 
			"    font-size: 1.15rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .project-tagline {\r\n" + 
			"    font-size: 1rem; } }\r\n" + 
			"\r\n" + 
			".main-content :first-child {\r\n" + 
			"  margin-top: 0; }\r\n" + 
			".main-content img {\r\n" + 
			"  max-width: 100%; }\r\n" + 
			".main-content h1, .main-content h2, .main-content h3, .main-content h4, .main-content h5, .main-content h6 {\r\n" + 
			"  margin-top: 2rem;\r\n" + 
			"  margin-bottom: 1rem;\r\n" + 
			"  font-weight: normal;\r\n" + 
			"  color: #159957; }\r\n" + 
			".main-content p {\r\n" + 
			"  margin-bottom: 1em; }\r\n" + 
			".main-content code {\r\n" + 
			"  padding: 2px 4px;\r\n" + 
			"  font-family: Consolas, \"Liberation Mono\", Menlo, Courier, monospace;\r\n" + 
			"  font-size: 0.9rem;\r\n" + 
			"  color: #383e41;\r\n" + 
			"  background-color: #f3f6fa;\r\n" + 
			"  border-radius: 0.3rem; }\r\n" + 
			".main-content pre {\r\n" + 
			"  padding: 0.8rem;\r\n" + 
			"  margin-top: 0;\r\n" + 
			"  margin-bottom: 1rem;\r\n" + 
			"  font: 1rem Consolas, \"Liberation Mono\", Menlo, Courier, monospace;\r\n" + 
			"  color: #567482;\r\n" + 
			"  word-wrap: normal;\r\n" + 
			"  background-color: #f3f6fa;\r\n" + 
			"  border: solid 1px #dce6f0;\r\n" + 
			"  border-radius: 0.3rem; }\r\n" + 
			"  .main-content pre > code {\r\n" + 
			"    padding: 0;\r\n" + 
			"    margin: 0;\r\n" + 
			"    font-size: 0.9rem;\r\n" + 
			"    color: #567482;\r\n" + 
			"    word-break: normal;\r\n" + 
			"    white-space: pre;\r\n" + 
			"    background: transparent;\r\n" + 
			"    border: 0; }\r\n" + 
			".main-content .highlight {\r\n" + 
			"  margin-bottom: 1rem; }\r\n" + 
			"  .main-content .highlight pre {\r\n" + 
			"    margin-bottom: 0;\r\n" + 
			"    word-break: normal; }\r\n" + 
			".main-content .highlight pre, .main-content pre {\r\n" + 
			"  padding: 0.8rem;\r\n" + 
			"  overflow: auto;\r\n" + 
			"  font-size: 0.9rem;\r\n" + 
			"  line-height: 1.45;\r\n" + 
			"  border-radius: 0.3rem; }\r\n" + 
			".main-content pre code, .main-content pre tt {\r\n" + 
			"  display: inline;\r\n" + 
			"  max-width: initial;\r\n" + 
			"  padding: 0;\r\n" + 
			"  margin: 0;\r\n" + 
			"  overflow: initial;\r\n" + 
			"  line-height: inherit;\r\n" + 
			"  word-wrap: normal;\r\n" + 
			"  background-color: transparent;\r\n" + 
			"  border: 0; }\r\n" + 
			"  .main-content pre code:before, .main-content pre code:after, .main-content pre tt:before, .main-content pre tt:after {\r\n" + 
			"    content: normal; }\r\n" + 
			".main-content ul, .main-content ol {\r\n" + 
			"  margin-top: 0; }\r\n" + 
			".main-content blockquote {\r\n" + 
			"  padding: 0 1rem;\r\n" + 
			"  margin-left: 0;\r\n" + 
			"  color: #819198;\r\n" + 
			"  border-left: 0.3rem solid #dce6f0; }\r\n" + 
			"  .main-content blockquote > :first-child {\r\n" + 
			"    margin-top: 0; }\r\n" + 
			"  .main-content blockquote > :last-child {\r\n" + 
			"    margin-bottom: 0; }\r\n" + 
			".main-content table {\r\n" + 
			"  display: block;\r\n" + 
			"  width: 100%;\r\n" + 
			"  overflow: auto;\r\n" + 
			"  word-break: normal;\r\n" + 
			"  word-break: keep-all; }\r\n" + 
			"  .main-content table th {\r\n" + 
			"    font-weight: bold; }\r\n" + 
			"  .main-content table th, .main-content table td {\r\n" + 
			"    padding: 0.5rem 1rem;\r\n" + 
			"    border: 1px solid #e9ebec; }\r\n" + 
			".main-content dl {\r\n" + 
			"  padding: 0; }\r\n" + 
			"  .main-content dl dt {\r\n" + 
			"    padding: 0;\r\n" + 
			"    margin-top: 1rem;\r\n" + 
			"    font-size: 1rem;\r\n" + 
			"    font-weight: bold; }\r\n" + 
			"  .main-content dl dd {\r\n" + 
			"    padding: 0;\r\n" + 
			"    margin-bottom: 1rem; }\r\n" + 
			".main-content hr {\r\n" + 
			"  height: 2px;\r\n" + 
			"  padding: 0;\r\n" + 
			"  margin: 1rem 0;\r\n" + 
			"  background-color: #eff0f1;\r\n" + 
			"  border: 0; }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .main-content {\r\n" + 
			"    max-width: 64rem;\r\n" + 
			"    padding: 2rem 6rem;\r\n" + 
			"    margin: 0 auto;\r\n" + 
			"    font-size: 1.1rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .main-content {\r\n" + 
			"    padding: 2rem 4rem;\r\n" + 
			"    font-size: 1.1rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .main-content {\r\n" + 
			"    padding: 2rem 1rem;\r\n" + 
			"    font-size: 1rem; } }\r\n" + 
			"\r\n" + 
			".site-footer {\r\n" + 
			"  padding-top: 2rem;\r\n" + 
			"  margin-top: 2rem;\r\n" + 
			"  border-top: solid 1px #eff0f1; }\r\n" + 
			"\r\n" + 
			".site-footer-owner {\r\n" + 
			"  display: block;\r\n" + 
			"  font-weight: bold; }\r\n" + 
			"\r\n" + 
			".site-footer-credits {\r\n" + 
			"  color: #819198; }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 64em) {\r\n" + 
			"  .site-footer {\r\n" + 
			"    font-size: 1rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (min-width: 42em) and (max-width: 64em) {\r\n" + 
			"  .site-footer {\r\n" + 
			"    font-size: 1rem; } }\r\n" + 
			"\r\n" + 
			"@media screen and (max-width: 42em) {\r\n" + 
			"  .site-footer {\r\n" + 
			"    font-size: 0.9rem; } }\r\n" + 
			"\r\n" + 
			"/*//////////////////////////////////////////END TEMPLATE//////////////////////////////////////////////*/\r\n" + 
			"\r\n" + 
			".btn.courseBlock{\r\n" + 
			"	border-style:solid;\r\n" + 
			"	border-width:1px;\r\n" + 
			"	border-color:rgba(0, 0, 0, 0.08);\r\n" + 
			"	padding:5px!important;\r\n" + 
			"	color:rgba(0, 0, 0, 0.7);\r\n" + 
			"	background-color: rgba(255, 255, 255, 0.2);\r\n" + 
			"}\r\n" + 
			".btn.courseBlock:hover{\r\n" + 
			"	color: rgba(0, 0, 0, 0.8);\r\n" + 
			"	border-color: rgba(0, 0, 0, 0.2);\r\n" + 
			"	background-color: rgba(255, 255, 255, 0.4);\r\n" + 
			"}" +
			"</style>";
}
