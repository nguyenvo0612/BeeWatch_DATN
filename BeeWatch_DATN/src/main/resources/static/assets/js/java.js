function search() {
	var number = document.getElementById("keyword").value;
	window.location.assign("/itwatch/account/history/search?keyword" + "=" + number);
}