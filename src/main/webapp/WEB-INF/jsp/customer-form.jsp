<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Save Customer</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<div class="wrapper">

    <!-- ── Header ──────────────────────────────────────────────────────── -->
    <div class="header">
        <h2>CRM - Customer Relationship Manager</h2>
    </div>

    <!-- ── Main Content ────────────────────────────────────────────────── -->
    <div class="content">

        <h3 class="form-title">Save Customer</h3>

        <!-- Hidden id field preserves id during update -->
        <form:form action="${pageContext.request.contextPath}/customer/saveCustomer"
                   modelAttribute="customer" method="POST" class="customer-form">

            <!-- Preserve customer id for updates -->
            <form:hidden path="id" />

            <!-- First Name -->
            <div class="form-group">
                <label for="firstName">First name:</label>
                <div class="input-wrapper">
                    <form:input path="firstName" id="firstName"
                                class="form-input" placeholder="Enter first name" />
                    <form:errors path="firstName" cssClass="error-msg" />
                </div>
            </div>

            <!-- Last Name -->
            <div class="form-group">
                <label for="lastName">Last name:</label>
                <div class="input-wrapper">
                    <form:input path="lastName" id="lastName"
                                class="form-input" placeholder="Enter last name" />
                    <form:errors path="lastName" cssClass="error-msg" />
                </div>
            </div>

            <!-- Email -->
            <div class="form-group">
                <label for="email">Email:</label>
                <div class="input-wrapper">
                    <form:input path="email" id="email" type="email"
                                class="form-input" placeholder="Enter email address" />
                    <form:errors path="email" cssClass="error-msg" />
                </div>
            </div>

            <!-- Submit Button -->
            <div class="form-group form-actions">
                <label></label>
                <div class="input-wrapper">
                    <button type="submit" class="btn btn-save">Save</button>
                </div>
            </div>

        </form:form>

        <hr class="divider" />

        <a href="${pageContext.request.contextPath}/customer/list" class="back-link">
            &#8592; Back to List
        </a>

    </div><!-- /content -->

</div><!-- /wrapper -->

</body>
</html>
