package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.web.client.RestTemplate;

import smartspace.layout.ElementBoundary;
import smartspace.layout.LocationForBoundary;
import smartspace.layout.UserForBoundary;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;
import smartspace.infra.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class ElementControllerIntegrationTest {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private ElementsService elementService;

	@Value("${smartspace.name}")
	private String appSmartSpace;
	private String adminEmail = "admin@admin";

	@Autowired
	public void setElementService(ElementsService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@PostConstruct
	public void init() {
		this.userDao.create(new UserEntity("appSmartSpace", adminEmail, "admin", "adminAvatar", ActionType.ADMIN, 0));
		
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements";
	}
	

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
	}

	@Test
	public void postNewElement() throws Exception {

		// GIVEN the element data base is empty
		// AND user data base has 1 use which is admin

		// WHEN admin POST new element
		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);
		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");
		Map<String, Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);

		ElementBoundary newElement = new ElementBoundary();

		newElement.setName("Test");
		newElement.setExpired(false);
		newElement.setLatlng(latlng);
		newElement.setCreator(creator);
		newElement.setElementProperties(elementProperties);
		newElement.setElementType("Test");
		
		List<ElementBoundary> elementList = new ArrayList<>();
		elementList.add(newElement);

		this.restTemplate.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}", elementList,
				ElementBoundary.class, appSmartSpace, adminEmail); // TODO How to create the admin? Check if its
																	// good!!!!!

		// THEN the database contains a single element
		assertThat(this.elementDao.readAll()).hasSize(1);
	}

	@Test
	public void postListOfElements() throws Exception {

		// GIVEN the element data base is empty
		// AND user data base has 1 use which is admin

		// WHEN admin POST new list of 5 elements
		int size = 5;
		List<ElementBoundary> listOfElement = new ArrayList<ElementBoundary>();
		ElementBoundary newElement = new ElementBoundary();

		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);

		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");

		Map<String, Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);

		IntStream.range(1, size + 1).forEach(i -> {
			newElement.setName("Test");
			newElement.setExpired(false);
			newElement.setLatlng(latlng);
			newElement.setCreator(creator);
			newElement.setElementProperties(elementProperties);
			newElement.setElementType("Test");
			listOfElement.add(newElement);
		});

		this.restTemplate.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}", listOfElement,
				ElementBoundary.class, appSmartSpace, adminEmail); // TODO How to create the admin? Check if its
																	// good!!!!!

		// THEN the database contains the exact amount of elements
		assertThat(this.elementDao.readAll()).hasSize(size);
	}

	@Test(expected = Exception.class)
	public void testPostNewElementWithNotAdmin() throws Exception {
		// GIVEN the database is empty
		// AND user data base has 1 use which is admin

		// WHEN I POST new message with bad code
		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);

		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");
		ElementBoundary newElement = new ElementBoundary();
		Map<String, Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);

		newElement.setName("Test");
		newElement.setExpired(false);
		newElement.setLatlng(latlng);
		newElement.setCreator(creator);
		newElement.setElementProperties(elementProperties);
		newElement.setElementType("Test");

		this.restTemplate.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}", newElement,
				ElementBoundary.class, "Not our smartspace", "NotAdmin"); // TODO How to create the admin? Check if its
																			// good!!!!!

		// THEN the test end with exception

		// assertThat(restTemplate)
	}

	@Test
	public void testGetAllElementsUsingPagination() throws Exception {
		// GIVEN the database contains 5 elements
		// AND user data base has 1 use which is admin
		int size = 5;
		IntStream.range(1, size + 1).mapToObj(i -> new ElementEntity()).forEach(this.elementDao::create);

		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?size={size}&page={page}", ElementBoundary[].class,
				appSmartSpace, adminEmail, 10, 0);

		// THEN I receive 3 messages
		assertThat(response).hasSize(size);
	}

	@Test
	public void testGetAllElementsUsingPaginationAndValidateContent() throws Exception {
		// GIVEN the database contains 5 Elements
		// AND user data base has 1 use which is admin
		int size = 5;
		java.util.List<ElementBoundary> allElements = IntStream
				.range(1, size + 1).mapToObj(i -> new ElementEntity(null, "Test" + i, "type", new Date(), true,
						appSmartSpace, adminEmail, null))
				.map(this.elementDao::create).map(ElementBoundary::new).collect(Collectors.toList());

		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?size={size}&page={page}", ElementBoundary[].class,
				appSmartSpace, adminEmail, 10, 0);

		// THEN I receive the exact 5 elements written to the database
		assertThat(response).usingElementComparatorOnFields("key").containsExactlyElementsOf(allElements);
	}

	@Test(expected = Exception.class)
	public void testGetAllElementUsingPaginationWithNotAdmin() throws Exception {
		// GIVEN the database contains 1 element
		// AND user data base has 1 use which is admin
		elementDao.create(new ElementEntity());

		// WHEN I GET messages of size 10 and page 0

		this.restTemplate.getForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?size={size}&page={page}",
				ElementBoundary[].class, "Not our smartspace", "NotAdmin", 10, 0);

		// THEN the test ends with exception
	}

	@Test
	public void testGetAllElementsUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception {
		// GIVEN the database contains 5 elements
		// AND user data base has 1 use which is admin

		int size = 5;
		Map<String, Object> details = new HashMap<>();
		details.put("y", 10.0);
		details.put("x", "10");

		List<ElementEntity> allElements = new ArrayList<ElementEntity>();

		for (int i = 1; i < size + 1; i++) {
			ElementEntity newElement = new ElementEntity();
			newElement.setName("Test" + i);
			newElement.setExpired((Math.random() > 0.5) ? true : false);
			newElement.setType("type");
			allElements.add(newElement);
		}

		
		elementService.newElements(allElements, appSmartSpace, adminEmail);

		List<ElementBoundary> elementboundary = new ArrayList<ElementBoundary>();
		allElements.stream().forEach(element -> elementboundary.add(new ElementBoundary(element)));

		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?size={size}&page={page}", ElementBoundary[].class,
				appSmartSpace, adminEmail, 10, 0);

		// THEN I receive the exact messages written to the database

		assertThat(response).usingElementComparatorOnFields("key", "elementType", "name", "expired") // TODO Add creator
				.containsExactlyElementsOf(elementboundary);
	}

	@Test
	public void testGetAllElementsUsingPaginationOfSecondPage() throws Exception {
		// GIVEN then database contains 11 Elements
		// AND user data base has 1 use which is admin

		int size = 11;
		List<ElementEntity> allElements = new ArrayList<ElementEntity>();
		for (int i = 1; i < size + 1; i++) {
			ElementEntity newElement = new ElementEntity();
			newElement.setName("Test" + i);
			newElement.setExpired((Math.random() > 0.5) ? true : false);
			newElement.setType("type");
			allElements.add(newElement);
		}

//	MessageBoundary last = new MessageBoundary( 
//	  all
//		.stream()
//		.sorted((e1,e2)->e2.getKey().compareTo(e1.getKey()))
//		.findFirst()
//		.orElseThrow(()->new RuntimeException("no messages after sorting")));

		ElementBoundary last = allElements.stream().skip(10).limit(1).map(ElementBoundary::new).findFirst()
				.orElseThrow(() -> new RuntimeException("no elements after skipping"));

		// WHEN I GET elements of size 10 and page 1
		ElementBoundary[] result = this.restTemplate.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?page={page}&size={size}", ElementBoundary[].class,
				appSmartSpace, adminEmail, 1, 10);

		// THEN the result contains a single element(last message)
		assertThat(result).usingElementComparator((b1, b2) -> b1.toString().compareTo(b2.toString()))
				.containsExactly(last);
	}

	@Test
	public void testGetAllMessagesUsingPaginationOfSecondNonExistingPage() throws Exception {
		// GIVEN the database contains 10 messages
		IntStream.range(0, 10).forEach(i -> this.elementDao.create(new ElementEntity()));

		// WHEN I GET messages of size 10 and page 1
		String[] result = this.restTemplate.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}" + "?size={size}&page={pp}", String[].class,
				appSmartSpace, adminEmail, 10, 1);

		// THEN the result is empty
		assertThat(result).isEmpty();

	}
}
