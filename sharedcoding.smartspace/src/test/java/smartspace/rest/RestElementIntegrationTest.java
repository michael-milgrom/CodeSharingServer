package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;
import smartspace.layout.ElementBoundary;
import smartspace.layout.Key;
import smartspace.layout.LocationForBoundary;
import smartspace.layout.UserForBoundary;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RestElementIntegrationTest {
	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;

	private EnhancedUserDao<String> userDao;
	private UserEntity userEntity;
	private UserEntity userEntityPlayer;
	private EnhancedElementDao<String> elementDao;

	
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
		this.userEntity = new UserEntity();
		this.userEntity.setUserEmail("manager.creating.element@de.mo");
		this.userEntity.setUserSmartspace("2019b.sarieltutay");
		this.userEntity.setRole(ActionType.ADMIN);
		this.userEntity.setAvatar("(:");
		this.userEntity.setUsername("aa");
		this.userEntity.setPoints(1111);
		this.userDao.create(userEntity);
		
		this.userEntityPlayer = new UserEntity();
		this.userEntityPlayer.setUserEmail("a@de.mo");
		this.userEntityPlayer.setUserSmartspace("2019b.sarieltutay");
		this.userEntityPlayer.setRole(ActionType.PLAYER);
		this.userEntityPlayer.setAvatar("(:");
		this.userEntityPlayer.setUsername("bb");
		this.userEntityPlayer.setPoints(1111);
		this.userDao.create(userEntityPlayer);
		
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}";
	}

	@After
	public void teardown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}

	@Test
	public void testInsertSingleElement() throws Exception {
		// GIVEN the database contains admin

		// WHEN I POST a new ElementBoundary
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("val1", "20");
		elementProperties.put("val2", 20);
;
		ElementBoundary newElement = new ElementBoundary();
		newElement.setKey(new Key("3", "smartspace"));
		newElement.setElementType("type");
		newElement.setName("name");
		newElement.setExpired(false);
		newElement.setCreator(new UserForBoundary(this.userEntity.getUserEmail(), "bla"));
		newElement.setLatlng(new LocationForBoundary(22, 23.11));
		newElement.setElementProperties(elementProperties);
		newElement.setCreated(new Date());

		ElementBoundary[] elements = { newElement };
		this.restTemplate.postForObject(this.baseUrl, elements,
				ElementBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail());
		// THEN the database contains the new element
		assertThat(this.elementDao.readAll()).hasSize(1);
	}
	
	
	@Test(expected=Exception.class)
	public void testPostNewElementWithUserBadPermission() throws Exception{
		// GIVEN the database contains player
		// WHEN player POST new element

				Map<String, Object> details = new HashMap<>();
				details.put("val1", "20");
				details.put("val2", 20.0);

				ElementBoundary newElement = new ElementBoundary();
				newElement.setKey(new Key("1", "Smartspace"));
				newElement.setElementType("Type");
				newElement.setName("Name");
				newElement.setElementProperties(details);
				newElement.setExpired(false);
				newElement.setCreated(new Date());
				newElement.setCreator(new UserForBoundary(this.userEntityPlayer.getUserEmail(), "bla"));
				newElement.setLatlng(new LocationForBoundary(31.11, 77.11));
				
				

				ElementBoundary[] elements = { newElement };
			
				this.restTemplate.postForObject(
						this.baseUrl, 
						elements, 
						ElementBoundary[].class, 
						this.userEntityPlayer.getUserSmartspace(),
						this.userEntityPlayer.getUserEmail());
		
		// THEN the test end with exception
	}
	
	@Test
	public void testGetAllElementsUsingPagination() throws Exception {
		// GIVEN the database contains 3 elements
		int size = 3;
		IntStream.range(1, size + 1).mapToObj(i->new ElementEntity("demo" + i))
				.forEach(this.elementDao::create);

		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = this.restTemplate.getForObject(this.baseUrl + "?page={page}&size={size}",
				ElementBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail(), 0, 10);

		// THEN I receive 3 elements
		assertThat(response).hasSize(size);
	}

	@Test
	public void testGetAllElementsUsingPaginationOfSecondPage() throws Exception {
		 //GIVEN the database contains 100 elements
		int size = 100;
		IntStream.range(1, size + 1).mapToObj(i -> "element" + i).map(ElementEntity::new)
				.forEach(this.elementDao::create);

		// WHEN I get elements of the page 1 and size 90
		ElementBoundary[] result = this.restTemplate.getForObject(this.baseUrl + "?page={page}&size={size}",
				ElementBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail(), 1, 90);

		// THEN I receive 10 elements
		assertThat(result).hasSize(10);
		
		
	}

	/*@Test
	public void testInsertUsingRestAndGetElementsUsingRest() throws Exception {
		// GIVEN the database is empty

		// WHEN I POST 3 new elements
		// AND I get elements of page 0 of size 2
		UserForBoundary userKey = new UserForBoundary(this.userEntity.getUserEmail(), this.userEntity.getUserSmartspace());
		LocationForBoundary boundaryLocation = new LocationForBoundary(32.115, 84.817);
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("value", "hello");
		elementProperties.put("value2", 42);
		elementProperties.put("value3", 4.2);

		List<ElementBoundary> newBoundaries = IntStream.range(1, 3 + 1).mapToObj(i -> "element #" + i)
				.map(name -> new ElementBoundary("myType", name, false, userKey, boundaryLocation, elementProperties))
				.collect(Collectors.toList());

		int i = 1;
		for (ElementBoundary element : newBoundaries) {
			element.setKey(new Key(i++ + "" , "anotherSmartspace"));
		}

		this.restTemplate.postForObject(baseUrl, newBoundaries, ElementBoundary[].class, this.userEntity.getUserSmartspace(), this.userEntity.getUserEmail());

		ElementBoundary[] getResult = this.restTemplate.getForObject(this.baseUrl + "?page={page}&size={size}",
				ElementBoundary[].class, this.userEntity.getUserSmartspace(), this.userEntity.getUserEmail(), 0, 2);

		// THEN the received elements are similar to 2 of the new elements
		assertThat(getResult).hasSize(2).usingElementComparatorOnFields("elementType", "name", "elementProperties")
				.containsAnyElementsOf(newBoundaries);
	}*/


	/*@Test(expected = Exception.class)
	public void testInsertValidElementsAndOneInvalidElement() throws Exception {
		// GIVEN the database is empty

		// WHEN I POST 3 valid elements and one invalid element
		UserForBoundary userKey = new UserForBoundary(this.userEntity.getUserEmail(), this.userEntity.getUserSmartspace());
		LocationForBoundary boundaryLocation = new LocationForBoundary(32.115, 84.817);
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("value", "hello");
		elementProperties.put("value2", 42);
		elementProperties.put("value3", 4.2);

		List<ElementBoundary> newBoundaries = IntStream.range(1, 3 + 1).mapToObj(i -> "element #" + i)
				.map(name -> new ElementBoundary("myType", "demoElement", false, userKey, boundaryLocation,
						elementProperties))
				.collect(Collectors.toList());

		for (ElementBoundary element : newBoundaries) {
			element.setKey(new Key(null, "anotherSmartspace"));
		}

		ElementBoundary invalidElementBoundary = new ElementBoundary("myType", "demoElement", false, userKey,
				boundaryLocation, elementProperties);
		invalidElementBoundary.setKey(new Key(null, "2019b.sarieltutay"));
		newBoundaries.add(invalidElementBoundary);

		this.restTemplate.postForObject(baseUrl, newBoundaries, ElementBoundary[].class);

		// THEN throws exception

	}*/

}
