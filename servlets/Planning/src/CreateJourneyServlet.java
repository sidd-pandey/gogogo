

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateJourneyServlet
 */
@WebServlet("/CreateJourneyServlet")
public class CreateJourneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateJourneyServlet() {
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
		String str = Journey.displayJourney(request.getParameter("creator"), conn);
		response.getWriter().write(str);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getConnection();
		Journey journey = new Journey(conn, request.getParameter("Creator"), request.getParameter("JPass"),request.getParameter("JName"), request.getParameter("DOJ"));
		if(journey.createJourney()){
			response.getWriter().println("true");
			return;
		}
		response.getWriter().println("false");
	}
}
