<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>List Customers</title>
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

        <!-- Flash success message -->
        <c:if test="${not empty successMessage}">
            <div class="alert-success">
                <span>${successMessage}</span>
                <button class="alert-close" onclick="this.parentElement.style.display='none'">&#x2715;</button>
            </div>
        </c:if>

        <!-- Toolbar: Add button + Search -->
        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/customer/showFormForAdd"
               class="btn btn-add">&#43; Add Customer</a>

            <form action="${pageContext.request.contextPath}/customer/search"
                  method="GET" class="search-form">
                <input type="text" name="searchName" placeholder="Search customers…"
                       value="${searchName}" class="search-input" />
                <button type="submit" class="btn btn-search">Search</button>
                <c:if test="${not empty searchName}">
                    <a href="${pageContext.request.contextPath}/customer/list"
                       class="btn btn-clear">Clear</a>
                </c:if>
            </form>
        </div>

        <!-- Customer Table -->
        <table class="customer-table">
            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty customers}">
                        <tr>
                            <td colspan="4" class="no-data">
                                No customers found.
                                <a href="${pageContext.request.contextPath}/customer/showFormForAdd">
                                    Add the first one!
                                </a>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="tempCustomer" items="${customers}" varStatus="status">
                            <c:url var="updateLink" value="/customer/showFormForUpdate">
                                <c:param name="customerId" value="${tempCustomer.id}" />
                            </c:url>
                            <c:url var="deleteLink" value="/customer/delete">
                                <c:param name="customerId" value="${tempCustomer.id}" />
                            </c:url>

                            <tr class="${status.index % 2 == 0 ? 'row-even' : 'row-odd'}">
                                <td>${tempCustomer.firstName}</td>
                                <td>${tempCustomer.lastName}</td>
                                <td>
                                    <a href="mailto:${tempCustomer.email}">${tempCustomer.email}</a>
                                </td>
                                <td class="action-cell">
                                    <a href="${updateLink}" class="action-link update-link">Update</a>
                                    &nbsp;|&nbsp;
                                    <a href="${deleteLink}" class="action-link delete-link"
                                       onclick="return confirmDelete('${tempCustomer.firstName} ${tempCustomer.lastName}')">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <!-- Row count -->
        <c:if test="${not empty customers}">
            <p class="row-count">Total: ${customers.size()} customer(s)</p>
        </c:if>

    </div><!-- /content -->

</div><!-- /wrapper -->

<!-- Delete confirmation dialog -->
<script>
function confirmDelete(customerName) {
    return confirm('Are you sure you want to delete customer: ' + customerName + '?\n\nThis action cannot be undone.');
}
</script>

</body>
</html>
