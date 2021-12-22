<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="io.Kilo" %>

<c:if test="${not empty message}">
    <p class="notify">${message}</p>
</c:if>

<h1 class="left-float">Sheet Imports</h1>
<a href="${pageContext.request.contextPath}/import/media/${business.id}" class="button retro right-float" style="margin-top:20px;">New  Import</a>
<br class="clear"/>

<p>What is an item image import? An item image import is an image import that can be easily converted
    into items.</p>

<c:if test="${sheetImports.size() > 0}">
    <table>
        <tr>
            <th>Date</th>
            <th></th>
        </tr>
        <c:forEach var="sheetImport" items="${sheetImports}" varStatus="idx">
            <tr>
                <td>${dataImport.dateImport}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/${business.id}/${sheetImport.id}/sheets/import/delete" method="post">
                        <input type="submit" value="Delete This Import" class="button remove"  onsubmit="return confirm('Are you sure you want to delete this import. The ItemGroup and models associated will be deleted as well.?');">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${dataImports == null || dataImports.size() == 0}">
    <p class="notify">No sheet imports yet! <a href="${pageContext.request.contextPath}/${business.id}/sheet/import" class="href-dotted">Google Sheet Import</a></p>
</c:if>

