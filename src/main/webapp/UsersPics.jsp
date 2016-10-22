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
            session.setAttribute("pictype", "user");
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
                <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>" alt="Fjords" width="380" height="auto"></a>
            </div>

            <div class="up">
                <%
                    String name = p.getSUUID();
                    String comment = (String) session.getAttribute(name);
                %>
                <div class="comment"><%=comment%></div>
            </div>

            <div class="bottom">
                <form method="POST" action="Image">
                    <input name="comment<%=a%>" placeholder="Make Your Comment" required="" type="text">
                    <input class="hello" value="Submit" style="width:100%;" type="submit">
                </form>
            </div>
        </ul>
        <%
                }
            }
        %>
    </body>
</html>
