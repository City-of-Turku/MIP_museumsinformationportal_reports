package fi.turku.mip.reportserver.config;

import java.util.concurrent.RejectedExecutionHandler;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fi.turku.mip.reportserver.core.ReportTaskRejectedHandler;
import fi.turku.mip.reportserver.util.JndiEnvironmentVariableLookup;
import liquibase.integration.spring.SpringLiquibase;

/**
 * Application configuration.
 *
 * @author mip
 *
 */
@Configuration
@ComponentScan("fi.turku.mip.reportserver")
@EnableWebMvc
@EnableScheduling
@ImportResource({"classpath:queries.xml"})
public class AppConfig {

	@Bean
    public DataSource dataSource() {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        return dsLookup.getDataSource("jdbc/reportDS");
    }

	@Bean
    public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dstm = new DataSourceTransactionManager();
		dstm.setDataSource(dataSource());
        return dstm;
	}

	@Bean
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager());
        return transactionTemplate;
	}

	@Bean
	public SpringLiquibase liquibase() {
	    SpringLiquibase liquibase = new SpringLiquibase();
	    liquibase.setChangeLog("classpath:database/master.xml");
	    liquibase.setDataSource(dataSource());
	    return liquibase;
	}

	@Bean RejectedExecutionHandler rejectedExecutionHandler() {
		return new ReportTaskRejectedHandler();
	}

	@Bean
	public ThreadPoolTaskExecutor reportTaskExecutor() {

		// todo parameterize from some properties file(?)
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setMaxPoolSize(10);
		tpte.setCorePoolSize(10);
		tpte.setQueueCapacity(10);
		tpte.setWaitForTasksToCompleteOnShutdown(true);
		tpte.setThreadNamePrefix("ReportTaskExecutor-");
		tpte.setRejectedExecutionHandler(rejectedExecutionHandler());
		return tpte;
	}

	@Bean
	public ReportServerConfiguration serverConfiguration() {
		ReportServerConfiguration rsc = new ReportServerConfiguration();

        final JndiEnvironmentVariableLookup envLookup = new JndiEnvironmentVariableLookup();

        String srcPath = envLookup.getEnvironmentVariable("reportserver.reportsource.base.path");
        if(srcPath==null || srcPath.isEmpty()) {
        	throw new IllegalArgumentException("JNDI configuration parameter 'reportserver.reportsource.base.path' is not set!");
        }
        rsc.setReportSourceBasePath(srcPath);

        String dstPath = envLookup.getEnvironmentVariable("reportserver.report.output.path");
        if(dstPath==null || dstPath.isEmpty()) {
        	throw new IllegalArgumentException("JNDI configuration parameter 'reportserver.report.output.path' is not set!");
        }
        rsc.setReportOutputPath(dstPath);

		return rsc;
	}

}