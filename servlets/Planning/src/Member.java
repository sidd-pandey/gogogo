import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.jdbc2.optional.MysqlXid;


public class Member {

	private String aName, pass, jName, mName, jDate, jid;
	private boolean valid = false;
	Connection conn;
	public Member(Connection conn ,String aName, String mName, String pass, String jName){
		this.aName = aName;
		this.pass = pass;
		this.jName = jName;
		this.mName = mName;
		this.conn = conn;

	}
	public boolean addJourney(){
		try {
			if(validJourney()){
				Statement stm = conn.createStatement();
				System.out.println("Executing insert querry:" + querryBuilder());
				int result = stm.executeUpdate(querryBuilder());
				if(result > 0){
					System.out.println("Journey for member added!");
					valid = true;
					return valid;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	private boolean validJourney() throws SQLException{
		Statement stm = conn.createStatement();
		String querry = "SELECT * FROM journeydetails WHERE Name = '" + aName +"' AND JName = '"+jName+"' AND JPass = '" + pass + "'";
		System.out.println("Executing validating querry: " +querry);
		ResultSet rs = stm.executeQuery(querry);
		while(rs.next()){
			System.out.println("Journey validated!!");
			jDate = rs.getString(5);
			jid = rs.getString(6);
			System.out.println("Journey date fetched: " + jDate);
			return true;
		}
		return false;
	}
	private String querryBuilder(){
		return "INSERT INTO journeydetails VALUES ('" + mName +"', 'Member', '" + jName +"','" + pass +"','" + jDate + "','" + jid + "')";	
	}
	public static String getMemberList(Connection conn, String jName, String mName){
		String jid = Topic.findJid(conn, jName, mName);
		StringBuilder builder = new StringBuilder("");
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Name, Role FROM journeydetails WHERE journeyid='"+jid+"'");
			while(rs.next()){
				String name = rs.getString(1);
				String role = rs.getString(1);
				if(role.equalsIgnoreCase("admin")){
					builder.append(name+"(Admin)" +"\n");
				}
				else{
					builder.append(name + "\n");
				}
			}
			System.out.println("MemberList: \n" + builder.toString());
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
