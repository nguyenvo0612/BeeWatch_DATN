const bannerContent = document.querySelector(".banner-content");

setInterval(() => {
  bannerContent.style.animation = "none";
  setTimeout(() => {
    bannerContent.style.animation = "banner-animation 10s linear infinite";
  }, 100);
}, 5000);
