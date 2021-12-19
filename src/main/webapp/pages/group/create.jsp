<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1 class="left-float">New Item Group</h1>

<a href="${pageContext.request.contextPath}/${business.id}/groups/options/create/" class="button orange right-float" style="margin-top:20px;">New Item Option</a>
<br class="clear"/>

<form action="${pageContext.request.contextPath}/" method="post">
    <label>Name</label>
    <input type="text" name="name" value="${group.name}"/>

    <label>Image</label>
    <input type="file" name="media"/>

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

    <div class="button-wrapper">
        <c:if test="${group == null}">
            <input type="submit" value="Start Item Group" class="button green" onclick="this.disabled=true;this.value='Saving Item...';this.form.submit();"/>
        </c:if>
        <c:if test="${group != null}">
            <input type="submit" value="Update Item Group" class="button green" onclick="this.disabled=true;this.value='Saving Item...';this.form.submit();"/>
        </c:if>
    </div>

</form>





<form action="${pageContext.request.contextPath}/groups/save/${business.id}" method="post">

    <style>
        #outer-wrapper{width:980px;}
        #content-wrapper{width: 100%;}
        #menu-wrapper{display:none;}
        .model-wrapper{padding:20px 0px; border-top:solid 1px #e4e8e9; border-bottom: solid 1px #e4e8e9;}
        input[type="file"]{width:182px;}
    </style>

    <div class="model-wrapper">

        <div class="left-float" style="width:45%;">
            <h3>Model Number #</h3>
            <input type="text" placeholder="Model No."/>
        </div>

        <div class="right-float"style="width:45%;">
            <h3>Weight</h3>
            <input type="text" placeholder="Weight" style="width:45%;"/>
        </div>

        <br class="clear"/>

        <div class="option-wrapper">

            <style>
                .grid input[type="text"]{padding:10px 0px !important}
            </style>

            <h3>Model Options</h3>
            <table class="grid">
                <tr>
                    <th>Label</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td><select><option>Option...</option></select></td>
                    <td>
                        <input type="text" placeholder="Value"/><br/>
                        <input type="file" name="media">
                    </td>
                </tr>
                <tr>
                    <td><select><option>Option...</option></select></td>
                    <td>
                        <input type="text" placeholder="Value"/><br/>
                        <input type="file" name="media">
                    </td>
                </tr>
                <tr>
                    <td><select><option>Option...</option></select></td>
                    <td>
                        <input type="text" placeholder="Value"/><br/>
                        <input type="file" name="media">
                    </td>
                </tr>
                <tr>
                    <td><select><option>Option...</option></select></td>
                    <td>
                        <input type="text" placeholder="Value"/><br/>
                        <input type="file" name="media">
                    </td>
                </tr>
                <tr>
                    <td><select><option>Option...</option></select></td>
                    <td>
                        <input type="text" placeholder="Value"/><br/>
                        <input type="file" name="media">
                    </td>
                </tr>
            </table>

        </div>


        <style>
            .option-wrapper{width:40%;float:left;margin:0px;}
            .pricing-wrapper{width:60%; float:right}
        </style>
        <div class="pricing-wrapper">
            <h3>Pricing Details</h3>
            <table class="grid">
                <tr>
                    <th>Pricing Header</th>
                    <td colspan="4"><input type="text" placeholder="Price/Carton"></td>
                </tr>
                <tr>
                    <th>Label</th>
                    <td><input type="text" placeholder="eg. 1"/></td>
                    <td><input type="text" placeholder="eg. 3"/></td>
                    <td><input type="text" placeholder="eg. 6"/></td>
                    <td><input type="text" placeholder="eg. 9+"/></td>
                </tr>
                <tr>
                    <th>$ Price</th>
                    <td><input type="text" placeholder="$ Price"/></td>
                    <td><input type="text" placeholder="$ Price"/></td>
                    <td><input type="text" placeholder="$ Price"/></td>
                    <td><input type="text" placeholder="$ Price"/></td>
                </tr>
                <tr>
                    <th>Resellers Price</th>
                    <td><input type="text" placeholder="Resellers $ Price"/></td>
                    <td><input type="text" placeholder="Resellers $ Price"/></td>
                    <td><input type="text" placeholder="Resellers $ Price"/></td>
                    <td><input type="text" placeholder="Resellers $ Price"/></td>
                </tr>
                <tr>
                    <th>Quantity</th>
                    <td><input type="text" placeholder="Quantity"/></td>
                    <td><input type="text" placeholder="Quantity"/></td>
                    <td><input type="text" placeholder="Quantity"/></td>
                    <td><input type="text" placeholder="Quantity"/></td>
                </tr>
            </table>

            <div class="button-wrapper">
                <input type="submit" class="button blue" value="Save">
                <input type="submit" class="button green" value="Add Model Number">
                <p class="align-right tiny">Add model saves and adds it to this item group</p>
            </div>
        </div>

        <br class="clear"/>

    </div>

</form>
