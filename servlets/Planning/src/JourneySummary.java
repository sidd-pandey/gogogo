import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;


public class JourneySummary {

	private Connection conn;
	private String mName, jName;
	private StringBuilder builder;
	public JourneySummary(Connection conn, String mName, String jName){
		this.mName = mName;
		this.jName = jName;
		this.conn = conn;
		builder = new StringBuilder("");
	}
	public String buildSummary(){
		builder.append(getJourneyDetails());
		builder.append("Member List: \n");
		builder.append(getMemberList());
		builder.append("\nDiscussion Details: \n");
		builder.append(getTopicSummary());
		builder.append("\nTotal Expenses: ");
		builder.append("Rs. "+getBudget() +"/-");
		return builder.toString();
	}
	private String getBudget() {
		// TODO Auto-generated method stub
		String jid = Topic.findJid(conn, jName, mName);
		Statement s2;
		StringBuilder builder = new StringBuilder("");
		try {
			s2 = conn.createStatement();
			ResultSet rs = s2.executeQuery("SELECT sum(value) FROM budgetdetails group by journeyid having journeyid='"+jid+"'");
			String sum = "0";
			while(rs.next()){
				sum = rs.getString(1);
			}
			builder.append(sum);
			return builder.toString();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	private String getTopicSummary() {
		// TODO Auto-generated method stub
		try {
			StringBuilder builder = new StringBuilder("");
			String jid = Topic.findJid(conn, jName, mName);
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM topicdetails WHERE journeyid='"+jid+"'");
			while(rs.next()){
				String tName = rs.getString(2);
				String summary = rs.getString(3);
				builder.append("Topic Name: " + tName+"\n");
				builder.append("Summary: \n" + summary + "\n\n");
			}
			return builder.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getMemberList() {
		// TODO Auto-generated method stub
		return Member.getMemberList(conn, jName, mName);
	}
	private String getJourneyDetails() {
		// TODO Auto-generated method stub
		Statement s;
		StringBuilder builder = new StringBuilder("");
		builder.append("Journey Name: "+jName+"\n\n");
		try {
			s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM journeydetails WHERE Name='"+mName+"' AND JName='"+jName+"'");
			while(rs.next()){
				builder.append("Date of Journey: "+ rs.getString(5)+"\n\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Journey Details are: "+builder.toString());
		return builder.toString();
	}
} 
