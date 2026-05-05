package com.voting.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/adminPanel")
public class AdminPanelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔒 Admin session check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.getWriter().println("<h3>Access Denied. Admin only.</h3>");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_voting",
                    "root",
                    "admin"
            );

            /* ================== VOTE COUNT ================== */
            int voteA = 0, voteB = 0, voteC = 0, voteD = 0;

            PreparedStatement countPs = con.prepareStatement(
                "SELECT voted_for, COUNT(*) FROM votes GROUP BY voted_for"
            );
            ResultSet rsCount = countPs.executeQuery();

            while (rsCount.next()) {
                String candidate = rsCount.getString("voted_for");
                int count = rsCount.getInt(2);

                if ("Candidate-A".equals(candidate)) voteA = count;
                else if ("Candidate-B".equals(candidate)) voteB = count;
                else if ("Candidate-C".equals(candidate)) voteC = count;
                else if ("Candidate-D".equals(candidate)) voteD = count;
            }

            /* ================== PAGE ================== */
            out.println("<!DOCTYPE html><html><head><title>Admin Panel</title>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
            out.println("</head><body>");

            /* ================== COUNT TABLE ================== */
            out.println("<h2>Candidate Wise Vote Count</h2>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>Candidate</th><th>Party</th><th>Symbol</th><th>Votes</th></tr>");

            out.println("<tr><td>Candidate-A</td><td>BJP</td><td><img src='images/bjp.png' width='40'></td><td>" + voteA + "</td></tr>");
            out.println("<tr><td>Candidate-B</td><td>INC</td><td><img src='images/inc.png' width='40'></td><td>" + voteB + "</td></tr>");
            out.println("<tr><td>Candidate-C</td><td>AAP</td><td><img src='images/aap.png' width='40'></td><td>" + voteC + "</td></tr>");
            out.println("<tr><td>Candidate-D</td><td>BSP</td><td><img src='images/bsp.png' width='40'></td><td>" + voteD + "</td></tr>");

            out.println("</table>");

            /* ================== BAR CHART ================== */
            out.println("<h2>Vote Distribution</h2>");
            out.println("<canvas id='voteChart' width='600' height='300'></canvas>");

            out.println("<script>");
            out.println("new Chart(document.getElementById('voteChart'), {");
            out.println("type:'bar',");
            out.println("data:{");
            out.println("labels:['Candidate-A','Candidate-B','Candidate-C','Candidate-D'],");
            out.println("datasets:[{");
            out.println("label:'Votes',");
            out.println("data:[" + voteA + "," + voteB + "," + voteC + "," + voteD + "],");
            out.println("backgroundColor:['#ff9933','#2ecc71','#3498db','#9b59b6']");
            out.println("}]");
            out.println("}");
            out.println("});");
            out.println("</script>");

            /* ================== ONLY VOTED USERS ================== */
            out.println("<h2>List of Voted Users</h2>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>S.No</th><th>Voter ID</th><th>Full Name</th><th>Voted For</th></tr>");

            PreparedStatement voterPs = con.prepareStatement(
                "SELECT v.voter_id, vt.full_name, v.voted_for " +
                "FROM votes v INNER JOIN voters vt ON v.voter_id = vt.voter_id"
            );
            ResultSet rs = voterPs.executeQuery();

            int i = 1;
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + i++ + "</td>");
                out.println("<td>" + rs.getString("voter_id") + "</td>");
                out.println("<td>" + rs.getString("full_name") + "</td>");
                out.println("<td>" + rs.getString("voted_for") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            /* ================== LOGOUT ================== */
            out.println("<div style='margin:40px;text-align:center;'>");
            out.println("<a href='adminLogout'><button style='padding:12px 30px;'>Logout</button></a>");
            out.println("</div>");

            out.println("</body></html>");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Server Error</h3>");
        }
    }
}
