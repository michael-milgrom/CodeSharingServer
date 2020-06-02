package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.UserUsersServiceImpl;

@RestController
public class UserUsersController {

	private UserUsersServiceImpl userService;

	@Autowired
	public UserUsersController(UserUsersServiceImpl userService) {
		this.userService = userService;
	}
	
	
	@RequestMapping(
			path="/smartspace/users",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary newElement (
			@RequestBody UserForm newUser) {		
			return new UserBoundary(this.userService.newUser(userService.convertToUserEntity(newUser)));	
			}
	
	@RequestMapping(
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUser (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail) {		
			return new UserBoundary(this.userService.getUser(userSmartspace,userEmail));	
			}
	
	@RequestMapping(
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@RequestBody UserBoundary user,
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail) {		
			this.userService.updateUser(userSmartspace,userEmail,null,user.convertToEntity());	
			}
}
