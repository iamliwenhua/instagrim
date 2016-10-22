<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
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
            <div class="message">Log to InstaGrim</div>
            <div id="darkbannerwrap"></div>
            <form method="POST"  action="Login">
                <font color="red">${loginstate}</font>
                <input name="username" placeholder="Username" required="" type="text">
                <hr class="hr15">
                <input name="password" placeholder="Password" required="" type="password">
                <hr class="hr15">
                <input value="Log In" style="width:100%;" type="submit">
                <hr class="hr20">

            </form>
        </div>
        <div class="copyright"><a href="Home" target="index.jsp"></a></div>
    </body>
</html>
