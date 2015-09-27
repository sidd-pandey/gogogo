

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ToDoListItemServlet
 */
@WebServlet("/ToDoListItemServlet")
public class ToDoListItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToDoListItemServlet() {
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
		String output = ToDoList.displayListItems(conn, request.getParameter("JName"), request.getParameter("UName"), request.getParameter("todoItem"));
		response.getWriter().print(output);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getConnection();
		boolean result = ToDoList.modifyListItem(conn, request.getParameter("JName"), request.getParameter("UName"), request.getParameter("todoItem"), request.getParameter("state"));
		if(result){
			response.getWriter().print("true");
			return;
		}
		response.getWriter().print("false");
	}
}
