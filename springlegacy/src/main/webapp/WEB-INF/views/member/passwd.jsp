<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
#contact {
	padding-top: 300px;
}
</style>
</head>
<body id="page-top">
	<!-- Navigation-->
	<jsp:include page="/WEB-INF/views/include/nav-bar.jsp" />

	<!-- Contact Section-->
	<section class="page-section" id="contact">
		<div class="container">

			<!-- Contact Section Form-->
			<div class="row justify-content-center">
				<!-- side-menu -->
				<jsp:include page="/WEB-INF/views/include/side-menu.jsp" />
				<div class="col-lg-6">
					<!-- Contact Section Heading-->
					<h2
						class="page-section-heading text-center text-uppercase text-secondary mb-0">비밀번호 변경</h2>
					<!-- Icon Divider-->
					<div class="divider-custom">
						<div class="divider-custom-line"></div>
						<div class="divider-custom-icon">
							<i class="fas fa-star"></i>
						</div>
						<div class="divider-custom-line"></div>
					</div>
					<form action="/member/passwd" method="POST" id="contactForm"
						data-sb-form-api-token="API_TOKEN">
						
						<!-- passwd input-->
						<div class="form-floating mb-3">
							<input class="form-control" id="passwd" type="password"
								name="passwd" data-sb-validations="required" /> <label
								for="passwd">비밀번호</label>
						</div>
						<div class="form-floating mb-3">
							<input class="form-control" id="newPasswd" type="password"
								name="newPasswd" data-sb-validations="required" /> <label
								for="newPasswd">새 비밀번호</label>
						</div>
						<div class="form-floating mb-3">
							<input class="form-control" id="newpasswdConfirm" type="password"
								name="newpasswdConfirm" data-sb-validations="required" /> <label
								for="newpasswdConfirm">새 비밀번호 확인</label>
						</div>
						
						<button class="btn btn-primary btn-xl" id="submitButton"
							type="submit">비밀번호 변경하기</button>
					</form>
				</div>
			</div>
		</div>
	</section>
	<!-- Footer-->
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />

	<!-- Bootstrap core JS-->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Core theme JS-->
	<script src="/resources/js/scripts.js"></script>
	<script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
</body>
</html>
