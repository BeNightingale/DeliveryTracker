<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <title>Information about the delivery</title>
</head>
<body>
    <br>
    <table class="table table-striped table-hover align-middle caption-top table-light">
        <caption>Information about the delivery</caption>
        <thead class="table-header table-primary">
            <tr class="bg-primary p-2 text-dark bg-opacity-20">
                <th scope="col">Delivery id</th>
                <th scope="col">Delivery created</th>
                <th scope="col">Delivery number</th>
                <th scope="col">Delivery description</th>
                <th scope="col">Deliverer</th>
                <th scope="col">Status</th>
                <th scope="col">Status description</th>
                <th scope="col">Finished</th>
                <th scope="col">Last status change date</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${delivery.deliveryId}</td>
                <td>${delivery.deliveryCreated}</td>
                <td>${delivery.deliveryNumber}</td>
                <td>${delivery.deliveryDescription}</td>
                <td>${delivery.deliverer}</td>
                <td>${delivery.deliveryStatus}</td>
                <td>${delivery.statusDescription}</td>
                <td>${delivery.finished}</td>
                <td>${delivery.statusChangeDatetime}</td>
            </tr>
        </tbody>
    </table>
    <br>
    <br>
    <table class="table table-striped table-hover align-middle caption-top table-light">
        <caption>Delivery history</caption>
        <thead class="table-header table-primary">
            <tr class="bg-primary p-2 text-dark bg-opacity-20">
                <th scope="col">Delivery id</th>
                <th scope="col">Delivery number</th>
                <th scope="col">Delivery status</th>
                <th scope="col">Status description</th>
                <th scope="col">Status change datetime</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="history" items="${historyList}">
                <tr>
                    <td>${history.deliveryId}</td>
                    <td>${history.deliveryNumber}</td>
                    <td>${history.deliveryStatus}</td>
                    <td>${history.statusDescription}</td>
                    <td>${history.statusChangeDatetime}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>