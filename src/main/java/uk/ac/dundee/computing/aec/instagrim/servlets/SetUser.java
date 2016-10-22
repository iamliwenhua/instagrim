/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SetUser", urlPatterns = {"/SetUser","/SetUser/*"})
public class SetUser extends HttpServlet {

    Cluster cluster=null;


    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session1 = request.getSession();
        String email=request.getParameter("email");
        String address=request.getParameter("address");
        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        String username = (String)session1.getAttribute("username");
        
        LoggedIn lg=new LoggedIn();
        lg.setLogedin();
        lg.setUsername(username);
        lg.setFirstname(firstname);
        lg.setLastname(lastname);
        lg.setEmail(email);
        session1.setAttribute("LoggedIn",lg);
        
        Session session = cluster.connect("instagrim");
        String code="update userprofiles set first_name=?,last_name=? where login=?";
        PreparedStatement ps = session.prepare(code);
        System.out.println(username);
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        firstname,lastname,username));
        code="update userprofiles set email={\'"+email+"\'} where login=?";
        ps = session.prepare(code);
        System.out.println(username);
        boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	rd.forward(request,response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
