/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
@WebServlet(name = "Login", urlPatterns = {"/Login","/Login/*"})
public class Login extends HttpServlet {

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
        
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        
        String uname = request.getParameter("userName");  
        request.getSession().setAttribute("myusername",uname);
        
        User us=new User();
        us.setCluster(cluster);
        boolean isValid=us.IsValidUser(username, password);
        HttpSession session=request.getSession();
        session.setAttribute("uploaduserprofile", "no");
        System.out.println("Session in servlet "+session);
        if (isValid){
            LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(username);
            
            Session session1 = cluster.connect("instagrim");
            ResultSet rs = null;
            PreparedStatement ps = null;
            ps = session1.prepare("select first_name,last_name,email,picid,friends from userprofiles where login=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session1.execute(
                    boundStatement.bind(username));
            String ret=null;
                for (Row row:rs)
                {
                    ret=row.toString();
                }
            ret=ret.substring(4,ret.length()-1);
            System.out.println(ret);
            String[] tmp=ret.split(",");
            lg.setFiend(ret);
            tmp[2]=tmp[2].substring(2,tmp[2].length()-1);
            boolean flag=false;
            if (" NULL".equals(tmp[3])) {
                flag = false;
            } else { flag = true;}
            lg.sethaveUserPic(flag);
            lg.setFirstname(tmp[0]);
            lg.setLastname(tmp[1]);
            lg.setEmail(tmp[2]);
            session.removeAttribute("loginstate");
            session.setAttribute("LoggedIn", lg);
            System.out.println("Session in servlet "+session);

            RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    rd.forward(request,response);
            
        }else{
            session.setAttribute("loginstate","Invalid username or password");
            response.sendRedirect("/Instagrim/login.jsp");
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //String uri=request.getRequestURI();
        //String[] parts=uri.split("/");
        //System.out.println(parts[1]+parts[3]);
        RequestDispatcher rd=request.getRequestDispatcher("login.jsp");
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
