/**
 * 
 */
package org.mcplissken.repository;

import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.models.RestModel;
import org.mcplissken.repository.query.SimpleSelectionAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 20, 2014
 */
public interface StructuredRepository {
	
	public List<?> read(RestSearchKey key) throws InvalidCriteriaException;

	public void write(RestModel model);
	
	public void write(List<RestModel> entities);
	
	public void update(RestModel model);
	
	public void delete(RestModel model);

	public SimpleSelectionAdapter createSimpleSelectionAdapter(String modelName);

}
