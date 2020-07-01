package smartspace.dao.rdb;


import java.util.List;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ActionEntity;

import org.springframework.data.domain.Pageable;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String>  {
	public List<ActionEntity> findAllByElementKey(
			@Param("elementKey") String elementKey,
			Pageable pageable);
	
}

