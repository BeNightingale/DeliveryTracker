<!DOCTYPE>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <!-- Include Bootstrap CSS (make sure you already load this in your layout if using one) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Optional: Bootstrap JS for modals (not required here because we force show via CSS) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <title>Account Confirmed</title>
</head>
<body class="bg-primary p-2 text-dark bg-opacity-10">

<!-- Modal (shown immediately on page load) -->
<div class="modal show d-block" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0">
            <div class="modal-header bg-primary-subtle">
                <h1 class="modal-title fs-5 fw-bold .text-success">✅ Registration Confirmed</h1>
                <br>
            </div>
            <div class="modal-body bg-success p-2 text-dark bg-opacity-10">
                <p class="mb-3">Your account has been successfully created. You can now log in.</p>
<%--                <div class="text-end">--%>
<%--                    <a href="${pageContext.request.contextPath}/delivery_tracking/log" class="btn btn-success">Go to Login</a>--%>
<%--                </div>--%>
                <div class="text-end">
                    <button style="text-align: center" class="btn btn-success btn-lg refreshBtn" type="submit">
                        <a class="nav-link" href="${pageContext.request.contextPath}/delivery_tracking/log">Go to Login</a>
                    </button>
                    <button type="button" class="btn btn-secondary btn-lg refreshBtn" data-bs-dismiss="modal">Close</button>
                </div>
<%--                <div class="modal-footer">--%>
<%--                    <button type="button" class="btn btn-secondary refreshBtn" data-bs-dismiss="modal">Close</button>--%>
<%--                </div>--%>

            </div>
        </div>
    </div>
</div>

<!-- Optional: Bootstrap JS for modals (not required here because we force show via CSS) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>