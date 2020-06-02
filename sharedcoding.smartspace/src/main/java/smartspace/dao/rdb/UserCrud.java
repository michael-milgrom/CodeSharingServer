package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.UserEntity;

public interface UserCrud extends PagingAndSortingRepository<UserEntity, String> {
	public List<UserEntity> findAllByEmailLike(
			@Param("pattern") String pattern,
			Pageable pageable);
}
