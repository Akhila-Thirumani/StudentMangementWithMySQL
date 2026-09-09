package config;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;


public class DatabaseConfig {
	
    private static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/student_management_db";

    private static final String DATABASE_USERNAME = "root";

    private static final String DATABASE_PASSWORD = "@akhila@456*";
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();
	
    
    static {
	    DATA_SOURCE.setUrl(DATABASE_URL);
	    DATA_SOURCE.setUsername(DATABASE_USERNAME);
	    DATA_SOURCE.setPassword(DATABASE_PASSWORD);

	    DATA_SOURCE.setInitialSize(5);
	    DATA_SOURCE.setMaxTotal(10);
	}
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}