package com.voting.servlet;

import java.io.IOException;
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

@WebServlet("/voterLogin")
public class VoterLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String voterId = request.getParameter("voterId");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/online_voting",
                "root",
                "admin"
            );

            // ✅ ONLY validate voter
            PreparedStatement ps = con.prepareStatement(
                "SELECT has_voted FROM voters WHERE voter_id = ? AND password = ?"
            );
            ps.setString(1, voterId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                response.getWriter().println("<h3>Invalid Voter ID or Password</h3>");
                return;
            }

            if (rs.getInt("has_voted") == 1) {
                response.getWriter().println("<h3>You have already voted</h3>");
                return;
            }

            // ✅ Create session (VERY IMPORTANT)
            HttpSession session = request.getSession();
            session.setAttribute("voterId", voterId);

            con.close();

            // ✅ ONLY redirect to voting page
            response.sendRedirect("vote_page.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Server Error</h3>");
        }
    }
}
