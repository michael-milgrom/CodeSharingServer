package smartspace.dao;

import java.util.Optional;

import smartspace.data.SequenceEntity;

public interface SequenceDao {

	public Optional<SequenceEntity> readById(String key);
	public Long newEntity(String sequence);
	
}
