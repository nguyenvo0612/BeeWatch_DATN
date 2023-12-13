const bannerContent = document.querySelector(".banner-content");

setInterval(() => {
  bannerContent.style.animation = "none";
  setTimeout(() => {
    bannerContent.style.animation = "banner-animation 10s linear infinite";
  }, 100);
}, 5000);

document.addEventListener('DOMContentLoaded', function () {
  var formattedNumberElements = document.querySelectorAll('.formatted-number');

  formattedNumberElements.forEach(function (element) {
    var number = element.textContent;
    var formattedNumber = formatNumber(number);
    element.textContent = formattedNumber;
  });

  function formatNumber(number) {
    // Chia nhỏ thành từng nhóm 3 chữ số và nối chúng với dấu chấm
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
  }
});