<%-- Created by IntelliJ IDEA.--%>
<%--  User: beatakazmierczak--%>
<%--  Date: 23/01/2024--%>
<%--  Time: 21:12--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name='viewport' content='width=device-width, initial-scale=1'>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
        <title>Deliveries</title>
    </head>

    <body>
        <%
            int i = 0;
        %>
        <div class="bg-primary p-2 text-dark bg-opacity-10">
            <div class="fas" style='font-size:24px'>&#xf6be;</div>
<%--            <button type="button" class="btn btn-primary btn-lg" role="button" data-bs-toggle="button" aria-pressed="true">Add new delivery to track</button>--%>
            <br>
            <div class="d-grid gap-2 col-4 mx-auto">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#my">
                Add new delivery to track
            </button>
            </div>

            <!-- Modal -->
            <div class="modal fade" id="my" tabindex="-1" aria-labelledby="myLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-primary-subtle">
                            <h1 class="modal-title fs-5 fw-bold" id="myLabel">Add new delivery to track</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="${pageContext.request.contextPath}/add" method="get" id="modal-details">
                                <p>Insert delivery information data:</p>
                                <div class="mb-3">
                                    <label for="deliveryNumber" class="form-label fw-semibold">Delivery number</label>
                                    <input type="text" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="deliveryNumber" aria-describedby="numberHelp" name="deliveryNumber">
                                    <div id="numberHelp" class="form-text">We'll never share your number with anyone else.</div>
                                </div>
                                <br>
                                <div class="mb-3">
                                    <label for="deliverer" class="form-label fw-semibold">Deliverer</label>
                                    <select class="form-select form-select-lg mb-2 bg-primary p-2 text-dark bg-opacity-10" id="deliverer" aria-label="Large select example" name="deliverer">
<%--                                        <option selected>Select deliverer</option>--%>
                                        <option value="INPOST">INPOST</option>
                                        <option value="POCZTA_POLSKA">POCZTA POLSKA</option>
                                        <option value="DPD">DPD</option>
                                        <option value="DHL">DHL</option>
                                    </select>
                                    <br>
                                </div>
                                <div class="mb-3">
                                    <label for="deliveryDescription" class="form-label fw-semibold">Delivery description</label>
                                    <input type="text" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="deliveryDescription" aria-describedby="descriptionHelp" name="deliveryDescription">
                                    <div id="descriptionHelp" class="form-text">Delivery description shouldn't exceed 10 characters.</div>
                                </div>
                                <br>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary" form="modal-details">Save changes</button>
                        </div>
                    </div>
                </div>
                <br>
                <br>
            </div>
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
                    <th scope="col"></th>
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
<%--                            <td><a class="nav-link btn bi-trash3" href="${pageContext.request.contextPath}/deleting?deliveryId=${delivery.deliveryId}" role="button" data-toggle="tooltip" data-placement="top" title="Delete delivery"></a></td>--%>
                            <td>
                                <button class="btn bi-trash3 deleteBtn" data-delivery-id="${delivery.deliveryId}" data-bs-toggle="modal" data-bs-target="#deleteModal" data-toggle="tooltip" data-placement="top" title="Delete delivery"></button>
                            </td>
                            <td><div>
                                <!-- Button trigger modal -->
<%--                                <a class="btn bi-trash3" data-bs-toggle="modal" data-delivery-id="${delivery.deliveryId}" data-bs-target="#exampleModal" role="button" data-toggle="tooltip" data-placement="top" title="Delete delivery">--%>
<%--                                </a>--%>
                                </div>

                                <!-- Modal -->
                                <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header bg-primary-subtle">
                                                <h5 class="modal-title" id="exampleModalLabel">Are you sure you want to delete this delivery?</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">

                                                <div>Delivery number:${delivery.deliveryNumber}</div>
                                                <div>Deliverer:${delivery.deliverer}</div>
                                            </div>

                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                <a class="nav-link btn btn-primary" href="${pageContext.request.contextPath}/deleting?deliveryId=${delivery.deliveryId}" role="button">Delete this delivery</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
<%--                            <td><a class="nav-link btn bi-trash3" href="${pageContext.request.contextPath}/deleting?deliveryId=${delivery.deliveryId}" role="button" data-toggle="tooltip" data-placement="top" title="Delete delivery"></a></td>--%>
                        </tr>
                    </c:forEach>
                </tbody>
                    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteModalLabel">Delete delivery</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to delete the book with ID <span id="deliveryIdToDelete"></span>?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="button" class="btn btn-danger" onclick="deleteDelivery()">Delete delivery</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header bg-success-subtle">
                                    <h5 class="modal-title fs-5 fw-bold" id="successModalLabel">Deletion successful</h5>
                                    <br>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body bg-success p-2 text-dark bg-opacity-10">
                                    <span class="fw-bold" id="successMessage"></span>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        $('.deleteBtn').on('click', function () {
                            var deliveryId = $(this).data('delivery-id');
                            $('#deliveryIdToDelete').text(deliveryId);
                            $('#deleteModal').modal('show');
                        });

                        function deleteDelivery() {
                            var deliveryId = $('#deliveryIdToDelete').text();
                            // Now you can use AJAX or any method to send the bookId to your delete endpoint
                            // and handle the deletion in your controller.
                            // Example AJAX call:
                             $.ajax({
                                 type: "GET",
                                 url: "${pageContext.request.contextPath}/deleting?deliveryId=" + deliveryId,
                                 success: function () {
                                     $('#successModal').modal('show');
                                     $('#successMessage').text('A delivery with deliveryId ' + deliveryId + ' was deleted.');
                                     $('#deleteModal').modal('hide');
                                 }
                            //     error: function () {
                            //         // Handle error
                            //     }
                             });
                        }
                    </script>
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
