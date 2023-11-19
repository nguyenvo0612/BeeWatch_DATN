function search() {
  var number = document.getElementById("keyword").value;
  window.location.assign(
    "/beewatch/account/history/search?keyword" + "=" + number
  );
}
