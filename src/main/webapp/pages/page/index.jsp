<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="io.Kilo" %>

${siteService.getPageBit(Kilo.HEAD, page, business, request)}

<c:if test="${not empty message}">
    <p class="notify">${message}</p>
</c:if>

${page.content}

${siteService.getPageBit(Kilo.BOTTOM, page, business, request)}
