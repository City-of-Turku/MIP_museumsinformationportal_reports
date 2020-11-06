package fi.turku.mip.reportserver.service;

import org.springframework.stereotype.Service;

/**
 * QueryService provides named database queries.
 *
 * @author mip
 *
 */
@Service
public interface QueryService {

	/**
	 * Get the SQL for named query.
	 *
	 * @param queryName the name
	 * @return the query for given name if found
	 * @throws IllegalArgumentException if the query for given name is not found
	 */
	public String getQuery(String queryName) throws IllegalArgumentException;
}
