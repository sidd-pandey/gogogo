import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Scheduler {
	private String jName, mName, desc;
	private Connection conn;
	private String jid = "none";
	
	public Scheduler(Connection conn, String jName, String mName, String desc){
		this.jName = jName;
		this.mName = mName;
		this.desc = desc;
		this.conn = conn;
	}
	
	public boolean addSchedule(){
		jid = Topic.findJid(conn, jName, mName);
		try {
			Statement s = conn.createStatement();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private int getInsertionPos(){
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM scheduledetils WHERE journeyid='"+jid+"' ORDER BY position");
			int position = 0;
			while(rs.next()){
				position++;
			}
			position++;
			return position;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
