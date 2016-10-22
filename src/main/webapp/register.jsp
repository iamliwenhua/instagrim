<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

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

        <div class="login">
            <div class="message">Register as user</div>
            <div id="darkbannerwrap"></div>
            <form method="POST"  action="Register">
                <input name="username" placeholder="Username" required="" type="text">
                <hr class="hr15">
                <input name="firstname" placeholder="Firstname" required="" type="text">
                <hr class="hr15">
                <input name="lastname" placeholder="Lastname" required="" type="text">
                <hr class="hr15">
                <input name="email" placeholder="Email" required="" type="email">
                <hr class="hr15">
                <input name="password" placeholder="Password" required="" type="password">
                <hr class="hr15">
                
                <input value="Register" style="width:100%;" type="submit">
                <hr class="hr20">
            </form>
        </div>
    </body>
</html>
