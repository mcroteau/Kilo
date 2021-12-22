<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>New Sheet Import</h1>

<p>We hope this makes life easy. Here is a template for an Item Group to be used for an import.</p>

<form action="${pageContext.request.contextPath}/imports/item_groups/${business.id}" id="import-form"  enctype="multipart/form-data" method="post">

    <label>Item Image Files</label>
    <input type="file" name="media" multiple/>

    <div style="text-align: right;margin-top: 20px;">
        <input type="submit" value="Import Item Groups" class="button green" id="import-submit"/>
    </div>
</form>

<script>

    $(document).ready(function(){

        const $form = $('#import-form');
        let $submit = $('#import-submit');

        $submit.click(function(){
            this.disabled = true;
            this.value = "Importing Data...";
            $form.submit();
        })

    });
</script>
