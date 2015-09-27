import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Chat {

	public static boolean addChat(Connection conn, String sender, String jName, String tName, String msg ){
		String jid = Topic.findJid(conn, jName, sender);
		try {
			Statement stm  = conn.createStatement();
			long timeStamp = new Date().getTime();
			System.out.println("Adding msg to database!");
			String querry = "INSERT INTO topicchat VALUES ('" + jid + "','"+tName+"','"+ sender +"'," + timeStamp+", '" + msg + "')";
			int result = stm.executeUpdate(querry);
			if(result > 0){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static String recieve(Connection conn, String sender, String jName, String tName){
		String jid = Topic.findJid(conn, jName, sender);
		try {
			Statement stm = conn.createStatement();
			//System.out.println("Fetching chat details for jid: " + jid + "TName: " + tName);
			ResultSet rs = stm.executeQuery("SELECT * FROM topicchat WHERE journeyid = '" + jid + "' AND TName = '" + tName + "' ORDER BY Time ");
			StringBuilder builder = new StringBuilder("");
			while(rs.next()){
				String s = rs.getString(3);
				String m = rs.getString(5);
				//System.out.println(s + ": " + m);
				builder.append(s + ": " + m + "\n");
			}
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
