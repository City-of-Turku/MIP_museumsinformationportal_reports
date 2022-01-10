# ReportServer

ReportServer is a server application that creates JasperReports from API requests for MIP.

Develop and Master branches are Spring v4, develop_v5 branch uses Spring v5

## Installation

**Prerequisites**
- Java JDK (e.g. openjdk-11)
- Postgresql (recommended)
- MIP db set up
- Maven
- Tomcat

After you have pulled the code to your own computer, run (in reportserver folder)

1. Install dependencies: maven install
2. Rename the created .war to reportserver.war
3. Create a dedicated folder for the report output files and the report source files:
    1. mkdir mip
    2. mkdir mip/reportsources
    3. mkdir mip/reportoutput
4. Create the database for the application
5. Configure datasources
6. Deploy the war to the tomcat (NOTE: The scripts will create the needed tables and insert default data.)

NOTE: The reportserver application should only be accessible internally as no user authentication is implemented. All report requests are processed.

---

## Environment settings

* Datasources configuration in server.xml for Tomcat:

        <Context docBase="ReportServer.war" path="/reportserver" reloadable="false">
            <!-- env variable that defines the base path for all report definitions (sources) -->
            <Environment name="reportserver.reportsource.base.path" type="java.lang.String" value="<path_to_the_reportsource_folder/>"/>

            <!-- env variable that defines the output path of created reports -->
            <Environment name="reportserver.report.output.path" type="java.lang.String" value="<path_to_the_reportoutput_folder/>"/>

            <Resource name="jdbc/reportDS" auth="Container" type="javax.sql.DataSource"
                username="<report_database_report_user_name>" password="<report_database_report_user_password>"
                url="<connection_string_to_the_report_database>"
                driverClassName="org.postgresql.Driver"
                validationQuery="select 1"
                poolPreparedStatements="true"/>

            <Resource auth="Container" driverClassName="org.postgresql.Driver"
                maxTotal="10" maxIdle="4" minIdle="2"
                name="jdbc/reportSourceDS"
                username="<mip_database_read_user_name>"
                password="<mip_database_read_user_password>"
                type="javax.sql.DataSource"
                url="<connection_string_to_the_mip_database>"
                testOnBorrow="true"
                validationQuery="select 1"/>
        </Context>

* Database table global_parameters can be used to store _global_ _parameters_ that are common for different reports.


## Adding report sources

1. Move your compiled jasper report template (.jasper-file) to the /mip/reportsources/example/ -folder
2. Add the file to the reportserver database:

       insert into report (name, description, reportsource_path, reportsource_file) values ('raportin_nimi', 'raportin_kuvaus', 'example/', 'myreport.jasper');

