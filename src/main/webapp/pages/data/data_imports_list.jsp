<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="io.Kilo" %>

<c:if test="${not empty message}">
    <p class="notify">${message}</p>
</c:if>

<h1>Data Imports</h1>

<a href="${pageContext.request.contextPath}/import/media/${business.id}" class="button remove" style="margin-top:20px;">New Item Image Import</a>&nbsp;&nbsp;
<a href="${pageContext.request.contextPath}/imports/item_groups/new/${business.id}" class="button orange" style="margin-top:20px;">New Item Group Import</a>
<br class="clear"/>

<p>Item import allows gives you the ability to easily import items using images.</p>
<p>Item groups are what they sound like, groups of items. Meant for wholesale and distributors.</p>
