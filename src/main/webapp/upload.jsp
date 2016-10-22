<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
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
        <h1 class="centermy">InstaGrim ! </h1>
        <nav><jsp:include page="nav.jsp"></jsp:include></nav>

            <article>
                <h3 class="centermy" style="font-size:18px">File Upload</h3>
                <ul>
                <%session.setAttribute("numofpic", "0");%>
                <form method="POST" enctype="multipart/form-data" action="Image">

                    <a class="uploadfile2">Choose file to upload: <input type="file" name="upfile">
                        <a class="uploadfile"><input type="submit" value="Upload File"></a>
                    </a>

                    <div class="sel">
                        <div style="font-size:20px">Filter</div>
                        <select select name="selectcondition" id="selectcondition">
                            <option value="1">Grey the pic</option>
                            <option value="2">DissolveFilter</option>
                            <option value="3">SolarizeFilter</option>
                            <option value="4">KaleidoscopeFilter</option>
                            <option value="5">DisplaceFilter</option>
                            <option value="10">Normal</option>
                        </select>
                    </div>
                    </a>
                </form>
            </ul>
        </article>

    </body>
</html>
