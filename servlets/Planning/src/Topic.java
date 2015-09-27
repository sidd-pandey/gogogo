import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Topic {

	private String mName, tName, jName, jid;
	private Connection conn;
	private boolean valid = false;
	
	public Topic(Connection conn, String mName, String tName, String jName){
		this.conn = conn;
		this.tName = tName;
		this.jName = jName;
		this.mName = mName;
	}
	
	public boolean addTopic(){
		try {
			Statement stm = conn.createStatement();
			jid = findJid(conn, jName, mName);
			String querry = querryBuilder();
			int result = stm.executeUpdate(querry);
			if(result > 0){
				System.out.println("New topic added!");
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
		return "INSERT INTO topicdetails VALUES('" + jid +"','" + tName +"','Add your summary!')" ;
	}
	public static String listTopic(Connection conn, String mName, String jName){
		try {
			Statement stm = conn.createStatement();
			//System.out.println("MName: " + mName + "JName: " + jName);
			String jid = findJid(conn, jName, mName);
			System.out.println("jid for listing topic: " + jid);
			StringBuilder builder = new StringBuilder("");
			ResultSet rs = stm.executeQuery("SELECT * FROM topicdetails WHERE journeyid ='" + jid +"'");
			System.out.println("Listing topics...");
			while(rs.next()){
				System.out.println("Topic name: " + rs.getString(2));
				builder.append(rs.getString(2) + "\n");
			}
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String findJid(Connection conn, String jName, String mName){
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT journeyid FROM journeydetails WHERE Name = '" + mName + "' AND JName = '" + jName +"'");
			while(rs.next()){
				String jid = rs.getString(1);
				System.out.println("Jid found for new topic: " + jid);
				return jid;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
