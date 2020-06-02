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
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary newElement(
			@RequestBody ElementBoundary element, 
			@PathVariable("managerSmartspace") String userSmartspace, 
			@PathVariable("managerEmail") String userEmail) {		
		return new ElementBoundary(elementsService.newElement(userSmartspace, userEmail,null,element.convertToEntity()));		
			}


	@RequestMapping(
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}",
			method=RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void patchElement (
			@PathVariable("managerSmartspace") String userSmartspace, 
			@PathVariable("managerEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary element){
			this.elementsService
			.setElement(userSmartspace, userEmail,null,elementSmartspace,elementId,element.convertToEntity());
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getSpecipicElementUsingId (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId) {
		return 
			new ElementBoundary(elementsService
			.getSpecificElement(userSmartspace, userEmail,null, elementSmartspace,elementId));
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPagination (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPagination(userSmartspace, userEmail,null, size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/search=location",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfLocation (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="x", required=false, defaultValue="0") int x,
			@RequestParam(name="y", required=false, defaultValue="0") int y,
			@RequestParam(name="distance", required=false, defaultValue="5") int distance,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfLocation(userSmartspace, userEmail, null, x, y, distance, size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/search=name",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfSpecifiedName (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="value", required=true) String name,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfName(userSmartspace, userEmail, null, name, size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}

	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/search=type",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfSpecifiedType (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="value", required=true) String type,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfSpecifiedType(userSmartspace, userEmail, null, type, size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
}
