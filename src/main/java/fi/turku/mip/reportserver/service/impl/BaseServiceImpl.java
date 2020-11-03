package fi.turku.mip.reportserver.service.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import fi.turku.mip.reportserver.service.QueryService;

/**
 * Base service that contains useful utility methods and related services
 *
 * @author mip
 *
 */
public class BaseServiceImpl {

	@Autowired
	protected QueryService queryService;

	/**
	 * The JDBC template class to use on queries.
	 */
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	protected TransactionTemplate transactionTemplate;

	/**
     * Set the DataSource.
     *
     * @param dataSource the data source to access the database.
     */
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	public String getQuery(String queryName) {
		return queryService.getQuery(queryName);
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

}
