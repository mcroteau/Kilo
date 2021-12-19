<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<h1 class="left-float">New Item Group</h1>

<a href="${pageContext.request.contextPath}/${business.id}/groups/options/create/" class="button orange right-float" style="margin-top:20px;">New Item Option</a>
<br class="clear"/>

<form action="${pageContext.request.contextPath}/${business.id}/groups/save" method="post">

    <div class="left-float" style="width:75%;">

        <input type="hidden" name="businessId" value="${business.id}"/>

        <label>Name</label>
        <input type="text" name="name" value="${group.name}"/>

        <div class="left-float" style="width:140px;">
            <label>Categories</label>
            <span class="tiny">Active: Select 1 or many!<br/></span>
            <select name="categories" multiple>
                <c:forEach items="${categories}" var="category">
                    <c:if test="${activeCategories.contains(category)}">
                        <c:set var="selected" value="selected"/>
                    </c:if>
                    <c:if test="${!activeCategories.contains(category)}">
                        <c:set var="selected" value=""/>
                    </c:if>
                    <option value="${category.id}" ${selected}>${category.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="left-float" style="width:30%;">
            <label>Design<span class="information">&nbsp;&nbsp;</span></label><br/>
            <select name="designId">
                <c:forEach items="${designs}" var="active">
                    <c:if test="${active.id == item.designId}">
                        <c:set var="selected" value="selected"/>
                    </c:if>
                    <c:if test="${active.id != item.designId}">
                        <c:set var="selected" value=""/>
                    </c:if>
                    <option value="${active.id}" ${selected}>${active.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="left-float" style="width:250px;">
            <label>Image</label>
            <input type="file" name="media" style="width:100%;"/>
        </div>

    </div>

    <br class="clear"/>

    <div style="margin-top:40px;text-align: right">
        <input type="submit" name="save" value="Start Item Group" class="button green" onclick="this.disabled=true;this.value='Saving Item...';this.form.submit();"/>
    </div>

</form>






