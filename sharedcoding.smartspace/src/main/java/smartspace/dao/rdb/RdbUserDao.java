package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements EnhancedUserDao<String> {

	private UserCrud userCrud;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {
		if (userEntity != null) {
			if (!this.userCrud.existsById(userEntity.getKey())) {
				userEntity.setKey(userEntity.getEmail());
				if (userCrud != null) {
					if (!this.userCrud.existsById(userEntity.getKey())) {
						UserEntity rv = this.userCrud.save(userEntity);
						return rv;
					} else {
						throw new RuntimeException("user already exists with key: " + userEntity.getKey());
					}
				}
			}
			else {
				throw new RuntimeException("user already exists with key: " + userEntity.getKey());
			}
		} else
			throw new RuntimeException("user cant be null");
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> readById(String userKey) {
		return this.userCrud.findById(userKey);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();

		// SQL: SELECT
		this.userCrud.findAll().forEach(rv::add);
		
		return rv;
	}

	@Override
	@Transactional
	public void update(UserEntity user) {
		UserEntity existing = readById(user.getKey()).orElseThrow(() -> new RuntimeException("user couldn't be found"));


		if (user.getProjects() != null) {
			existing.setProjects(user.getProjects());
		}

		if (user.getEmail() != null) {
			existing.setEmail(user.getEmail());
		}

		if (user.getPassword() != null) {
			existing.setPassword(user.getPassword());
		}
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(String sortBy, int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(
						page, size, 
						Direction.ASC, sortBy))
				.getContent();
	}

	@Override
	@Transactional
	public UserEntity createfromImport(UserEntity entity) {
		return this.userCrud.save(entity);
	}
	
}
