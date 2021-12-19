<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1 class="left-float">Create Group</h1>

<a href="${pageContext.request.contextPath}/options/create/${business.id}" class="button orange right-float" style="margin-top:20px;">New Item Option</a>
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
        .option-wrapper{
            width:130px;
            margin-left: 20px;
        }
        #outer-wrapper{width:980px;}
        #content-wrapper{width: 100%;}
        #menu-wrapper{display:none;}
        .model-wrapper{border:solid 1px #e4e8e9; padding:20px;}
        /*.grid td{padding:0px;}*/
        /*.grid input[type="text"]{font-size: 13px !important;border-radius: 0px !important;border:none !important;background-color: #fff}*/
        /*.grid select{font-size: 13px;padding: 10px 0px !important;background: #f0f4f5;width:100%;text-align:center;border-radius: 0px !important;border: none;background-color: #fff}*/
        /*input[type="text"]::placeholder{font-size:13px !important;}*/
    </style>

    <div class="model-wrapper">

        <h2 class="left-float">Add Model No/Item Group</h2>
        <a href="${pageContext.request.contextPath}/options/create/${business.id}" class="button orange right-float" style="margin-top:20px;">New Item Option</a>
        <br class="clear"/>

        <label>Model Number #</label>
        <input type="text" placeholder="Model No." style="width:45%;background-color: #fff;"/><br/><br/>

        <table class="grid">
            <tr>
                <th>Label</th>
                <td><select><option>Option...</option><option>Option2...</option><option>v...</option></select></td>
                <td><select><option>Option...</option></select></td>
                <td><select><option>Option...</option></select></td>
            </tr>
            <tr>
                <th>Value</th>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Value"/></td>
            </tr>
        </table>


        <label>Pricing Details</label>
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
                <td><input type="text" placeholder="$Price"/></td>
                <td><input type="text" placeholder="$Price"/></td>
                <td><input type="text" placeholder="$Price"/></td>
                <td><input type="text" placeholder="$Price"/></td>
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



    <div class="option-wrapper">

        <style>
            .grid input[type="text"]{padding:10px 0px !important}
        </style>

        <table class="grid">
            <tr>
                <th>Label</th>
                <th>Value</th>
                <th>Weight (oz)</th>
            </tr>
            <tr>
                <td><select><option>Option...</option></select></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Weight"/></td>
            </tr>
            <tr>
                <td><select><option>Option...</option></select></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Weight"/></td>
            </tr>
            <tr>
                <td><select><option>Option...</option></select></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Weight"/></td>
            </tr>
            <tr>
                <td><select><option>Option...</option></select></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Weight"/></td>
            </tr>
            <tr>
                <td><select><option>Option...</option></select></td>
                <td><input type="text" placeholder="Value"/></td>
                <td><input type="text" placeholder="Weight"/></td>
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

</form>
