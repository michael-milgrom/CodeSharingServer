package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
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

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserForBoundary;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RestUserIntegrationTest {
	
	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private UserEntity userEntity;
	private EnhancedUserDao<String> userDao;

	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	@PostConstruct
	public void init() {
		this.userEntity = new UserEntity();
		this.userEntity.setUserEmail("manager.creating.element@de.mo");
		this.userEntity.setUserSmartspace("2019b.sarieltutay");
		this.userEntity.setRole(ActionType.ADMIN);
		this.userDao.create(userEntity);
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/{adminSmartspace}/{adminEmail}";
		//this.restTemplate = new RestTemplate();
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}


	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test(expected = Exception.class)
	public void testPostWithoutPermissions() throws Exception {
		// GIVEN the database contains user with player role
		this.userEntity = new UserEntity();
		this.userEntity.setUserEmail("email@gmail.com");
		this.userEntity.setUserSmartspace("2019b.sarieltutay");
		this.userEntity.setRole(ActionType.PLAYER);
		this.userDao.create(userEntity);

		// WHEN POST a new UserBoundary
		UserBoundary newUser = new UserBoundary();
		newUser.setAvatar(":)");
		newUser.setPoints(21);
		newUser.setRole(ActionType.PLAYER.name());
		newUser.setUsername("name");

		UserBoundary[] userBoundariesArr = { newUser };
		restTemplate.postForObject(baseUrl, userBoundariesArr, UserBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail());

		// THEN throws Exception

	}


	@Test
	public void testGetUsersWithPagination() throws Exception {
		// Given the database contains 200 users
		int size = 100;

		IntStream.range(1, size + 1).mapToObj(i -> "a@gmail.com" + i).map(UserEntity::new)
				.forEach(this.userDao::create);

		// When I get users of the page  0 and size 30
		UserBoundary[] result = restTemplate.getForObject(baseUrl + "?page={page}&size={size}", UserBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail(), 0,
				30);

		// THEN I receive 30 users
		assertThat(result).hasSize(30);
	}


	@Test
	public void testGetUsersWithPaginationSecondPageThatHaveData() throws Exception {
		// Given the database contains 40 users
		int size = 39;

		IntStream.range(1, size + 1).mapToObj(i -> "a@gmail.com" + i).map(UserEntity::new)
				.forEach(this.userDao::create);

		// When I get users of the page 1 and size 30
		UserBoundary[] result = restTemplate.getForObject(baseUrl + "?page={page}&size={size}", UserBoundary[].class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail(), 1,
				30);

		// THEN I receive 10 users
		assertThat(result).hasSize(10);
	}



	// Test posting a user with the same smartspace as us
	@Test(expected = Exception.class)
	public void testPostUserWithSameLocalSmartspace() throws Exception {
		// GIVEN the database containsadmin 

		// WHEN I POST user with local smartsapce

		UserBoundary userBoundary = new UserBoundary("name", "avatar", "player", 3);
		userBoundary.setKey(new UserForBoundary("a@gmail.com", "2019b.sarieltutay"));

		restTemplate.postForObject(baseUrl, userBoundary, UserBoundary.class, this.userEntity.getUserSmartspace(),
				this.userEntity.getUserEmail());

		// THEN the I get exception

	}

	// Test - Post 3 valid users and 1 invalid user
	@Test(expected = Exception.class)
	public void testPostValidAndInValidUsers() throws Exception {
		// GIVEN the database only contains an admin that belong to our smartspace

		// WHEN I post 3 valid users and 1 invalid user

		int size = 3;
		List<UserBoundary> newBoundaries = IntStream.range(1, size + 1).mapToObj(i -> i)
				.map(j -> new UserBoundary("name" + j, "avatar" + j, "PLAYER", 5)).collect(Collectors.toList());

		for (int i = 0; i < newBoundaries.size(); i++)
			newBoundaries.get(i).setKey(new UserForBoundary("email@" + i, "2019b.nsarieltutay"));

		UserBoundary invalidUserBoundary = new UserBoundary("name10", "avatar10", "PLAYER", 5);
		invalidUserBoundary.setKey(new UserForBoundary("@email", "2019b.sarieltutay"));

		newBoundaries.add(invalidUserBoundary);

		restTemplate.postForObject(baseUrl, newBoundaries, UserBoundary[].class);

		// THEN i get exception and transaction is canceled

	}
}