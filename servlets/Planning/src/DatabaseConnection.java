import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
	
	private static String vendorName;
	private static Connection conn;
	private static String dbName, username, pwd;
	
	static{
		vendorName = "jdbc:mysql://localhost:3306/";
		dbName = "planning";
		username = "root";
		pwd = "123456";
	}
	
	public synchronized Connection getConnection(){
		if(conn == null){
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection(vendorName + dbName, username, pwd);
				System.out.println("Connected to database!");
				return conn;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}
}
