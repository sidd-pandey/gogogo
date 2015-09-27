

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TopicServlet
 */
@WebServlet("/TopicServlet")
public class TopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getConnection();
		String result = Topic.listTopic(conn, request.getParameter("Creator"), request.getParameter("JName"));
		response.getWriter().print(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getConnection();
		Topic topic  = new Topic(conn, request.getParameter("Creator"), request.getParameter("TName"), request.getParameter("JName"));
		if(topic.addTopic()){
			response.getWriter().print("true");
			return;
		}
		response.getWriter().print("false");
	}
}
