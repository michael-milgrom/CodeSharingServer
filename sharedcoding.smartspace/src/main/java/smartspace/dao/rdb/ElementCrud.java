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

	
	

}




