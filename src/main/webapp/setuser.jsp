<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />

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
                session.setAttribute("username", lg.getUsername());
            %>

        <div class="info">
            <%
                boolean flag=lg.gethaveUserPic();
                if (flag) {
            %>
            <a href="/Instagrim/Image/userprofilepic" ><img src="/Instagrim/userpic/<%=lg.getUsername()%>" alt="Fjords" width="150" height="150"></a>
            <% }else{%>
            <a href="/Instagrim/Image/userprofilepic" ><img src="image/user.png" alt="Fjords" width="150" height="150"></a>
            <%}%>
            <div class="infoab">
                <a>Firstname:<%=lg.getFirstname()%></a><br>
            <a>Lastname:<%=lg.getLastname()%></a><br>
            <a>Email:<%=lg.getEmail()%></a><br>
            <a style="color:white;font-style:normal">click the picture to change</a>
            </div>
            <div class="changeinfo">
                <div class="message">Change your setting</div>
                <div id="darkbannerwrap"></div>
                <form method="POST"  action="SetUser">
                    <input name="firstname" placeholder="Firstname" required="" type="text">
                    <hr class="hr15">
                    <input name="lastname" placeholder="Lastname" required="" type="text">
                    <hr class="hr15">
                    <input name="email" placeholder="Email" required="" type="email">
                    <hr class="hr15">
                    <input name="address" placeholder="Address" required="" type="text">
                    <hr class="hr15">
                    <input value="Submit" style="width:100%;" type="submit">
                    <hr class="hr20">
                </form>
            </div>
        </div>
    </body>
</html>
