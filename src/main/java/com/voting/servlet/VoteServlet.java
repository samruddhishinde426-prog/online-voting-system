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

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        // ✅ Check login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("voterId") == null) {
            response.getWriter().println("<h3>Please login first</h3>");
            return;
        }

        String voterId = (String) session.getAttribute("voterId");
        String candidate = request.getParameter("candidate");

        if (candidate == null || candidate.trim().isEmpty()) {
            response.getWriter().println("<h3>Please select a candidate</h3>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_voting",
                    "root",
                    "admin"
            );

            // ✅ Check has_voted
            PreparedStatement checkPs =
                    con.prepareStatement("SELECT has_voted FROM voters WHERE voter_id = ?");
            checkPs.setString(1, voterId);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                response.getWriter().println("<h3>Invalid voter</h3>");
                return;
            }

            if (rs.getInt("has_voted") == 1) {
                response.getWriter().println("<h3>You have already voted</h3>");
                return;
            }

            // ✅ Insert vote
            PreparedStatement insertPs =
                    con.prepareStatement(
                            "INSERT INTO votes (voter_id, voted_for) VALUES (?, ?)");
            insertPs.setString(1, voterId);
            insertPs.setString(2, candidate);
            insertPs.executeUpdate();

            // ✅ Update voter status
            PreparedStatement updatePs =
                    con.prepareStatement(
                            "UPDATE voters SET has_voted = 1 WHERE voter_id = ?");
            updatePs.setString(1, voterId);
            updatePs.executeUpdate();

            con.close();

            response.sendRedirect("thankyou.jsp");


        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Server Error</h3>");
        }
    }
}
