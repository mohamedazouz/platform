/**
 * 
 */
package org.mcplissken.repository.cassandra.criteriahandlers;

import org.mcplissken.repository.key.RestCriteria;

import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 8, 2014
 */
public class AndCriteria extends BasicCriteriaHandler {

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.cassandra.criteriahandlers.BasicCriteriaHandler#resolveCriteria(com.mubasher.market.repository.key.RestCriteria, com.datastax.driver.core.querybuilder.Select.Where)
	 */
	@Override
	protected void resolveCriteria(RestCriteria criteria, Where query)
			throws Exception {
		
		String[] ids = criteria.readCriteriaNames();
		
		for(int i = 0; i < ids.length; i++)
			query = query.and(readClause(ids[i]));
	}

}
