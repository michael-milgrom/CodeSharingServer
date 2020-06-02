package smartspace.layout;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.ActionsService;

@RestController
public class ActionController {
	
	private ActionsService actionsService;

	
	@Autowired
	public ActionController(ActionsService actionsService) {
		this.actionsService = actionsService;
	}
	
	@RequestMapping(
			path="/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] newActions (
			@RequestBody ActionBoundary[] actions, 
			@PathVariable("adminSmartspace") String adminSmartspace, 
			@PathVariable("adminEmail") String adminEmail) {
		
		return this.actionsService.newActions(Arrays.asList(actions)
				.stream()
				.map(ActionBoundary::convertToEntity)
				.collect(Collectors.toList())
				,adminSmartspace, adminEmail)
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ActionBoundary[0]);
	}

	@RequestMapping(
			path="/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActionsUsingPagination (
			@PathVariable("adminSmartspace") String adminSmartspace, 
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
			this.actionsService
			.getActionsUsingPagination(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(ActionBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ActionBoundary[0]);
	}
	
	

}
