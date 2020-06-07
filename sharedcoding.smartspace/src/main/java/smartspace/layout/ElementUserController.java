package smartspace.layout;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.ElementsUserServiceImpl;

@RestController
public class ElementUserController {

	private ElementsUserServiceImpl elementsService;

	@Autowired
	public ElementUserController(ElementsUserServiceImpl elementService) {
		this.elementsService = elementService;
	}
	

	@RequestMapping(
			path="/elements/{creatorEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary newElement(
			@RequestBody ElementBoundary element, // TODO FIX?
			@PathVariable("creatorEmail") String userEmail) {		
		return new ElementBoundary(elementsService.newElement(element.convertToEntity()));		
			}

	
	@RequestMapping(
			path="/elements/{userEmail}/{elementKey}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getSpecipicElementUsingId (
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementKey") String elementKey) {
		return new ElementBoundary(elementsService.getSpecificElement(elementKey));
	}
	
	@RequestMapping(
			path="/elements/{userEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPagination (
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPagination(userEmail, size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}


	@RequestMapping(
			path="/elements/{userEmail}/{elementKey}/{type}",
			method=RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void patchElement (
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementKey") String elementKey,
			@RequestBody ElementBoundary element){
			this.elementsService
			.setElement(elementKey,element.convertToEntity()); // TODO SEND EMAIL TOO
	}
}
