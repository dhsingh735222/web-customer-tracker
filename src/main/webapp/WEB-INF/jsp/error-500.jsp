<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Server Error - CRM</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<div class="wrapper">
    <div class="header"><h2>CRM - Customer Relationship Manager</h2></div>
    <div class="content">
        <div class="error-page">
            <h2>500 - Internal Server Error</h2>
            <p>Something went wrong on our end. Please try again later.</p>
            <a href="${pageContext.request.contextPath}/customer/list" class="btn btn-add">
                &#8592; Back to Home
            </a>
        </div>
    </div>
</div>
</body>
</html>
