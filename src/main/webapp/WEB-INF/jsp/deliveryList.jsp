<%--
  Created by IntelliJ IDEA.
  User: beatakazmierczak
  Date: 23/01/2024
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
        <title>Deliveries</title>
    </head>

    <body>
        <%
            int i = 0;
        %>
        <div class="bg-primary p-2 text-dark bg-opacity-10">
            <table class="table table-striped table-hover align-middle caption-top table-light">
                <caption>List of active deliveries</caption>
                <thead class="table-header table-primary">
                <tr class="bg-primary p-2 text-dark bg-opacity-20">
                    <th scope="col">no</th>
                    <th scope="col">Delivery number</th>
                    <th scope="col">Delivery description</th>
                    <th scope="col">Deliverer</th>
                    <th scope="col">Status</th>
                    <th scope="col">Last status change date</th>
                </tr>
                </thead>
                <tbody>
                     <c:forEach var="delivery" items="${deliveries}">
                        <tr>
                            <% i+=1;%>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}"><%=i%></a></td>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryNumber}</a></td>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryDescription}</a></td>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliverer}</a></td>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryStatus}</a></td>
                            <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">null</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <ul class="pagination justify-content-end">
                <li class="page-item"><a class="page-link" href="#">Previous</a></li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item"><a class="page-link" href="#">Next</a></li>
            </ul>
        </div>
    </body>
</html>
