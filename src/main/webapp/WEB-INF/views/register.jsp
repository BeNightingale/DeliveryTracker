<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--    <meta charset="utf-8">--%>
<%--    <meta name='viewport' content='width=device-width, initial-scale=1'>--%>
<%--    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"--%>
<%--          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">--%>
<%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"--%>
<%--            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"--%>
<%--            crossorigin="anonymous"></script>--%>
<%--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">--%>
<%--    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>--%>
<%--    <title>Login</title>--%>

<%--</head>--%>
<%--<body>--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>

<%--<div class="col-sm-4 mb-3" style="width: 500px">Register form:</div>--%>
<br/>
<%--<form method="get" action="${pageContext.request.contextPath}/pending_users">--%>
<%--            <div class="container">--%>
<%--    <div class="row">--%>
<%--        <div class="col-sm-4 mb-3" style="width: 500px">--%>
<%--            <label for="username" class="form-label fw-semibold fs-5">Username:</label>--%>
<%--            <input type="text" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="username"--%>
<%--                   name="username"/>--%>
<%--            <br>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="row">--%>
<%--        <div class="col-sm-4 mb-3" style="width: 500px">--%>
<%--            <label for="email" class="form-label fw-semibold fs-5">Email:</label>--%>
<%--            <input type="email" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="email"--%>
<%--                   name="email"/>--%>
<%--            <br>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="row">--%>
<%--        <div class="col-sm-4 mb-3" style="width: 500px">--%>
<%--            <label for="password" class="form-label fw-semibold fs-5">Password:</label>--%>
<%--            <input type="password" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="password"--%>
<%--                   name="password"/>--%>
<%--            <br>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    &lt;%&ndash;        </div>&ndash;%&gt;--%>
<%--    <br/>--%>
<%--    <br>--%>
<%--    <button class="btn btn-primary btn-lg refreshBtn" type="submit" value="Register">Register</button>--%>
<%--    <br/>--%>
<body class="bg-primary p-2 text-dark bg-opacity-10">
<br>
<br>
<h1 class="position-absolute top-10 start-50 translate-middle">Delivery Tracking Application</h1>
<br>
<br>
<br>
<h2 class="position-absolute top-10 start-50 translate-middle">Register</h2>
<br>
<br>
<div class="container d-flex justify-content-center  align-content-center">
    <form method="post" action="${pageContext.request.contextPath}/pending_users">
        <div class="col-sm-4 mb-3" style="width: 500px">
            <label for="userName" class="form-label fw-semibold fs-5">User name:</label>
            <input type="text" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="userName" required
                   name="userName"/>
            <br>
        </div>
        <div class="col-sm-4 mb-3" style="width: 500px">
            <label for="email" class="form-label fw-semibold fs-5">Email:</label>
            <input type="email" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="email" required
                   name="email"/>
            <br>
        </div>
        <div class="col-sm-4 mb-3" style="width: 500px">
            <label for="password" class="form-label fw-semibold fs-5">Password:</label>
            <input type="password" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="password" required
                   name="password"/>
            <br>
        </div>
        <button type="submit" class="btn btn-primary" style="font-size: large">Register</button>
    </form>

    <c:if test="${not empty message}">
        <div class="alert alert-info mt-3">${message}</div>
    </c:if>
</div>
</body>
</html>