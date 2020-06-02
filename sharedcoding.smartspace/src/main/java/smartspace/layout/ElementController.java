package smartspace.layout;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.ElementsService;

@RestController
public class ElementController {

	private ElementsService elementsService;

	@Autowired
	public ElementController(ElementsService elementService) {
		this.elementsService = elementService;
	}
	
	

	@RequestMapping(
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ElementBoundary> newElement (
			@RequestBody ElementBoundary[] elements, 
			@PathVariable("adminSmartspace") String adminSmartspace, 
			@PathVariable("adminEmail") String adminEmail) {		
		return this.elementsService.newElements(Arrays.asList(elements)
				.stream()
				.map(ElementBoundary::convertToEntity)
				.collect(Collectors.toList())
				,adminSmartspace, adminEmail)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList());
	}

	@RequestMapping(
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPagination (
			@PathVariable("adminSmartspace") String adminSmartspace, 
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
			this.elementsService
			.getElementsUsingPagination(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
}














