import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Journey {

	private String creator, jName,jDate, jPass;
	private String jid;
	private Connection conn;
	private boolean valid = false;
	
	public Journey(Connection conn, String creator, String jPass , String jName, String date){
		this.creator = creator;
		this.jDate = date;
		this.jPass = jPass;
		this.jName = jName;
		this.conn = conn;
	}
	
	public boolean createJourney(){
		try {
			Statement stm = conn.createStatement();
			
			while(addJid() != true);
			System.out.println("Details of journey: " + creator + ", "+jPass +", " +jDate+", " + jName + " jid: " + jid);
			String querry = querryBuilder();
			System.out.println(querry);
			int update = stm.executeUpdate(querry);
			if(update > 0){
				System.out.println("New journey created!");
				valid = true;
				return valid;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
	private boolean addJid() {
		Integer number = (int)(Math.random()*4096);
		jid = number.toString();
		Statement stm;
		int result = -1;
		try {
			stm = conn.createStatement();
			result = stm.executeUpdate("INSERT INTO journeyid VALUES ('" + jid + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if(result > 0){
			return true;
		}
		return false;
	}
	
	
	private String querryBuilder(){
		return "INSERT INTO journeydetails VALUES ('" + creator + "', 'Admin', '" + jName + "', '" + jPass + "', '" + jDate + "','" + jid +"')";
	}
	public  static String displayJourney(String jcreator, Connection conn){
		try {
			StringBuilder  builder = new StringBuilder("");
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT JName FROM journeydetails WHERE Name = '" + jcreator + "'");
			while(rs.next()){
				builder.append(rs.getString(1) + "\n");
			}
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
