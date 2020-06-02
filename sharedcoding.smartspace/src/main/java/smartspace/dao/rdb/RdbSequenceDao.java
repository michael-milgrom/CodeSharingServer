package smartspace.dao.rdb;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import smartspace.dao.SequenceDao;
import smartspace.data.SequenceEntity;

@Repository
public class RdbSequenceDao implements SequenceDao {

	private SequenceCrud sequenceCrud;

	@Autowired
	public RdbSequenceDao(SequenceCrud sequenceCrud) {
		super();
		this.sequenceCrud = sequenceCrud;
	}
	
	@Override
	public Optional<SequenceEntity> readById(String key) {
		return this.sequenceCrud.findById(key);
	}

	@Override
	public Long newEntity(String sequence) {
		Optional<SequenceEntity> seq = this.readById(sequence);
		Long id = (long) 0;
		if(seq.isPresent()) {
			seq.get().setValue(seq.get().getValue()+1);
			id = seq.get().getValue();
			sequenceCrud.save(seq.get());
		}
		else
			sequenceCrud.save(new SequenceEntity(sequence, id));
		return id;
	}

}
