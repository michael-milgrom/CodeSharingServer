package smartspace.dao.rdb;

import org.springframework.data.repository.PagingAndSortingRepository;

import smartspace.data.SequenceEntity;

public interface SequenceCrud extends PagingAndSortingRepository<SequenceEntity, String> {

}
