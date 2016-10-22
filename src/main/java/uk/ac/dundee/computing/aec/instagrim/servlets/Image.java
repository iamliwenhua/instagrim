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
    "/Image",
    "/userpic/*",
    "/likes/*",
    "/unlikes/*",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("userpic", 4);
        CommandsMap.put("likes", 5);
        CommandsMap.put("unlikes", 6);
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
                DisplayImage(Convertors.DISPLAY_PROCESSED, args[2], response, request);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB, args[2], response, request);
                break;
            case 4:
                DisplayUserpic(Convertors.DISPLAY_THUMB, args[2], response, request);
                break;
            case 5:
                SetLikes(args[2], request, response);
                break;
            case 6:
                SetUnlikes(args[2], request, response);
                break;
            default:
                error("Bad Operator", response);
        }
    }

    public void SetLikes(String Image, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String t=request.getRequestURI();
        System.out.println(t);
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        tm.setLikes(java.util.UUID.fromString(Image));
         response.sendRedirect("/Instagrim/Images/allpics");

        
    }
    private void SetUnlikes(String Image, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        tm.setUnlikes(java.util.UUID.fromString(Image));
        response.sendRedirect("/Instagrim/Images/allpics");
    }
    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        if (User.equals("allpics")) {
            System.out.println("In dispaly all pics");
            java.util.LinkedList<Pic> lsPics = tm.getAllpics();
            RequestDispatcher rd = request.getRequestDispatcher("/Allpics.jsp");
            request.setAttribute("Pics", lsPics);
            rd.forward(request, response);
        } else {
            java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
            RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
            request.setAttribute("Pics", lsPics);
            rd.forward(request, response);
        }
    }

    private void DisplayImage(int type, String Image, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if ("userprofilepic".equals(Image)) {
            System.out.println("hello,in dpi");
            session.setAttribute("uploaduserprofile", "yes");
            response.sendRedirect("/Instagrim/upload.jsp");
        } else {

            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            Pic p = tm.getPic(type, java.util.UUID.fromString(Image));

            //load comment
            String comment = tm.getComment(java.util.UUID.fromString(Image));
            session.setAttribute(Image, comment);
            //load info
            String info = tm.getInfo(java.util.UUID.fromString(Image));
            String[] parts = info.split(",");
            session.setAttribute("info" + Image, info);
            session.setAttribute("user" + Image, parts[0]);
            session.setAttribute("times" + Image, parts[1]);
            session.setAttribute("likes" + Image, parts[2]);
            session.setAttribute("unlikes" + Image, parts[3]);
            //set viewtimes
            System.out.println("type" + type);
            if (type != 1) {
                tm.setViewtimes(java.util.UUID.fromString(Image));
            }
            OutputStream out = response.getOutputStream();

            response.setContentType(p.getType());
            response.setContentLength(p.getLength());
            //out.write(Image);
            InputStream is = new ByteArrayInputStream(p.getBytes());
            BufferedInputStream input = new BufferedInputStream(is);
            byte[] buffer = new byte[8192];
            for (int length = 0; (length = input.read(buffer)) > 0;) {
                out.write(buffer, 0, length);
            }
            out.close();
        }
    }

    private void DisplayUserpic(int type, String username, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        System.out.println("in userpic");
        HttpSession session = request.getSession();
        PicModel tm = new PicModel();
        tm.setCluster(cluster);

        Session session1 = cluster.connect("instagrim");
        ResultSet rs = null;
        PreparedStatement ps = null;
        ps = session1.prepare("select picid from userprofiles where login=?");
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session1.execute(boundStatement.bind(username));
        String ret = null;
        for (Row row : rs) {
            ret = row.toString();
        }
        ret = ret.substring(4, ret.length() - 1);
        System.out.println(ret);

        Pic p = tm.getPic(type, java.util.UUID.fromString(ret));
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());

        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        String comment = tm.getComment(java.util.UUID.fromString(ret));
        System.out.println(comment);
        session.setAttribute(ret, comment);
        out.close();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("the comment is adding to0");
        HttpSession sessionhttp = request.getSession();
        String tmpnum = (String) sessionhttp.getAttribute("numofpic");
        int numofpic = Integer.parseInt(tmpnum);

        if (numofpic != 0) {
            //Add comment here
            for (int i = 1; i <= numofpic; i++) {
                String text = "picID" + Integer.toString(i);
                String picid = (String) sessionhttp.getAttribute(text);
                String com = "comment" + Integer.toString(i);
                String comment = (String) request.getParameter(com);
                java.util.UUID picID = java.util.UUID.fromString(picid);

                System.out.println("the comment is adding to" + picid);
                if (comment != null) {
                    PicModel pic = new PicModel();
                    pic.setCluster(cluster);
                    LoggedIn lg = (LoggedIn) sessionhttp.getAttribute("LoggedIn");
                    pic.setComment(comment, picID, lg.getUsername());
                }
            }
            String tmp = (String) sessionhttp.getAttribute("pictype");
            //Display All Pics
            if (tmp.equals("user")) {
                LoggedIn lg = (LoggedIn) sessionhttp.getAttribute("LoggedIn");
                System.out.println(lg.getUsername());
                response.sendRedirect("/Instagrim/Images/" + lg.getUsername());
            } else {
                response.sendRedirect("/Instagrim/Images/allpics");
            }
        } else {
            //Here is the upload code
            for (Part part : request.getParts()) {
                if (!part.getName().equals("selectcondition")) {
                    System.out.println("Part Name " + part.getName());
                    String type = part.getContentType();
                    String filename = part.getSubmittedFileName();
                    InputStream is = request.getPart(part.getName()).getInputStream();
                    int i = is.available();
                    HttpSession session = request.getSession();
                    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                    String username = "majed";
                    if (lg.getlogedin()) {
                        username = lg.getUsername();
                    }
                    if (i > 0) {
                        byte[] b = new byte[i + 1];
                        is.read(b);
                        System.out.println("Length : " + b.length);
                        PicModel tm = new PicModel();
                        tm.setCluster(cluster);
                        String userprofile = (String) sessionhttp.getAttribute("uploaduserprofile");
                        System.out.println(userprofile);
                        String select = (String) request.getParameter("selectcondition");
                        int effecttype = Integer.parseInt(select);
                        if (!userprofile.equals("yes")) //Upload image
                        {
                            System.out.println("hello in upload user pic");
                            tm.insertPic(b, type, filename, username, effecttype);
                            is.close();
                        } else {//Upload userprofile
                            System.out.println("hello in upload userprofile");
                            tm.insertUserprofile(b, type, filename, username, effecttype);
                            is.close();
                            session.setAttribute("uploaduserprofile", "no");
                            lg.sethaveUserPic(true);
                            session.setAttribute("LoggedIn", lg);
                            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                            rd.forward(request, response);
                        }
                    }
                    response.sendRedirect("/Instagrim/index.jsp");
                }
            }
        }
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
