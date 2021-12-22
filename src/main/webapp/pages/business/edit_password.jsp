<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="io.Kilo" %>

${siteService.getBaseBit(Kilo.HEAD, design, business, request)}

<div class="section-wrapper">
	<div class="section">

		<c:if test="${not empty message}">
			<p class="notify">${message}</p>
		</c:if>

		<h1>Edit Password</h1>
		<p>You can update your password below!</p>

		<form action="${pageContext.request.contextPath}/${business.uri}/users/password/save" method="post">

			<input type="hidden" name="id" value="${user.id}"/>
			<input type="hidden" name="phone" value="${user.phone}"/>

			<div class="form-itemGroup">
				<label for="password">${user.phone}</label>
			</div>

			<div class="form-itemGroup">
				<label for="password">New Password</label>
				<input type="text" name="password" class="form-control" id="password" placeholder="Password" value="">
			</div>

			<div class="align-right" style="margin: 20px 0px 170px">
				<input type="submit" class="button green" id="reset-password" value="Reset Password"/>
			</div>

		</form>

	</div>
</div>

${siteService.getBaseBit(Kilo.BOTTOM, design, business, request)}

