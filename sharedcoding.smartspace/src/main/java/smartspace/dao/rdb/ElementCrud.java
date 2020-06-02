package smartspace.dao.rdb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

import org.springframework.data.domain.Pageable;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	public List<ElementEntity> findAllByCreatorLike(
			@Param("pattern") String pattern,
			Pageable pageable);
	
	public List<ElementEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable);
	
//	public List<ElementEntity> findAllByNameAndExpired(
//			@Param("name") String name, @Param("expired") boolean expired,
//			Pageable pageable);
	
//	public List<ElementEntity> findAllByLocation_XBetweenAndLocation_YBetween(
//			@Param("xmin") int xmin, @Param("xmax") int xman, 
//			@Param("ymin") int ymin, @Param("ymax") int yman, 
//			Pageable pageable);

//	public List<ElementEntity> findAllByExpired(@Param("expired") boolean expired, Pageable pageable);
	
//	public List<ElementEntity> findAllByExpiredAndLocation_XBetweenAndLocation_YBetween(@Param("expired") boolean expired,
//			@Param("xmin") int xmin, @Param("xmax") int xman, 
//			@Param("ymin") int ymin, @Param("ymax") int yman, 
//			Pageable pageable);
	
	public List<ElementEntity> findAllByType(@Param("type") String type, Pageable pageable);
	
	public List<ElementEntity> findAllByExpiredAndType(@Param("expired") boolean expired,
			@Param("type") String type, Pageable pageable);

	public Optional<ElementEntity> findByKeyAndExpired(@Param("key") String key, @Param("expired") boolean expired);
	
	

}




