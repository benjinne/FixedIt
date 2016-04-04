package fixedIt.controllers;

import fixedIt.modelComponents.User;

public class DisplayScheduleController {
	User user;
	
	public DisplayScheduleController(User user){
		this.user=user;
	}
	
	public void initializeSchedule(){
		if(user.getSchedules().isEmpty()){
			user.createSchedule("testSchedule");
		}
	}

}
