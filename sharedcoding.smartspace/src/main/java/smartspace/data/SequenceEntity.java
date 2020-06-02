package smartspace.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="SEQUENCES")
public class SequenceEntity implements SmartspaceEntity<String> {
	
	@Id
	private String key;
	private Long value;

	public SequenceEntity() {
	}
	
	public SequenceEntity(Long value) {
		this();
		this.value = value;	
	}
	
	public SequenceEntity(String key, Long value) {
		this();
		this.key = key;
		this.value = value;	
	}

	public long getValue() {
		return this.value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "SequenceEntity [key=" + key + ", value=" + value + "]";
	}

}