<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="io.Kilo" %>

<style>
    #menu-wrapper{display:none;}
    #content-wrapper{width:100%;}
</style>

<c:if test="${not empty message}">
    <p class="notify">${message}</p>
</c:if>

<h3 class="left-float">Item Groups</h3>
<a href="${pageContext.request.contextPath}/imports/item_groups/new/${business.id}" class="button retro right-float" style="margin-top:20px;">New Import</a>
<br class="clear"/>

<c:if test="${ingests.size() > 0}">
    <c:forEach var="ingest" items="${ingests}" varStatus="idx">
        <c:forEach var="itemGroup" items="${ingest.itemGroups}" varStatus="idxn">
            <h1>${itemGroup.name}</h1>
            <table>
                <tr>
                    <th>Model Number</th>
                    <th>Quantity</th>
                    <th>Weight</th>
                    <c:forEach var="groupOption" items="${itemGroup.groupOptions}" varStatus="idxi">
                        <th>${groupOption.title}</th>
                    </c:forEach>
                    <c:forEach var="pricingOption" items="${itemGroup.pricingOptions}" varStatus="idxd">
                        <th>${pricingOption.description}</th>
                    </c:forEach>
                </tr>
                <c:forEach var="groupModel" items="${itemGroup.groupModels}" varStatus="idc">
                    <tr>
                        <td>${groupModel.modelNumber}</td>
                        <td>${groupModel.quantity}</td>
                        <td>${groupModel.weight}</td>
                        <c:forEach var="optionValue" items="${groupModel.groupValues}" varStatus="idxb">
                            <td>${optionValue.value}</td>
                        </c:forEach>
                        <c:forEach var="pricingValue" items="${groupModel.pricingValues}" varStatus="idxa">
                            <td>${pricingValue.price}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>

            <div class="button-wrapper">
                <form action="${pageContext.request.contextPath}/imports/item_groups/delete/${business.id}/${itemGroup.id}">
                    <input type="submit" value="Delete Above Item Group" class="button remove"/>
                </form>
            </div>
        </c:forEach>
    </c:forEach>
</c:if>

<c:if test="${ingests == null || ingests.size() == 0}">
    <p class="notify">No item group imports yet! <a href="${pageContext.request.contextPath}/imports/item_groups/new/${business.id}" class="href-dotted">New Item Group Import</a></p>
</c:if>

