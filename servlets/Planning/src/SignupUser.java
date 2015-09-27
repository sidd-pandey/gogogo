import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class SignupUser {
	private String username, pwd, email;
	private Connection conn;
	private boolean valid = false;
	
	
	public SignupUser(Connection conn, String username, String pwd, String email){
		this.conn = conn;
		this.username = username;
		this.pwd = pwd;
		this.email = email;
	}
	
	public boolean signup(){
		try {
			Statement stm = conn.createStatement();
			boolean result = stm.execute(querryBuilder());
			if(result){
				valid = true;
				return valid;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	
	private String querryBuilder(){
		return "INSERT INTO userdetails VALUES('" + username +"','" + pwd + "','" + email +"')";
	}
	
}
