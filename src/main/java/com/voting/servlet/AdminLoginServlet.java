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

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_voting",
                    "root",
                    "admin"
            );

            String sql = "SELECT username FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // ✅ Admin login success
                HttpSession session = request.getSession();
                session.setAttribute("adminUser", username);

                con.close();
                response.sendRedirect("adminPanel");
            } else {
                response.getWriter().println("<h3>Invalid Admin Username or Password</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Server Error</h3>");
        }
    }
}
