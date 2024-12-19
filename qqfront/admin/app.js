var pathToBackend = window.location.hostname;
var pn = $('#page_name').val()


function errorPushWindow(msg) {
	if (msg.responseJSON) alert(msg.responseJSON["detail"]);
	else alert("При взаимодействии с сервером произошла ошибка. Проверьте поля ввода данных");
}

function exitLK() {
	sessionStorage.clear();
	window.location.reload();
}

function checker() {
	if (!getUser()) { 
		sessionStorage.clear(); 
		window.location.href = '/'; 
	}
	if (getUser()['is_admin'] != 'true') {
		window.location.href = '/workroom'; 
	}
}

function singleCheckerMode() {
	if (getSystemMode() == 'single') {
		window.location.href = '/workroom'; 
	}
}

function modal_window_controller(elem, action, row=null) {
	if (action == 1) {
		$('.modal_wrap_lk').css("display", "block");
		$('#' + elem).css("display", "block");
		if ($('#' + elem + ' input[name=id]').length) {
			$('#' + elem + ' input[name=id]').val(row);
		}
	}
	else {
		$('.modal_wrap_lk').css("display", "none");
		$('#' + elem).css("display", "none");
	}
}

$('#create_form').on('submit', function(e) {
	e.preventDefault();
	$('form [name="token"]').val(sessionStorage.getItem('token'));
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/add' + pn,     
			method: 'POST',
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded",
			data: $(this).serialize(),
			success: function(data){
				window.location.reload();
			},
			error: function(data) {
				errorPushWindow(data);
			}
		});
	return false;
});

$('#settings_form').on('submit', function(e) {
	e.preventDefault();
	$('form [name="token"]').val(sessionStorage.getItem('token'));
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/update' + pn,     
			method: 'POST',
			dataType: 'json',
			contentType: "application/x-www-form-urlencoded",
			data: $(this).serialize(),
			success: function(data){
				window.location.reload();
			},
			error: function(data) {
				errorPushWindow(data);
			}
		});
	return false;
});


function delete_position(id, page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/delete' + page,   
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token'), id : id },
			success: window.location.reload()
		});
}

function hide_position(id, page=pn) {
	$.ajax({
		url: 'http://' + pathToBackend + ':8080/hide' + page,     
		method: 'POST',
		dataType: 'json',
		data: {token: sessionStorage.getItem('token'), id: id},
		success: function(data){
			window.location.reload();
		},
		error: function(data) {
			errorPushWindow(data);
		}
	});
}

function getSystemMode() {
	var f = null;
	$.ajax({
		'async': false,
		url: 'http://' + pathToBackend + ':8080/systemMode',     
		method: 'GET',
		success: function(data){
			f = data;
		},
	});
	return f;
}


function getDoctypesTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/show' + page,     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#doctypesListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						var id = data[i]["id"];
						var description = data[i]["description"] || "-";
						var warnIcon = "<td></td>";
						
						if (data[i]["stat"] != "Доступно") {
							statIcon = "<td><img src = 'design/hidden.svg' title = 'Отобразить позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						else {
							statIcon = "<td><img src = 'design/showed.svg' title = 'Cкрыть позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						
						$('#doctypesListTable>tbody').append(
							"<tr><td>" + id + "</td><td>" + data[i]["label"] + "</td><td>"
							+ description + "</td><td>" + data[i]["stat"] + "</td>"
							+ statIcon
							+ "<td><img src = 'design/reject.svg' title = 'Удалить позицию' onclick = 'delete_position(\""+ id + "\")'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

function getCategoriesTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/show' + page,     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#categoriesListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						var id = data[i]["id"];
						var description = data[i]["description"] || "-";
						var warnIcon = "<td></td>";
						
						if (data[i]["stat"] != "Доступно") {
							statIcon = "<td><img src = 'design/hidden.svg' title = 'Отобразить позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						else {
							statIcon = "<td><img src = 'design/showed.svg' title = 'Cкрыть позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
		
						$('#categoriesListTable>tbody').append(
							"<tr><td>" + id + "</td><td>" + data[i]["name"] + "</td><td>"
							+ description + "</td><td>" + data[i]["stat"] + "</td>"
							+ statIcon
							+ "<td><img src = 'design/reject.svg' title = 'Удалить позицию' onclick = 'delete_position(\""+ id + "\")'></td>"
							+ "<td><img src = 'design/services.svg' title = 'Список доступных сервисов' onclick = 'categories_avails_services(\""+ id + "\")'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

function getServicesTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/show' + page,     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#servicesListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						var id = data[i]["id"];
						var description = data[i]["description"] || "-";
						var warnIcon = "<td></td>";
						
						if (data[i]["stat"] != "Доступно") {
							statIcon = "<td><img src = 'design/hidden.svg' title = 'Отобразить позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						else {
							statIcon = "<td><img src = 'design/showed.svg' title = 'Cкрыть позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
		
						$('#servicesListTable>tbody').append(
							"<tr><td>" + id + "</td><td>" + data[i]["name"] + "</td><td>"
							+ description + "</td><td>" + data[i]["stat"] + "</td>"
							+ statIcon
							+ "<td><img src = 'design/reject.svg' title = 'Удалить позицию' onclick = 'delete_position(\""+ id + "\")'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

function getWindowsTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/show' + page,     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#windowsListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						var id = data[i]["id"];
						var warnIcon = "<td></td>";
						
						if (data[i]["stat"] != "Доступно") {
							statIcon = "<td><img src = 'design/hidden.svg' title = 'Отобразить позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						else {
							statIcon = "<td><img src = 'design/showed.svg' title = 'Cкрыть позицию' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
		
						$('#windowsListTable>tbody').append(
							"<tr><td>" + id + "</td><td>" + data[i]["label"] + "</td>"
							+"<td>" + data[i]["stat"] + "</td>"
							+ statIcon
							+ "<td><img src = 'design/reject.svg' title = 'Удалить позицию' onclick = 'delete_position(\""+ id + "\")'></td>"
							+ "<td><img src = 'design/services.svg'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

function getStaffTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/show' + page,     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#staffListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						var id = data[i]["id"];
						var warnIcon = "<td></td>";
						var admin_stat = "Нет";
						
						if (data[i]["is_admin"] === 'true') {
							console.log(1);
							admin_stat = "Да";
						}
						
						if (data[i]["stat"] != "Активен") {
							statIcon = "<td><img src = 'design/hidden.svg' title = 'Заблокировать' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
						else {
							statIcon = "<td><img src = 'design/showed.svg' title = 'Активировать' onclick = 'hide_position(\""+ id + "\")'></td>"
						}
		
						$('#staffListTable>tbody').append(
							"<tr><td>" + id + "</td><td>" + data[i]["name"] + "</td>"
							+ "<td>" + data[i]["login"] + "</td>"
							+ "<td>" + admin_stat + "</td>"
							+"<td>" + data[i]["stat"] + "</td>"
							+ statIcon
							+ "<td><img src = 'design/reject.svg' title = 'Удалить пользователя' onclick = 'delete_position(\""+ id + "\")'></td>"
							+ "<td><img src = 'design/services.svg'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

function getSettings() {
	$.ajax({
		url: 'http://' + pathToBackend + ':8080/system_settings',     
		method: 'GET',
		dataType: 'json',
		data: { token : sessionStorage.getItem('token') },
		success: function(data) {
			if (data != undefined) {
				$('#settings_form [name=systemMode]').val(data["systemMode"]);
				$('#settings_form [name=startTime]').val(data["startTime"]);
				$('#settings_form [name=endTime]').val(data["endTime"]);
				$('#settings_form [name=footerName]').val(data["footerName"]);
				$('#settings_form [name=logoPath]').val(data["logoPath"]);
			}
		}
	});
}

function getQueueTable(page=pn) {
	$.ajax({
			url: 'http://' + pathToBackend + ':8080/queueAdmin',     
			method: 'GET',
			dataType: 'json',
			data: { token : sessionStorage.getItem('token') },
			success: function(data) {
				if (data != undefined) {
					$('#queueListTable>tbody').empty();
					for (var i = 0; i < data.length; i++) {
						console.log(data);
						var id = data[i]["id"];
						var stat = "";
						
						switch(data[i]["stat"]) {
							case 'Ожидает':
								stat = "<td><div class = 'stat' data-type='wait'>Ожидает</div></td>"
								break;
							case 'Приглашен':
								stat = "<td><div class = 'stat' data-type = 'invite'>Приглашён</div></td>"
								break;
						}	
											
						var gh = data[i]["service"] || '-';
						var ti = data[i]["time"].slice(0,-1).split("T");

						$('#queueListTable>tbody').append(
							"<tr>"
							+ "<td>" + id + "</td>"
							+ "<td>" + data[i]["fio"] + "</td>"
							+ "<td>" + gh + "</td>"
							+ "<td>" + ti[0] + " " + ti[1] + "</td>"
							+ stat
							+ "<td><img title = 'Пригласить' src = 'design/invite.svg'></td>"
							+ "<td><img title = 'Перенаправить'  src = 'design/redirect.svg'></td>"
							+ "<td><img title = 'Снять с очереди'  src = 'design/stop.svg'></td>"
							+ "</tr>"
						);
					}
				}
			}
		});
}

$(document).ready(function() {
	$('.dateMask').mask('9999-99-99');
	var hrefs = {
		"Дашборд" : "../workroom", 
		"Типы документов" : "../doctypes", 
		"Категории" : "../categories", 
		"Сервисы" : "../services", 
		"Обслуживающие окна" : "../windows", 
		"Пользователи" : "../staff",
		"Заявители" : "../applicants",
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
	if ($('#doctypesListTable').length) {
		getDoctypesTable();
	}
	if ($('#categoriesListTable').length) {
		getCategoriesTable();
	}
	if ($('#servicesListTable').length) {
		getServicesTable();
	}
	if ($('#windowsListTable').length) {
		getWindowsTable();
	}
	if ($('#staffListTable').length) {
		getStaffTable();
	}
	if ($('#settings_form').length) {
		getSettings();
	}
	if ($('#queueListTable').length) {
		getQueueTable();
	}
	});