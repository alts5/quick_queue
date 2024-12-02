var pathToBackend = window.location.hostname;


$('#auth_form').on('submit', function(e) {
	e.preventDefault();
	$.ajax({
		url: 'http://' + pathToBackend + ':8080/authenticate',     
		method: 'POST',
		dataType: 'json',
		contentType: "application/x-www-form-urlencoded",
		data: $(this).serialize(),
		success: function(data){
			$('.error_base').css('display','none');
			sessionStorage.setItem('token', data["token"]);
			window.location.href="/workroom.html";
		},
		error: function(data){
			var error = data["responseText"] || "Произошла ошибка при обращении к API";
			$('.error_base').text(error);
			$('.error_base').css('display','block');
		}
	});
	return false;
});

function getUser() {
	var f = null;
	$.ajax({
		'async': false,
		url: 'http://' + pathToBackend + ':8080/userInfo',     
		method: 'GET',
		dataType: 'json',
		headers: {token : sessionStorage.getItem("token")}, 
		success: function(data){
			f = data;
		},
	});
	return f;
}