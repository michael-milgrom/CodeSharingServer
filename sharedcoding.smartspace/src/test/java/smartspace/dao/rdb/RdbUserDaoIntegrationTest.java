package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class RdbUserDaoIntegrationTest {
	
	private RdbUserDao userDao;

	@Autowired
	public void setUserDao(RdbUserDao userDao) {
		this.userDao = userDao;
	}
	
	@Before
	public void setup() {
		this.userDao.deleteAll();
	}
	
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}
	
	@Test
	public void testCreateUpdateAndRead() throws Exception {
		// GIVEN users table is empty
		
		// WHEN Create in DB new user with name "Test"
		// AND Update user details
		// AND Read user from database
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
		newUser = this.userDao.create(newUser);
		
		UserEntity update = new UserEntity();
		update.setKey(newUser.getKey());
		update.setUsername("Test User Name");
		
		this.userDao.update(update);
		
		Optional<UserEntity> rv = this.userDao.readById(newUser.getKey());
		
		
		// THEN the user exists
		// AND the user name is "Test"
		// AND the user name is updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("username")
			.containsExactly("Test User Name");
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		// GIVEN users table is empty
		
		// WHEN trying to delete the table
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
		
		newUser = this.userDao.create(newUser);
		
		userDao.deleteAll();
		
		Optional<UserEntity> rv = this.userDao.readById(newUser.getKey());
		
		
		// THEN the table is empty
			assertThat(rv)
			.isEmpty();
	}
	
	@Test
	public void testReadAll() throws Exception {
		// GIVEN user table is empty
		
		// WHEN trying to read from the table all the user
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
		
		newUser= this.userDao.create(newUser);
		
		List<UserEntity> rv = this.userDao.readAll();
		
		// THEN the table is not empty
			assertThat(rv)
			.isNotEmpty();
	}
	
	@Test
	public void testReadById() throws Exception {
		// GIVEN user table is empty
		
		// WHEN trying to read a specific user using key
		// AND  the user is exist
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
	
		newUser = this.userDao.create(newUser);
		
		Optional<UserEntity> rv = this.userDao.readById(newUser.getKey());
		
		// THEN the returning user is not empty
		assertThat(rv)
		.isNotEmpty();
	}
	
	@Test
	public void testReadNonexistentUserById() throws Exception {
		// GIVEN users table is empty
		
		// WHEN trying to read an nonexistent user from the table
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
		newUser = this.userDao.create(newUser);
		String userKey = newUser.getKey();
		userDao.deleteAll();
		
		Optional<UserEntity> rv = this.userDao.readById(userKey);
		
		// THEN the returned value will be empty
		assertThat(rv)
		.isEmpty();
	}
	
	
	@Test
	public void testWriteAnAlreadyExistUser() throws Exception {
		// GIVEN users table is empty
		
		// WHEN trying to write an user to the table
		// AND the user already exist 
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String username = "shay";
		String avatar = "avatar";
		
		UserEntity newUser = new UserEntity(smartspace, email, username, avatar, ActionType.PLAYER, 0);
		
		newUser = this.userDao.create(newUser);
		
		List<UserEntity> rv = this.userDao.readAll();
		
		this.userDao.create(newUser);
		
		List<UserEntity> rvAfterChange = this.userDao.readAll();
		
		// THEN the user will not be written to the table
		assertThat(rvAfterChange)
		.hasSameSizeAs(rv);
	}
	
	
	
}
