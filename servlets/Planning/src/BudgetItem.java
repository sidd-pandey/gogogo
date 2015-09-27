import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BudgetItem {
	private Connection conn;
	private String jName, mName, itemName;
	private double value;
	private boolean valid = false;;
	
	public BudgetItem(Connection conn, String jName, String mName, String itemName, String value){
		this.conn = conn;
		this.jName = jName;
		this.mName = mName;
		this.itemName = itemName;
		System.out.println("Budget item name: " + this.itemName);
		System.out.println("Value of item:" + value);
		this.value = Double.parseDouble(value);
	}
	public boolean addItem(){
		String jid = Topic.findJid(conn, jName, mName);
		try {
			Statement s = conn.createStatement();
			int update = s.executeUpdate("INSERT INTO budgetdetails VALUES ('"+jid+"','"+itemName+"',"+value+")");
			if(update > 0){
				valid = true;
				return valid;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return valid ;
	}
	public static String listItems(Connection conn, String jName, String mName){
		String jid = Topic.findJid(conn, jName, mName);
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM budgetdetails WHERE journeyid = '"+jid+"'");
			StringBuilder builder = new StringBuilder("");
			while(rs.next()){
				builder.append(rs.getString(2)+"####"+rs.getString(3)+"\n");
			}
			Statement s2 = conn.createStatement();
			rs = s2.executeQuery("SELECT sum(value) FROM budgetdetails group by journeyid having journeyid='"+jid+"'");
			String sum = "0";
			while(rs.next()){
				sum = rs.getString(1);
			}
			builder.append(sum);
			System.out.println(builder.toString());
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
