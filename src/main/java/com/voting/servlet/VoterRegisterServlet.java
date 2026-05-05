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

@WebServlet("/voterRegister")
public class VoterRegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String voterId = request.getParameter("voterId");
        String fullName = request.getParameter("fullName");
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_voting",
                    "root",
                    "admin"
            );

            // ✅ Check voter_id uniqueness ONLY
            String checkSql = "SELECT id FROM voters WHERE voter_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, voterId);

            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                out.println("<h3>Voter ID already exists</h3>");
                return;
            }

            // ✅ Insert voter (username = voterId)
            String sql = "INSERT INTO voters " +
                         "(username, voter_id, full_name, dob, phone, password, has_voted) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 0)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, voterId);   // username
            ps.setString(2, voterId);   // voter_id
            ps.setString(3, fullName);
            ps.setString(4, dob);
            ps.setString(5, phone);
            ps.setString(6, password);

            ps.executeUpdate();
            con.close();

            response.sendRedirect("voter_login.html");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error during registration</h3>");
        }
    }
}
