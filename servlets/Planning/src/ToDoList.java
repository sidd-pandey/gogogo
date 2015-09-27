import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ToDoList {

	private String jName, mName, itemName;
	private String jid;
	private ArrayList<String> memberList;
	private boolean valid = false;
	private Connection conn;
	public ToDoList(Connection conn, String jName, String mName, String itemName){
		this.conn = conn;
		this.jName = jName;
		this.mName = mName;
		this.itemName = itemName;
		System.out.println("itemName is:" + this.itemName);
		memberList = new ArrayList<String>();
	}
	public boolean addToDoList(){
		jid = Topic.findJid(conn, jName, mName);
		valid = fetchMemberList();
		valid = createForMember();
		return valid;
	}
	private boolean fetchMemberList(){
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Name FROM journeydetails WHERE journeyid = '" + jid +"'");
			System.out.println("Member list fetched!");
			while(rs.next()){
				memberList.add(rs.getString(1));
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private boolean createForMember(){
		try {
			for(String mName : memberList){
				Statement s = conn.createStatement();
				int update = s.executeUpdate(querryBuilder(mName));
				if(update > 1){
					System.out.println("To Do List created for" + mName);
				}
			}
			return true;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private String querryBuilder(String mName){
		return "INSERT INTO todolistdetails VALUES('" + jid +"','"+mName+"','"+itemName+"', 0)";
	}
	public static String displayList(Connection conn, String mName, String jName){
		try {
			Statement s = conn.createStatement();
			String jid = Topic.findJid(conn, jName, mName);
			ResultSet rs = s.executeQuery("SELECT * FROM todolistdetails WHERE journeyid = '" + jid+ "' AND Name = '"+mName+"'");
			StringBuilder builder = new StringBuilder("");
			System.out.println("ToDoList Fetched!");
			while(rs.next()){
				//System.out.println(rs.getString(3) + "####" + rs.getString(4));
				builder.append(rs.getString(3) + "####" + rs.getString(4) + "\n");
			}
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String displayListItems(Connection conn, String jName, String mName, String itemName){
		String jid = Topic.findJid(conn, jName, mName);
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM todolistdetails WHERE journeyid = '"+jid+"' AND ItemName='"+itemName+"'");
			StringBuilder builder = new StringBuilder("");
			while(rs.next()){
				if(rs.getString(2).equalsIgnoreCase(mName))
					continue;
				builder.append(rs.getString(2)+"####"+rs.getString(4)+"\n");
			}
			//System.out.println(builder.toString());
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static boolean modifyListItem(Connection conn, String jName, String mName, String itemName, String state){
		String jid = Topic.findJid(conn, jName, mName);
		int integerState = Integer.parseInt(state);
		System.out.println("State is: " + integerState);
		System.out.println("Itemname is: " + itemName);
		try {
			Statement s = conn.createStatement();
			String q = "UPDATE todolistdetails SET Checked = "+integerState+" WHERE journeyid = '"+jid+"' AND Name = '"+mName+"' AND ItemName = '"+itemName+"'";
			//System.out.println(q);
			int update = s.executeUpdate(q);
			//System.out.println("Trying to update the item...");
			if(update > 0){
				System.out.println("Item update successful!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
