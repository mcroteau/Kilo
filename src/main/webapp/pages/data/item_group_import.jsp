<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>New Sheet Import</h1>

<p>We hope this makes life easy. Here is a template for an Item Group to be used for an import.</p>

<form action="${pageContext.request.contextPath}/imports/item_groups/${business.id}" method="post">

    <label>Spreadsheet Id</label>
    <span class="tiny">The Id of the spread sheet of interest. Can be found in the address
    bar of your web browser on Sheets.</span>
    <input type="text" name="spreadsheetId"/>

    <label>Starting Cell</label>
    <input type="text" name="startCell"/>

    <label>Ending Cell</label>
    <input type="text" name="endCell"/>

    <div style="text-align: right;margin-top: 20px;">
        <input type="submit" value="Import Item Group" class="button green" onclick="this.disabled=true;this.value='Getting data...';this.form.submit();"/>
    </div>
</form>
