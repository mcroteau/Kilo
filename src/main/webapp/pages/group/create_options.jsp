<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty message}">
    <p class="notify">${message}</p>
</c:if>

<h1>New Item Group Option</h1>
<p>Item Group Options are attributes you place on
    an item like Size, Width, etc. Used when creating
    an Item Group.</p>

<form action="${pageContext.request.contextPath}/${business.id}/groups/options/save" method="post">

    <input type="hidden" name="businessId" value="${business.id}"/>

    <div class="left-float" style="width:270px;">
        <label>Title</label>
        <input type="text" name="title" value=""/>
    </div>

    <div class="left-float" style="margin-left:30px;margin-top:40px;">
        <input type="submit" value="Save Group Option" class="button green" onclick="this.disabled=true;this.value='Saving Item Group Option...';this.form.submit();"/>
    </div>

    <br class="clear"/>

</form>

<h3>Available Group Options</h3>

<style>
    .group-options li{list-style: disc; margin: 20px 0px 10px 70px;}
</style>
<c:if test="${groupOptions.size() > 0}">
    <ul class="group-options">
        <c:forEach items="${groupOptions}" var="groupOption">
            <li>${groupOption.title}</li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${groupOptions.size() == 0}">
    <p class="notify">No group options created yet.</p>
</c:if>
