package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.rdb.RdbElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Line;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class RdbElementDaoIntegrationTest {
	
	private RdbElementDao elementDao;

	@Autowired
	public void setElementDao(RdbElementDao elementDao) {
		this.elementDao = elementDao;
	}
	
	@Before
	public void setup() {
		this.elementDao.deleteAll();
	}
	
	@After
	public void teardown() {
		this.elementDao.deleteAll();
	}
	
	@Test
	public void testCreateUpdateAndRead() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN Create in DB new element with name "Test"
		// AND Update element details
		// AND Read element from database
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setElementId("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		Map<String, Object> updateAttributes = new TreeMap<>();
		updateAttributes.put("Shay", "Mas");
		updateAttributes.put("Michael", 26);
		updateAttributes.put("date", new Date());
		
		ElementEntity update = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		update.setKey(newElement.getKey());
		update.setMoreAttributes(updateAttributes);
		
		update.setName("updated test");
		
		this.elementDao.update(update);
		
		Optional<ElementEntity> rv = this.elementDao.readById(newElement.getKey());
		
		ObjectMapper jackson = new ObjectMapper();
		Map<String, Object>jacksonDetail = 
			jackson.readValue(	
				jackson.writeValueAsString(updateAttributes),
				Map.class);
		
		// THEN the element exists
		// AND the element name is "Test"
		// AND the attributes are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "moreAttributes")
			.containsExactly("updated test", jacksonDetail);
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to delete the table
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		elementDao.deleteAll();
		
		List<ElementEntity> rv = this.elementDao.readAll();
		
		// THEN the table is empty
			assertThat(rv)
			.isEmpty();
	}
	
	@Test
	public void testReadAll() throws Exception {
		// GIVEN element table is empty
		
		// WHEN trying to read from the table all the elements
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		List<ElementEntity> rv = this.elementDao.readAll();
		
		// THEN the table is not empty
			assertThat(rv)
			.isNotEmpty();
	}
	
	@Test
	public void testDeleteByKey() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to delete an element by key
		// AND the element is exist
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		elementDao.deleteByKey(newElement.getKey());
		
		List<ElementEntity> rv = this.elementDao.readAll();
					
		// THEN the row is not in the table
		assertThat(rv)
		.isEmpty();
			
	}
	
	@Test
	public void testReadById() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to read a specific element using key
		// AND  the element is exist
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setElementId("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		Optional<ElementEntity> rv = this.elementDao.readById(newElement.getKey());
		
		// THEN the returning element is not empty
		assertThat(rv)
		.isNotEmpty();
			
	}
	
	@Test
	public void testDeleteElement() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to delete an element by key
		// AND  the element is exist
			
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setElementId("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		elementDao.delete(newElement);
		
		List<ElementEntity> rv = this.elementDao.readAll();
					
		// THEN the row is not in the table
		assertThat(rv)
		.isEmpty();
			
	}
	
	@Test
	public void testReadNonexistentElementById() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to read an nonexistent element from the table
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement= this.elementDao.create(newElement);
		String elementKey = newElement.getKey();
		this.elementDao.delete(newElement);
		
		Optional<ElementEntity> rv = this.elementDao.readById(elementKey);
		
		// THEN the returned value will be empty
		assertThat(rv)
		.isEmpty();
	}
	
	@Test
	public void testDeleteNonexistentElement() throws Exception {		
		// GIVEN elements table is empty
		
		// WHEN trying to delete an element from table
		// AND the element does not exist in the table 
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement= this.elementDao.create(newElement);
		ElementEntity newElementTodelete = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElementTodelete.setKey("sarielshaymichael=2");
		newElementTodelete= this.elementDao.create(newElementTodelete);
		
		elementDao.delete(newElementTodelete);
		elementDao.delete(newElementTodelete);
		
		List<ElementEntity> rv = this.elementDao.readAll();
		
		// THEN the table will not change
		assertThat(rv)
		.hasSize(1);
	}
	
	@Test
	public void testWriteAnAlreadyExistElement() throws Exception {
		// GIVEN elements table is empty
		
		// WHEN trying to write an element to the table
		// AND the element already exist 
		
		String email = "shay@gmail.com";
		String smartspace = "shaymas";
		String name = "Shay";
		String type = "Something";
		
		ElementEntity newElement = new ElementEntity(new Line(),name, type, new Date(), false, smartspace, email, null);
		newElement.setKey("sarielshaymichael=1");
		newElement = this.elementDao.create(newElement);
		
		List<ElementEntity> rv = this.elementDao.readAll();
		
		this.elementDao.create(newElement);
		
		List<ElementEntity> rvAfterChange = this.elementDao.readAll();
		
		// THEN the element will not be written to the table
		assertThat(rvAfterChange)
		.hasSameSizeAs(rv);
	}
}
