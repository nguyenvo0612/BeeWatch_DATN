function remoteKiTuDB(obj){
	var string ="";
	var chuoiKt = $("#"+obj).val();
	string = chuoiKt.replace(/[&\/\\#,+()$~%'":*?<>{}]/g, '');
	$("#"+obj).val(string);
}

function toDateString(sDate, sFormatMark) {
//	    var sDatetemp
	    var values = sDate.split("-");
	    var yr_num;
	    var mo_num;
	    var day_num;
	    if ((sFormatMark.toUpperCase() == "DD/MM/YYYY")) {
	        yr_num = values[0];
	        mo_num = values[1];
	        day_num = values[2];
	    } 
	    return day_num+"/"+mo_num+"/"+yr_num;
	}
	
	function toDate(sDate, sFormatMark) {
//	    var sDatetemp
	    var values = sDate.split("/");
	    var yr_num;
	    var mo_num;
	    var day_num;
	    if ((sFormatMark.toUpperCase() == "DD/MM/YYYY") || (sFormatMark.toUpperCase() == "D")) {
	        yr_num = values[2];
	        mo_num = values[1] - 1;
	        day_num = values[0];
	    } else if ((sFormatMark.toUpperCase() == "MM/YYYY") || (sFormatMark.toUpperCase() == "M")) {
	        yr_num = values[1];
	        mo_num = values[0] - 1;
	        day_num = 1;
	    } else if ((sFormatMark.toUpperCase() == "Q/YYYY") || (sFormatMark.toUpperCase() == "Q")) {
	        yr_num = values[1];
	        values = values[0].split("Q");
	        mo_num = (values[1] * 3 - 2) - 1;
	        day_num = 1;
	    } else if ((sFormatMark.toUpperCase() == "YYYY") || (sFormatMark.toUpperCase() == "Y")) {
	        yr_num = values[0];
	        mo_num = 0;
	        day_num = 1;
	    }
	    return new Date(yr_num, mo_num, day_num);
	}
	
function checkDateHtai(obj){
	var date = new Date();
	var chuoiKt = $("#"+obj).val();
	var stringNgay = toDateString(chuoiKt,"DD/MM/YYYY");
	if(toDate(stringNgay,'DD/MM/YYYY') > date){
		alert("Nhập ngày không được lớn hơn ngày hiện tại");
		$("#"+obj).val("");
		return false;
	}
	
	
}

function checkTuNgayDenNgay(tu,den,check){
	//var date = new Date();
	var tuNgay = $("#"+tu).val();
	var denNgay = $("#"+den).val();
	
	if(tuNgay!="" && denNgay!=""){
		var stringtuNgay = toDateString(tuNgay,"DD/MM/YYYY");
		var stringdenNgay = toDateString(denNgay,"DD/MM/YYYY");
			if(check=="A"){
				if(toDate(stringtuNgay,'DD/MM/YYYY') > toDate(stringdenNgay,'DD/MM/YYYY')){
					alert("Nhập ngày bắt đầu không được lớn hơn ngày kết thúc");
					$("#"+tu).val("");
					return false;
				}
			}else if(check=="B"){
				if(toDate(stringtuNgay,'DD/MM/YYYY') > toDate(stringdenNgay,'DD/MM/YYYY')){
					alert("Nhập ngày kết thúc không được nhỏ hơn ngày bắt đầu");
					$("#"+den).val("");
					return false;
				}
			}
		
	}
}

