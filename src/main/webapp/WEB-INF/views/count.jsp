<%--
  Created by IntelliJ IDEA.
  User: beatakazmierczak
  Date: 21/01/2024
  Time: 09:01
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
<%--        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">--%>
<%--        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/operation.css" type="text/css">--%>
        <title>count</title>
    </head>
    <body>
        <p class="bg-light">Search deliveries count:</p>
        <br>
        <table class="table caption-top table-bordered border-primary bg-body-tertiary" >
            <caption>The number of all deliveries</caption>
            <thead>
                <tr>
                    <th scope="col">actual count</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                <th scope="col">${count}</th>
                </tr>
            </tbody>
        </table>
        <br>
        <table class="table table-striped table-hover align-middle caption-top bg-success-subtle">
            <caption>List of deliveries</caption>
            <thead class="table-header" style="background-color: lightblue">
                <tr>
                    <th scope="col">Delivery number</th>
                    <th scope="col">Delivery description</th>
                    <th scope="col">Deliverer</th>
                    <th scope="col">Status</th>
                    <th scope="col">Last status change date</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th scope="row">Margaret Nguyen</th>
                    <td>427311</td>
                    <td><time datetime="2010-06-03">June 3, 2010</time></td>
                    <td>0.00</td>
                </tr>
                <tr>
                    <th scope="row">Edvard Galinski</th>
                    <td>533175</td>
                    <td><time datetime="2011-01-13">January 13, 2011</time></td>
                    <td>37.00</td>
                </tr>
                <tr>
                    <th scope="row">Hoshi Nakamura</th>
                    <td>601942</td>
                    <td><time datetime="2012-07-23">July 23, 2012</time></td>
                    <td>15.00</td>
                </tr>
            </tbody>
        </table>

    </body>
</html>
