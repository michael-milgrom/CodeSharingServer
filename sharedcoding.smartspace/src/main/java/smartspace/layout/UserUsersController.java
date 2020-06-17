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
			path="/users",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary newUser (
			@RequestBody UserForm newUser) {		
			return new UserBoundary(this.userService.newUser(userService.convertToUserEntity(newUser)));	
			}
	
	@RequestMapping(
			path="/users/login/{email}/{password}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUser (
			@PathVariable("email") String userEmail,
			@PathVariable("password") String password) {		
			return new UserBoundary(this.userService.getUser(userEmail, password));	
			}
	
	@RequestMapping(
			path="/users/{email}",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@RequestBody UserBoundary user,
			@PathVariable("email") String userEmail) {		
			this.userService.updateUser(user.convertToEntity());	
			}
}
