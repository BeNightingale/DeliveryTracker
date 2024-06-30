<%--  Date: 23/01/2024--%>
<%--  Time: 21:12--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <head>
        <meta charset="utf-8">
        <meta name='viewport' content='width=device-width, initial-scale=1'>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<%-- The 2 lines below enable using dataTable for sorting, paging, searching in table.--%>
        <link rel="stylesheet" href="https://cdn.datatables.net/2.0.8/css/dataTables.dataTables.min.css">
        <script type="text/javascript" src="https://cdn.datatables.net/2.0.8/js/dataTables.min.js"></script>

        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
<%-- The function below enables sorting, paging, searching in table.--%>
        <script>
            $(document).ready(function() {
                $('#myTable').dataTable();
            });
        </script>
        <style>
            .tooltip-inner {
                background-color: cornflowerblue !important;
                /*!important is not necessary if you place custom.css at the end of your css calls. For the purpose of this demo, it seems to be required in SO snippet*/
                color: black;
                font-weight: 500;
            }

            .tooltip.top .tooltip-arrow {
                border-top-color: cornflowerblue;
            }
        </style>
        <style>
            .truncate-cell {
                max-width: 150px;
                white-space: nowrap;
                overflow: hidden !important;
                text-overflow: ellipsis !important;
                cursor: pointer;
            }
        </style>
        <title>Deliveries</title>
    </head>

    <body>
        <%
            int i = 0;
        %>
        <div class="bg-primary p-2 text-dark bg-opacity-10">
            <br>
            <H2 class="position-absolute top-10 start-50 translate-middle">Deliveries</H2>
            <br>
            <br>
            <div class="fas position-absolute top-10 start-50 translate-middle" style='font-size:24px'>&#xf6be;</div>
            <br>
            <div class="container">
                <div class="row row-cols-3 mx-auto">
                    <div class="col">
                        <div style="padding: 0px 70px;">
                        <button class="btn btn-primary btn-lg refreshBtn" style="width: 270px">Refresh page</button>
                        </div>
                    </div>
                    <div class="col">
                        <div style="padding: 0px 70px;">
                        <button type="button" class="btn btn-primary btn-lg addDeliveryBtn" style="width: 270px" data-bs-toggle="modal" data-bs-target="#addDeliveryModal" aria-pressed="true">
                            Add new delivery to track
                        </button>
                        </div>
                    </div>
                    <div class="col">
                        <div style="padding: 0px 70px;">
                        <button type="button" class="btn btn-primary btn-lg updateDeliveryBtn" style="width: 270px" data-bs-toggle="modal" data-bs-target="#updateDeliveryModal" aria-pressed="true">
                            Update deliveries
                        </button>
                        </div>
                    </div>
                </div>
            </div>
            <br>
<%--            <div class="d-grid gap-2 col-4 mx-auto">--%>
<%--                <button type="button" class="btn btn-primary btn-lg addDeliveryBtn" data-bs-toggle="modal" data-bs-target="#addDeliveryModal" aria-pressed="true">--%>
<%--                    Add new delivery to track--%>
<%--                </button>--%>
<%--                <button class="btn btn-primary btn-lg refreshBtn">Refresh</button>--%>
<%--                <button type="button" class="btn btn-primary btn-lg updateDeliveryBtn" data-bs-toggle="modal" data-bs-target="#updateDeliveryModal" aria-pressed="true">--%>
<%--                    Update deliveries--%>
<%--                </button>--%>
<%--            </div>--%>
            <div class="table-responsive">
                <table id="myTable" class="display table table-striped table-hover align-middle caption-top table-light table-responsive-sm" >
                    <caption title="List of deliveries"></caption>
                    <thead class="table-header table-primary">
                    <tr class="bg-primary p-2 text-dark bg-opacity-20">
                        <th scope="col" style="width: 50px">no</th>
                        <th scope="col" style="width: 100px">Delivery number</th>
                        <th scope="col" style="width: 200px">Delivery description</th>
                        <th scope="col" style="width: 150px">Deliverer</th>
                        <th scope="col" style="width: 160px">Status</th>
                        <th scope="col" style="width: 180px">Last status change date</th>
                        <th scope="col" style="width: 70px"></th>
                    </tr>
                    </thead>
                    <tbody>
                         <c:forEach var="delivery" items="${deliveries}">
                            <tr>
                                <% i+=1;%>
                                <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}"><%=i%></a></td>
                                <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryNumber}</a></td>
                                <td><div class="truncate-cell" data-toggle="tooltip" data-placement="top" title="${delivery.deliveryDescription}"><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryDescription}</a></div></td>
                                <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliverer}</a></td>
                                <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.deliveryStatus}<span style="white-space: pre;">  </span>
                                    <c:choose>
                                        <c:when test="${delivery.finished=='true'}">
                                            <i class="bi bi-house-check-fill" style="color: forestgreen"></i>
                                        </c:when>
                                    </c:choose>
                                </a></td>
                                <td><a class="nav-link" href="${pageContext.request.contextPath}/delivery?deliveryId=${delivery.deliveryId}">${delivery.statusChangeDatetime}</a></td>
                                <td>
                                    <button class="btn bi-trash3 deleteBtn" style="color: darkorange" data-delivery-id="${delivery.deliveryId}" data-delivery-number="${delivery.deliveryNumber}" data-delivery-description="${delivery.deliveryDescription}" data-delivery-status="${delivery.deliveryStatus}" data-delivery-deliverer="${delivery.deliverer}" data-bs-toggle="modal" data-bs-target="#deleteModal" data-toggle="tooltip" data-placement="top" title="Delete delivery"></button>
                                </td>
                            </tr>
                        </c:forEach>

                                                <%-- MODALS --%>

    <!-- Modal for adding deliveries -->
                         <div class="modal fade" id="addDeliveryModal" tabindex="-1" aria-labelledby="addDeliveryModalLabel" aria-hidden="true">
                             <div class="modal-dialog modal-dialog-centered">
                                 <div class="modal-content">
                                     <div class="modal-header bg-primary-subtle">
                                         <h1 class="modal-title fs-5 fw-bold" id="addDeliveryModalLabel">Add new delivery to track</h1>
                                         <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body">
                                         <form id="modal-details">
                                             <p>Insert delivery information data:</p>
                                             <div class="mb-3">
                                                 <label for="deliveryNumber" class="form-label fw-semibold">Delivery number</label>
                                                 <input type="text" class="form-control bg-primary p-2 text-dark bg-opacity-10" id="deliveryNumber" required aria-describedby="numberHelp" name="deliveryNumber">
                                                 <div id="numberHelp" class="form-text">We'll never share your number with anyone else.</div>
                                             </div>
                                             <br>
                                             <div class="mb-3">
                                                 <label for="deliverer" class="form-label fw-semibold">Deliverer</label>
                                                 <select class="form-select form-select-lg mb-2 bg-primary p-2 text-dark bg-opacity-10" id="deliverer" required aria-label="Large select example" name="deliverer">
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
                                                 <div id="descriptionHelp" class="form-text">Delivery description shouldn't exceed 200 characters.</div>
                                             </div>
                                             <br>
                                         </form>
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                         <button type="button" class="btn btn-primary" form="modal-details" onclick="addDelivery()">Save changes</button>
                                     </div>
                                 </div>
                             </div>
                             <br>
                         </div>

    <!-- Modal for success in adding deliveries -->
                         <div class="modal fade" id="successAddDeliveryModal" tabindex="-1" aria-labelledby="successAddDeliveryModalLabel" aria-hidden="true">
                             <div class="modal-dialog">
                                 <div class="modal-content">
                                     <div class="modal-header bg-success-subtle">
                                         <h5 class="modal-title fs-5 fw-bold" id="successAddDeliveryModalLabel">Delivery added successfully!</h5>
                                         <br>
                                         <button type="button" class="btn btn-close refreshBtn" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body bg-success p-2 text-dark bg-opacity-10">
                                         <span id="successAddDeliveryMessageAboutDeliveryNumber"></span>
                                         <br>
                                         <span id="successAddDeliveryMessageAboutDeliverer"></span>
                                         <br>
                                         <span id="successAddDeliveryMessageAboutDeliveryDescription"></span>
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary refreshBtn" data-bs-dismiss="modal">Close</button>
                                     </div>
                                 </div>
                             </div>
                         </div>

    <!-- Modal for updating deliveries -->
                         <div class="modal fade" id="updateDeliveryModal" tabindex="-1" aria-labelledby="updateDeliveryModalLabel" aria-hidden="true">
                             <div class="modal-dialog modal-dialog-centered">
                                 <div class="modal-content">
                                     <div class="modal-header bg-primary-subtle">
                                         <h1 class="modal-title fs-5 fw-bold" id="updateDeliveryModalLabel">Are you sure you want to update deliveries?</h1>
                                         <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body">
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                         <button type="button" class="btn btn-primary" form="modal-details" onclick="updateDeliveries()">Confirm update</button>
                                     </div>
                                 </div>
                             </div>
                             <br>
                         </div>

    <!-- Modal for success in updating deliveries -->
                         <div class="modal fade" id="successUpdateDeliveryModal" tabindex="-1" aria-labelledby="successUpdateDeliveryModalLabel" aria-hidden="true">
                             <div class="modal-dialog">
                                 <div class="modal-content">
                                     <div class="modal-header bg-success-subtle">
                                         <h5 class="modal-title fs-5 fw-bold" id="successUpdateDeliveryModalLabel">Deliveries updated successfully!</h5>
                                         <br>
                                         <button type="button" class="btn btn-close refreshBtn" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body bg-success p-2 text-dark bg-opacity-10">
                                            <p>The number of updated deliveries: <span id="successRowsChangedNumber"></span>.</p>
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary refreshBtn" data-bs-dismiss="modal">Close</button>
                                     </div>
                                 </div>
                             </div>
                         </div>

    <%-- Modal for deleting deliveries --%>
                         <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                             <div class="modal-dialog">
                                 <div class="modal-content">
                                     <div class="modal-header bg-primary-subtle">
                                         <h5 class="modal-title fs-5 fw-bold" id="deleteModalLabel">Delete delivery</h5>
                                         <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body">
                                         <p>Are you sure you want to delete the delivery with id <span id="deliveryIdToDelete"></span>?</p>
                                         <p>The delivery number: <span id="deliveryNumberToDelete"></span>.</p>
                                         <p><span id="deleteDeliveryDescriptionMessage"></span></p>
                                         <p>The delivery status: <span id="deliveryStatusToDelete"></span>.</p>
                                         <p>The deliverer: <span id="delivererDeliveryToDelete"></span>.</p>
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                         <button type="button" class="btn btn-danger" onclick="deleteDelivery()">Delete delivery</button>
                                     </div>
                                 </div>
                             </div>
                         </div>

    <%-- Modal for success in deleting deliveries --%>
                         <div class="modal fade" id="successDeleteModal" tabindex="-1" aria-labelledby="successDeleteModalLabel" aria-hidden="true">
                             <div class="modal-dialog">
                                 <div class="modal-content">
                                     <div class="modal-header bg-success-subtle">
                                         <h5 class="modal-title fs-5 fw-bold" id="successDeleteModalLabel">Deletion successful</h5>
                                         <br>
                                         <button type="button" class="btn btn-close refreshBtn" data-bs-dismiss="modal" aria-label="Close"></button>
                                     </div>
                                     <div class="modal-body bg-success p-2 text-dark bg-opacity-10">
                                         <span id="successDeleteMessageAboutDeliveryId"></span>
                                         <br>
                                         <span id="successDeleteMessageAboutDeliveryNumber"></span>
                                         <br>
                                         <span id="successDeleteMessageAboutDeliveryDescription"></span>
                                     </div>
                                     <div class="modal-footer">
                                         <button type="button" class="btn btn-secondary refreshBtn" data-bs-dismiss="modal">Close</button>
                                     </div>
                                 </div>
                             </div>
                         </div>
                    </tbody>
                </table>
            </div>
        </div>

                                            <%-- SCRIPTS --%>

<%-- Adding deliveries --%>
        <script>
            $('.addDeliveryBtn').on('click', function () {
                $('#addDeliveryModal').modal('show');
            });

            function addDelivery() {
                var deliveryNumber = document.getElementById('deliveryNumber').value;
                var deliverer = document.getElementById('deliverer').value;
                var deliveryDescription = document.getElementById('deliveryDescription').value;
                var deliveryDto = {
                    deliveryNumber: deliveryNumber,
                    deliverer: deliverer,
                    deliveryDescription: deliveryDescription
                };
                var infoAboutDeliveryNumber = 'The delivery with number ' + deliveryNumber + ' will be tracked.'
                var infoAboutDeliverer = 'The deliverer: ' + deliverer + '.'
                var infoAboutDeliveryDescription = "The delivery has no description.";
                if (deliveryDescription !== null && deliveryDescription.length !== 0) {
                    infoAboutDeliveryDescription = 'The delivery description: ' + deliveryDescription + '.'
                }
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/inserts",
                    data: JSON.stringify(deliveryDto),
                    contentType:"application/json",
                    success: function () {
                        $('#addDeliveryModal').modal('hide');
                        $('#successAddDeliveryMessageAboutDeliveryNumber').text(infoAboutDeliveryNumber);
                        $('#successAddDeliveryMessageAboutDeliverer').text(infoAboutDeliverer);
                        $('#successAddDeliveryMessageAboutDeliveryDescription').text(infoAboutDeliveryDescription);
                        $('#successAddDeliveryModal').modal('show');
                    },
                    error: function (xhr) {
                        var errorMessage = xhr.responseText;
                        var parsedErrorMessage = JSON.parse(errorMessage);
                        alert("Error: " + parsedErrorMessage.message);
                    }
                })
            }
        </script>

<%-- Updating deliveries --%>
        <script>
            $('.updateDeliveryBtn').on('click', function () {
                $('#updateDeliveryModal').modal('show');
            });

            function updateDeliveries() {
                $.ajax({
                    type: "GET",
                    url: "${pageContext.request.contextPath}/updates",

                    success: function (response) {
                        var rowsChanged = response.rowsChanged;
                        $('#updateDeliveryModal').modal('hide');
                        $('#successRowsChangedNumber').text(rowsChanged);
                         $('#successUpdateDeliveryModal').modal('show');
                    },
                    error: function (xhr, error) {
                        var errorMessage = xhr.responseText;
                    //    var errorMessage = response.errorMessage;
                        var parsedErrorMessage = JSON.parse(errorMessage);
                        alert("Error: " + parsedErrorMessage.message);
                    }
                })
            }
        </script>

<%-- Deleting deliveries --%>
        <script>
            $('.deleteBtn').on('click', function () {
                var deliveryId = $(this).data('delivery-id');
                var deliveryNumber = $(this).data('delivery-number');
                var deliveryDescription = $(this).data('delivery-description');
                var deliveryStatus = $(this).data('delivery-status');
                var deliverer = $(this).data('delivery-deliverer');
                var deleteDescriptionMassage = "The delivery has no description.";
                if (deliveryDescription !== null && deliveryDescription.length !== 0) {
                    deleteDescriptionMassage = 'The delivery description: ' + deliveryDescription + '.';
                }
                $('#deliveryIdToDelete').text(deliveryId);
                $('#deliveryNumberToDelete').text(deliveryNumber);
                $('#deliveryDescriptionToDelete').text(deliveryDescription);
                $('#deliveryStatusToDelete').text(deliveryStatus);
                $('#delivererDeliveryToDelete').text(deliverer);
                $('#deleteDeliveryDescriptionMessage').text(deleteDescriptionMassage);
                $('#deleteModal').modal('show');
            });

            function deleteDelivery() {
                var deliveryId = $('#deliveryIdToDelete').text();
                var deliveryNumber = $('#deliveryNumberToDelete').text();
                var successDeleteInfoDeliveryId = 'The delivery with deliveryId ' + deliveryId + ' was deleted.';
                var successDeleteInfoDeliveryNumber = 'The delivery number: ' + deliveryNumber + '.';
                var successDeleteInfoDeliveryDescription = $('#deleteDeliveryDescriptionMessage').text();
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/deletions?deliveryId=" + deliveryId,
                    success: function () {
                        $('#successDeleteModal').modal('show');
                        if ("The delivery has no description." === successDeleteInfoDeliveryDescription) {
                            successDeleteInfoDeliveryDescription = "The delivery had no description.";
                        }
                        $('#successDeleteMessageAboutDeliveryId').text(successDeleteInfoDeliveryId);
                        $('#successDeleteMessageAboutDeliveryNumber').text(successDeleteInfoDeliveryNumber);
                        $('#successDeleteMessageAboutDeliveryDescription').text(successDeleteInfoDeliveryDescription);
                        $('#deleteModal').modal('hide');
                    }
                    //     error: function () {
                    //         // Handle error
                    //     }
                })}
        </script>

<%-- Refreshing page --%>
        <script>
            $('.refreshBtn').on('click', function () {
                refresh();
            });
            function refresh() {
                $.ajax({
                    type: "GET",
                    url: "${pageContext.request.contextPath}/",
                    success: function() {
                        location.reload(); // Refresh the page after the AJAX call is successful
                    },
                })}
        </script>

    </body>
</html>
