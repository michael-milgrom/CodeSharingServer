package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
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

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.Key;
import smartspace.layout.UserForBoundary;;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})


public class ActionControllerIntegrationTest {
/*	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;

	
	@Value("${smartspace.name}")
	private String appSmartspace;
	private String adminEmail = "admin@admin";
	
	
	@Autowired
	public void setActionDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions";
	}
	
	@Before
	public void initAdmin() {
		this.userDao
			.create(new UserEntity("appSmartSpace",adminEmail,"admin","adminAvatar",ActionType.ADMIN,0));
	}
	
	@After
	public void tearDown() {
		this.actionDao
			.deleteAll();
	}

	@Test
	public void postNewAction() throws Exception{
		
		//GIVEN the action data base is empty 
		//AND user data base has 1 use which is admin
		
		//WHEN admin POST new action
		ActionBoundary newAction = new ActionBoundary();
		
		Map<String,Object> actionProperties = new HashMap<String, Object>();
		actionProperties.put("x", 10);
		actionProperties.put("isTired", true);
		
		newAction.setCreated(new Date());
		newAction.setElement(new Key("test",appSmartspace));
		newAction.setPlayer(new UserForBoundary("test@test",appSmartspace));
		newAction.setProperties(actionProperties);
		newAction.setType("test");
		
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				newAction, 
				ElementBoundary.class, 
				appSmartspace,adminEmail);		
		
		//THEN the database contains a single action
		assertThat(this.actionDao
				.readAll())
				.hasSize(1);
	}*/	
}

