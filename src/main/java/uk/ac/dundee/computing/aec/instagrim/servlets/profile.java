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
    "/profile/*",
})
@MultipartConfig

public class profile extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public profile() {
        super();
        // TODO Auto-generated constructor stub
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
        System.out.println("inside get");
        String args[] = Convertors.SplitRequestPath(request);
        DisplayImageList(args[2], request, response);
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        
        HttpSession session=request.getSession();
        LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(User);
            
            Session session1 = cluster.connect("instagrim");
            ResultSet rs = null;
            PreparedStatement ps = null;
            ps = session1.prepare("select first_name,last_name,email,picid from userprofiles where login=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session1.execute(
                    boundStatement.bind(User));
            String ret=null;
                for (Row row:rs)
                {
                    ret=row.toString();
                }
            ret=ret.substring(4,ret.length()-1);
            System.out.println(ret);
            
            String[] tmp=ret.split(",");
            System.out.println(tmp[0]+tmp[1]+tmp[2]);
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
            session.setAttribute("viewinguser", lg);
        
            java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
            RequestDispatcher rd = request.getRequestDispatcher("/userprofile.jsp");
            request.setAttribute("Pics", lsPics);
            rd.forward(request, response);
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
