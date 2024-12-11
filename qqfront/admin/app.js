var pathToBackend = window.location.hostname;


$(document).ready(function() {
	$('.dateMask').mask('9999-99-99');
	var hrefs = {
		"Дашборд" : "../workroom", 
		"Типы документов" : "../doctypes", 
		"Категории" : "../categories", 
		"Сервисы" : "../services", 
		"Обслуживающие окна" : "../windows", 
		"Сотрудники" : "../staffs",
		"Параметры" : "../settings"
		};
	
	if ($('.username_header').length) $('.username_header').text(getUser()["name"]);
	if ($('.nav').length) {
		for (var elem in hrefs) {
			$('.nav').append('<a href = ' + hrefs[elem] + '>' + elem + '</a>');
		}
	}
	if ($('.info_block').length) {
		$.ajax({
			url: 'http://' + pathToBackend + ':8000/dashboard-indicators',     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data){
				$('#dayAccept').text(data["dayAccept"]);
				$('#dayReject').text(data["dayReject"]);
				$('#dayScale').text(data["dayScale"]);
				$('#totalAccept').text(data["totalAccept"]);
				$('#totalReject').text(data["totalReject"]);
				$('#totalScale').text(data["totalScale"]);
				for (var i=0; i < data["totalAgrofirms"].length; i++) {
					var day = 0;
					var day = 0;
					if (data["dayAgrofirms"] != null && data["dayAgrofirms"][i] != undefined) day = data["dayAgrofirms"][i]["summ"];					
					$('#agrofirmsStatic>tbody').append(
						"<tr><td>" + data["totalAgrofirms"][i]["vendor"] + "</td><td>" + day + "</td><td>" + data["totalAgrofirms"][i]["summ"] + "</td></tr>"
					);
				}
			},
			error: function(data) {
				$('.error_base').css("display", "block");
			}
		});
	}
});