import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Summary {
	
	private Connection conn;
	private String jName, mName, tName, summary;
	
	public Summary(Connection conn, String jName, String mName, String tName, String summary){
		this.conn = conn;
		this.jName = jName;
		this.mName = mName;
		this.tName = tName;
		this.summary = summary;
		System.out.println("Summary is:" + this.summary);
		
	}
	
	public boolean addSummary(){
		try {
			Statement s = conn.createStatement();
			String jid = Topic.findJid(conn, jName, mName);
			int result  = s.executeUpdate("UPDATE topicdetails SET TSummary = '"+summary+"' WHERE journeyid = '"+jid+"' AND TName = '"+tName+"'");
			if(result > 0){
				System.out.println("Sucessfully added summary details!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static String getSummary(Connection conn, String jName, String mName, String tName){
		try {
			Statement s = conn.createStatement();
			String jid = Topic.findJid(conn, jName, mName);
			ResultSet rs = s.executeQuery("SELECT * FROM topicdetails WHERE journeyid='"+jid+"' AND TName='"+tName+"'");
			while(rs.next()){
				String summ = rs.getString(3);
				return summ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
