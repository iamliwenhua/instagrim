<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <style type="text/css">    
        body{    
            background-image: url(image/bg.jpg);    
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
                LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                if (lg != null) {
                    if (lg.getlogedin()) {
                        String username=lg.getUsername();
            %>
        <div class="relative">
            <%
                boolean flag=lg.gethaveUserPic();
                if (flag) {
            %>
            <img class="img" src="/Instagrim/userpic/<%=lg.getUsername()%>" alt="Fjords" width="150" height="150">
            <% }else{%>
            <img class="img" src="image/user.png" alt="Fjords" width="150" height="150">
            <%}%>
            <div class="absolute">
            <a class="afont">Firstname:<%=lg.getFirstname()%></a><br>
            <a class="afont">Lastname :<%=lg.getLastname()%></a><br>
            <a class="afont">Email    :<%=lg.getEmail()%></a>
            </div>
        </div>
        
        <a class="centermy" style="font-size: 50px">Welcome,<%=lg.getFirstname()%> <%=lg.getLastname()%></a>
        <%}
            }%>
    </body>



</html>
