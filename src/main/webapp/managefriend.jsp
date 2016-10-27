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
            String friend = (String) request.getAttribute("friends");
            if (friend.equals("empty")) {
        %>
        <ul class="userpics" style="height:50px">
            <a>No Friend</a>
        </ul>
        <%} else {
            String[] parts = friend.split(",");
            for (String user : parts) {
        %>
        <ul class="userpics" style="height:50px">
            <a class="follow3" href="/Instagrim/profile/<%=user%>"><%=user%></a>

            <a class="follow2" href="/Instagrim/deletefriend/<%=user%>">UnFollow</a>
        </ul>
        <%}
            }%>

        <ul class="userinfo">
            <div class="relative">
                <%
                    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                    boolean flag = lg.gethaveUserPic();
                    if (flag) {
                %>
                <img src="/Instagrim/userpic/<%=lg.getUsername()%>" alt="Fjords" width="150" height="150">
                <% } else {%>
                <img src="/Instagrim/image/user.png" alt="Fjords" width="150" height="150">
                <%}%>

            </div>    
            <div>
                <br><br><br>
                <a class="afont">  Username:<%=lg.getUsername()%></a><br>
                <a class="afont">  Firstname:<%=lg.getFirstname()%></a><br>
                <a class="afont">  Lastname :<%=lg.getLastname()%></a><br>
                <a class="afont">  Email    :<%=lg.getEmail()%></a>
            </div>
        </ul>
    </body>
</html>
