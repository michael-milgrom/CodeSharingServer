package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.rdb.RdbActionDao;
import smartspace.data.ActionEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class RdbActionDaoIntegrationTest {
	
	private RdbActionDao actionDao;

	@Autowired
	public void setActionDao(RdbActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Before // TODO harms the performance
	public void setup() {
		this.actionDao.deleteAll();
	}
	
	@After
	public void teardown() {
		this.actionDao.deleteAll();
	}
		
	@Test
	public void testReadAll() throws Exception {
		// GIVEN actions table is empty
		
		// WHEN trying to read from the table all the actions
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String elementId = "test";
		String type = "Something";
		
		ActionEntity newAction = new ActionEntity(smartspace, elementId, smartspace, email, type, new Date(), null);
		newAction.setActionId("Test");
		newAction = this.actionDao.create(newAction);
		
		List<ActionEntity> rv = this.actionDao.readAll();
		
		// THEN the table is not empty
			assertThat(rv)
			.isNotEmpty();
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		// GIVEN actions table is empty
		
		// WHEN trying to delete the table
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String elementId = "test";
		String type = "Something";
		
		ActionEntity newAction = new ActionEntity(smartspace, elementId, smartspace, email, type, new Date(), null);
		newAction.setActionId("Test");
		newAction = this.actionDao.create(newAction);
		
		actionDao.deleteAll();						
		List<ActionEntity> rv = this.actionDao.readAll();
		
		// THEN the table is empty
			assertThat(rv)
			.isEmpty();
	}
	
	@Test
	public void testWriteAnAlreadyExistAction() throws Exception {
		// GIVEN actions table is empty
		
		// WHEN trying to write an action to the table
		// AND the action already exist 
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String elementId = "test";
		String type = "Something";
		
		ActionEntity newAction = new ActionEntity(smartspace, elementId, smartspace, email, type, new Date(), null);
		newAction.setKey("sarielshaymichael=1");
		newAction = this.actionDao.create(newAction);
		
		List<ActionEntity> rv = this.actionDao.readAll();
		
		this.actionDao.create(newAction);
		
		List<ActionEntity> rvAfterChange = this.actionDao.readAll();
		
		// THEN the action will not be written to the table
		assertThat(rvAfterChange)
		.hasSameSizeAs(rv);
	}
	
}
