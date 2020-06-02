package smartspace.layout;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.ActionsUserServiceImpl;

@RestController
public class ActionUserController {

	private ActionsUserServiceImpl actionService;

	@Autowired
	public ActionUserController(ActionsUserServiceImpl actionService) {
		this.actionService = actionService;
	}
	
	
	@RequestMapping(
			path="/smartspace/actions",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> invokeAnAction(
			@RequestBody ActionBoundary action) {		
		return actionService.invokeAction(action.getPlayer().getSmartspace(),action.getPlayer().getEmail(), null,action.convertToEntity());
	}
}

