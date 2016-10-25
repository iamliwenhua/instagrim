<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <SCRIPT   LANGUAGE="JavaScript">
        function   fresh()
        {
            if (location.href.indexOf("?reload=true") < 0)
            {
                location.href += "?reload=true";
            }
        }
        setTimeout("fresh()", 50)
    </SCRIPT>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="/Instagrim/image.css" />
    </head>
    <style type="text/css">    
        body{    
            background-image: url(/Instagrim/image/bg.jpg);    
            background-repeat: no-repeat;    
            background-size: cover;
        }    
    </style>
    <body>
        <header>
            <h1 class="centermy">InstaGrim ! </h1>
        </header>

        <nav><jsp:include page="nav.jsp"></jsp:include></nav>

        <%
            session.setAttribute("pictype", "all");
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            int a = 0;
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                a = a + 1;
                session.setAttribute("numofpic", Integer.toString(a));
                Pic p = (Pic) iterator.next();
                String id = "picID" + Integer.toString(a);
                session.setAttribute(id, p.getSUUID());
        %>

        <ul class="all">
            <div>
                <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>" alt="Fjords" width="400" height="auto"></a>
            </div>
            <%
                String user = (String) session.getAttribute("user" + p.getSUUID());
                String viewtimes = (String) session.getAttribute("times" + p.getSUUID());
                String likes = (String) session.getAttribute("likes" + p.getSUUID());
                String unlikes = (String) session.getAttribute("unlikes" + p.getSUUID());
            %>
            <div class="picinfo">
                <a href="/Instagrim/profile/<%=user%>" ><div>Uploder:<%=user%></div></a>
                <div>Viewtimes:<%=viewtimes%></div>
            </div>
            
            <div class="likesandunlikes">
                <a style="color:blue" href="/Instagrim/likes/<%=p.getSUUID()%>" ><div>Likes:<%=likes%></div></a>
                <a style="color:blue" href="/Instagrim/unlikes/<%=p.getSUUID()%>" ><div>Unlikes:<%=unlikes%></div></a>
            </div>
        </div>
        <div class="up">
            <%
                String name = p.getSUUID();
                String comment = (String) session.getAttribute(name);
            %>
            <div class="comment"><%=comment%></div>
        </div>

        <%
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            if (lg != null) {
                if (lg.getlogedin()) {
        %>
        <div class="bottom">
            <form method="POST" action="Image">
                <textarea name="comment<%=a%>" placeholder="Make Your Comment" required="" type="text"></textarea>
                <input class="hello" value="Submit" type="submit">
            </form>
        </div>
        <%}
        } else {
        %>
        <div class="bottom">
            <div class="textinfo">Please login to add comment!</div>
        </div>
        <%}%>
    </ul>
    <%
            }
        }
    %>
</body>
</html>
