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
		"Заявители" : "../staffs",
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
			url: 'http://' + pathToBackend + ':8080/dashboardIndicators',     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data){
				$('#waiting').text(data["waiting"]);
				$('#invited').text(data["invited"]);
				$('#serviced').text(data["serviced"]);
				$('#reseted').text(data["reseted"]);
			},
			error: function(data) {
				$('.error_base').css("display", "block");
			}
		});
	}
});

function exitLK() {
	sessionStorage.clear();
	window.location.reload();
}
