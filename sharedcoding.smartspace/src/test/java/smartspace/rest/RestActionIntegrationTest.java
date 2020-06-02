package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

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

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;
import smartspace.layout.ActionBoundary;
import smartspace.layout.Key;
import smartspace.layout.UserForBoundary;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class RestActionIntegrationTest {
	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private UserEntity userEntity;
	private ElementEntity elementEntity;
	private EnhancedActionDao actionDao;
	private EnhancedUserDao <String> userDao;
	private EnhancedElementDao <String> elementDao;
	
	@Autowired
	public void setActionDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Autowired
	public void setElementDao(EnhancedElementDao <String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao <String> userDao) {
		this.userDao = userDao;
	}
	
	
	@PostConstruct
	public void init() {
		this.userEntity = new UserEntity();
		this.userEntity.setUserEmail("player.invoking.action@de.mo");
		this.userEntity.setUserSmartspace("2019b.sarieltutay");
		this.userEntity.setRole(ActionType.ADMIN);
		this.userDao.create(userEntity);
		
		
		this.elementEntity = new ElementEntity();
		this.elementEntity.setName("elementTest");
		this.elementEntity=this.elementDao.create(elementEntity);
		
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}";
		//this.restTemplate = new RestTemplate();
	}
	
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	
	@After
	public void teardown() {
		this.actionDao.deleteAll();
		this.userDao.deleteAll();
	}	
	
	
	@Test
	public void testGetActionWithPagination() throws Exception{
		
		// GIVEN the database contains 100 actions
		int size = 100;
		IntStream.range(1, size + 1).mapToObj(i->"action" + i).map(ActionEntity::new).forEach(this.actionDao::create);
		
		// WHEN I get action of the page 0 and size 100
		ActionBoundary[] result = 
			this.restTemplate
			.getForObject(this.baseUrl + "?page={page}&size={size}",
					ActionBoundary[].class, this.userEntity.getUserSmartspace(),
					this.userEntity.getUserEmail(),
					0, 20);
		
		// THEN I receive 20 elements
		assertThat(result).hasSize(20);
	}
	
	@Test
	public void testGetActionsWithPaginationOfSecondPage() throws Exception {
		// GIVEN the database contains 100 actions
		int size = 100;
		IntStream.range(1, size + 1).mapToObj(i -> "action #" + i).map(ActionEntity::new)
				.forEach(this.actionDao::create);

		// WHEN I get actions of the page 1 and size 100
		ActionBoundary[] result = this.restTemplate.getForObject(this.baseUrl + "?page={page}&size={size}",
				ActionBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail(), 1, 100);

		// THEN I receive no actions
		assertThat(result).isEmpty();
	}
	
	@Test(expected = Exception.class)
	public void testPostActionWithSameLocalSmartspace() throws Exception {
		// GIVEN the database is empty

		// WHEN I POST action with the same smartspace as the project
		UserForBoundary user = new UserForBoundary(this.userEntity.getUserEmail(), this.userEntity.getUserSmartspace());
		Key Key = new Key(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace());
		Map<String, Object> details = new HashMap<>();
		details.put("val1", "aa");

		ActionBoundary actionBoundary = new ActionBoundary("type", Key, user, details);
		actionBoundary.setActionKey(new Key("3", "2019b.sarieltutay"));
		ActionBoundary[] actionBoundaries = {actionBoundary};

		this.restTemplate.postForObject(baseUrl, actionBoundaries, ActionBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail());
		// THEN throws exception
	}
	
	
	@Test(expected = Exception.class)
	public void testPostActionWithoutElement() throws Exception {
		// GIVEN the database is empty

		// WHEN I POST action that element not imported in advanced
		UserForBoundary user = new UserForBoundary(this.userEntity.getUserEmail(), this.userEntity.getUserSmartspace());
		Map<String, Object> details = new HashMap<>();
		details.put("val1", 3);
		details.put("val2", "3");

		ActionBoundary actionBoundary = new ActionBoundary("myType", null, user, details);
		actionBoundary.setActionKey(new Key("1", "2019b.sarieltutay"));
		ActionBoundary[] actionBoundaries = {actionBoundary};

		this.restTemplate.postForObject(baseUrl, actionBoundaries, ActionBoundary[].class);
		// THEN throws exception
	}
	
	
}
