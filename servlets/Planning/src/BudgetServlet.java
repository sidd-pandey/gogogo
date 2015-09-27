

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BudgetServlet
 */
@WebServlet("/BudgetServlet")
public class BudgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BudgetServlet() {
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
		String out = BudgetItem.listItems(conn, request.getParameter("JName"), request.getParameter("UName"));
		response.getWriter().print(out);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseConnection dc = new DatabaseConnection();
		Connection conn = dc.getConnection();
		BudgetItem item = new BudgetItem(conn, request.getParameter("JName"), request.getParameter("UName"), request.getParameter("budgetItem"), request.getParameter("value"));
		boolean result = item.addItem();
		if(result){
			response.getWriter().print("true");
			return;
		}
		response.getWriter().print("false");
	}

}
