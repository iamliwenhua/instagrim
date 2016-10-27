<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <ul>
        <%
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            if (lg != null) {
                String UserName = lg.getUsername();
                if (lg.getlogedin()) {
        %>
        <li><a href="/Instagrim/profile/<%=lg.getUsername()%>">My Index</a></li>
        <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
        <li><a href="/Instagrim/upload.jsp">Upload</a></li>
        <li><a href="/Instagrim/setuser.jsp">Setting</a></li>
        <li><a href="/Instagrim/viewfriend/all">Friends</a></li>
        <li><a href="/Instagrim/manage/<%=lg.getUsername()%>">Manage</a></li>
        <li><a href="/Instagrim/Logout">Logout</a></li>
            <%}
            } else {
            %>
        <li><a href="/Instagrim/register.jsp">Register</a></li>
        <li><a href="/Instagrim/Login">Login</a></li>
            <%}%>
        <li><a href="/Instagrim/Images/allpics">All Pictures</a></li>
        <li style="float:right"><a  class="activef"  href="/Instagrim/index.jsp">Home</a></li>
    </ul>
</html>
