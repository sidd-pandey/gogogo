import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginUser {
	
	private String username, pwd;
	private Connection conn;
	private boolean valid = false;
	
	
	public LoginUser(Connection conn, String username, String pwd){
		this.conn = conn;
		this.username = username;
		this.pwd = pwd;
	}
	
	public boolean validateUser(){
		try {
			Statement stm = conn.createStatement();
			String querry = querryBuilder();
			ResultSet rs =  stm.executeQuery(querry);
			while(rs.next()){
				System.out.println("Result set found!");
				if(rs.getString(1).equals(username) && rs.getString(2).equals(pwd)){
					System.out.println("Verified!");
					valid = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return valid;
	}

	private String querryBuilder(){
		return  "SELECT * FROM userdetails WHERE USERNAME = '" + username + "' AND PASSWORD = '" + pwd +"'";
	}
}
