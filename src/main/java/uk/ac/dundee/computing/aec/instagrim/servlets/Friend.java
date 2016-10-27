package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/addfriend/*",
    "/deletefriend/*",
    "/viewfriend/*",
    "/manage/*"
})
@MultipartConfig

public class Friend extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Friend() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("addfriend", 1);
        CommandsMap.put("deletefriend", 2);
        CommandsMap.put("viewfriend", 3);
        CommandsMap.put("manage", 4);
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) {
            case 1:
                AddFriend(args[2], request, response);
                break;
            case 2:
                DelFriend(args[2], request, response);
                break;
            case 3:
                DisplayList(request, response);
                break;
            case 4:
                Manage(args[2], request, response);
                break;
            default:
                error("Bad Operator", response);
        }
    }

    public void AddFriend(String user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("inside add friend");
        HttpSession sessionhttp = request.getSession();
        LoggedIn lg = (LoggedIn) sessionhttp.getAttribute("LoggedIn");
        String friend = "{\'" + user + "\'}";
        Session session = cluster.connect("instagrim");
        String code = "update userprofiles set friends=friends+" + friend + " where login=?";
        System.out.println(code);
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind(lg.getUsername()));
        session.close();
        lg.addFriend(user);
        sessionhttp.setAttribute("LoggedIn", lg);
        RequestDispatcher rd = request.getRequestDispatcher("/profile/" + user);
        rd.forward(request, response);
    }

    private void DelFriend(String user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("inside del friend");
        HttpSession sessionhttp = request.getSession();
        LoggedIn lg = (LoggedIn) sessionhttp.getAttribute("LoggedIn");
        String friend = "{\'" + user + "\'}";
        Session session = cluster.connect("instagrim");
        String code = "update userprofiles set friends=friends-" + friend + " where login=?";
        System.out.println(code);
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind(lg.getUsername()));
        session.close();
        lg.deleteFriend(user);
        sessionhttp.setAttribute("LoggedIn", lg);
        RequestDispatcher rd = request.getRequestDispatcher("/profile/" + user);
        rd.forward(request, response);

    }

    private void DisplayList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = new java.util.LinkedList<>();

        System.out.println("inside display friend");
        HttpSession sessionhttp = request.getSession();
        LoggedIn lg = (LoggedIn) sessionhttp.getAttribute("LoggedIn");
        Session session = cluster.connect("instagrim");
        String code = "select friends from userprofiles where login=?";
        System.out.println(code);
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs = session.execute( // this is where the query is executed
                boundStatement.bind(lg.getUsername()));
        String tmp = null;
        for (Row row : rs) {
            tmp = row.toString();
        }
        System.out.println(tmp);
        tmp = tmp.substring(5, tmp.length() - 2);
        if (tmp.equals("UL")) {
            RequestDispatcher rd = request.getRequestDispatcher("/Allpics.jsp");
            request.setAttribute("Pics", lsPics);
            rd.forward(request, response);
        }
        String[] parts = tmp.split(",");

        for (String s : parts) {
            System.out.println(s);
            s = s.replace(" ", "");
            lsPics.addAll(tm.getPicsForUser(s));
        }
        RequestDispatcher rd = request.getRequestDispatcher("/Allpics.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }

    private void Manage(String user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessionhttp = request.getSession();
        Session session = cluster.connect("instagrim");
        String code = "select friends from userprofiles where login=?";
        System.out.println(code);
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs = session.execute( // this is where the query is executed
                boundStatement.bind(user));
        String tmp = null;
        for (Row row : rs) {
            tmp = row.toString();
        }
        System.out.println(tmp);
        tmp = tmp.substring(5, tmp.length() - 2);//friends list
        //get info for each friend
        if (tmp.equals("UL")) {
            RequestDispatcher rd = request.getRequestDispatcher("/managefriend.jsp");
            request.setAttribute("friends", "empty");
            rd.forward(request, response);
        } else {
            tmp = tmp.replace(" ", "");
            RequestDispatcher rd = request.getRequestDispatcher("/managefriend.jsp");
            request.setAttribute("friends", tmp);
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("inside friend");
        HttpSession session = request.getSession();
        String friend=request.getParameter("Friend");
    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
