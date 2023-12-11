app.controller("thongke-ctrl", function ($scope, $http) {
  //alert("Thống kê")
  $scope.dashboard = [];
  $scope.total = [];
  $scope.account = [];
  $scope.order = [];
  $scope.product = [];
  $scope.accountTop = [];
  $scope.productASC = [];
  $scope.productSmall = [];

  $scope.cates = [];
  $scope.brand = [];

  $scope.initialize = function () {
    //load category
    $http.get("/rest/categories").then((resp) => {
      $scope.cates = resp.data;
    });
    //load Brand
    $http.get("/rest/brand").then((resp) => {
      $scope.brand = resp.data;
    });

    var namTke = "";
    var categoySelectVal = document.getElementById("categoySelect").value;
    var brandSelectVal = document.getElementById("brandSelect").value;
    var categoySelect = "a:null";
    var brandSelect = "a:null";

    if (namTke == "") {
      namTke = null;
    }
    /*if(namTke < 0){
                alert("Năm phải là số nguyên dương");
                document.getElementById("namTke").value = "";
                return false;
            }*/

    if (categoySelectVal != "") {
      categoySelect = document.getElementById("categoySelect").value.split(":");
    } else {
      categoySelect = categoySelect.split(":");
    }
    if (brandSelectVal != "") {
      brandSelect = document.getElementById("brandSelect").value.split(":");
    } else {
      brandSelect = brandSelect.split(":");
    }

    $http
      .get(
        `/rest/dashboard/searchDthu/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.productASC = resp.data;
        var tongAll = 0;
        $scope.productASC.forEach((item) => {
          var totalAsc = $scope.formatSoNguyenDuong(item.total);
          tongAll += item.total;
          item.total = totalAsc;
        });
        $scope.total = $scope.formatSoNguyenDuong(tongAll);
      });

    $http
      .get(
        `/rest/dashboard/searchDthu/hoaDon/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.order = resp.data;
      });

    $http
      .get(
        `/rest/dashboard/searchDthu/account/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.account = resp.data;
      });

    $http.get("/rest/dashboard/totalProduct").then((resp) => {
      $scope.product = resp.data;
      $scope.product.forEach((item) => {
        var pricePR = $scope.formatSoNguyenDuong(item.price);
        item.price = pricePR;
      });
    });
    $http.get("/rest/dashboard/totalAccountTop").then((resp) => {
      $scope.accountTop = resp.data;
    });

    $http.get("/rest/dashboard/totalProdctASC").then((resp) => {
      $scope.productASC = resp.data;
      $scope.productASC.forEach((item) => {
        var totalAsc = $scope.formatSoNguyenDuong(item.total);
        item.total = totalAsc;
      });
    });

    $http.get("/rest/dashboard/totalProductSmall").then((resp) => {
      $scope.productSmall = resp.data;
      $scope.productSmall.forEach((item) => {
        var totalSmall = $scope.formatSoNguyenDuong(item.total);
        item.total = totalSmall;
      });
    });
  };
  $scope.initialize();

  $scope.searchdThu = function () {
    var namTke = document.getElementById("namTke").value;
    var categoySelectVal = document.getElementById("categoySelect").value;
    var brandSelectVal = document.getElementById("brandSelect").value;
    var categoySelect = "a:null";
    var brandSelect = "a:null";

    if (namTke == "") {
      namTke = null;
    }
    /*if(namTke < 0){
                alert("Năm phải là số nguyên dương");
                document.getElementById("namTke").value = "";
                return false;
            }*/

    if (categoySelectVal != "") {
      categoySelect = document.getElementById("categoySelect").value.split(":");
    } else {
      categoySelect = categoySelect.split(":");
    }
    if (brandSelectVal != "") {
      brandSelect = document.getElementById("brandSelect").value.split(":");
    } else {
      brandSelect = brandSelect.split(":");
    }

    $http
      .get(
        `/rest/dashboard/searchDthu/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.productASC = resp.data;
        var tongAll = 0;
        $scope.productASC.forEach((item) => {
          var totalAsc = $scope.formatSoNguyenDuong(item.total);
          tongAll += item.total;
          item.total = totalAsc;
        });
        $scope.total = $scope.formatSoNguyenDuong(tongAll);
      });

    $http
      .get(
        `/rest/dashboard/searchDthu/hoaDon/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.order = resp.data;
      });

    $http
      .get(
        `/rest/dashboard/searchDthu/account/${namTke}/${categoySelect[1]}/${brandSelect[1]}`
      )
      .then((resp) => {
        $scope.account = resp.data;
      });
  };

  $scope.formatSoNguyenDuong = function (value) {
    var strvalue;
    strvalue = value;
    //var str = strvalue.split('.');
    var num;
    var val = strvalue;
    num = strvalue.toString().replace(/\$|\./g, "");
    if (num.length > 15) num = num.substring(0, 15);
    if (!$scope.IsNumeric(num)) num = "0";
    sign = num == (num = Math.abs(num));
    num = Math.floor(num * 100 + 0.50000000001);
    num = Math.floor(num / 100).toString();
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
      num =
        num.substring(0, num.length - (4 * i + 3)) +
        "." +
        num.substring(num.length - (4 * i + 3));
    //return (((sign)?'':'-') + num);
    value = (sign ? "" : "-") + num;
    return value;
  };

  $scope.IsNumeric = function (sText) {
    var ValidChars = "0123456789.,-";
    var IsNumber = true;
    var Char;

    for (var i = 0; i < sText.length && IsNumber == true; i++) {
      Char = sText.charAt(i);
      if (ValidChars.indexOf(Char) == -1) {
        IsNumber = false;
        break;
      }
    }
    return IsNumber;
  };

  $scope.getVal = function (num) {
    if (num == "" || $scope.checkNumeric(num)) {
      return "";
    } else {
      return num;
    }
  };

  $scope.checkNumeric = function (sText) {
    var ValidChars = "0123456789";
    var IsNumber = true;
    if (ValidChars.indexOf(sText) != -1) {
      IsNumber = false;
    }
    return IsNumber;
  };

  //phân trang productASC
  $scope.pager = {
    page: 0,
    size: 5,
    get itemsProduct() {
      var start = this.page * this.size;
      return $scope.productASC.slice(start, start + this.size);
    },
    get count() {
      return Math.ceil((1.0 * $scope.productASC.length) / this.size);
    },

    first() {
      this.page = 0;
    },
    prev() {
      this.page--;
      if (this.page < 0) {
        this.last();
      }
    },
    next() {
      this.page++;
      if (this.page >= this.count) {
        this.first();
      }
    },
    last() {
      this.page = this.count - 1;
    },
  };

  //phân trang productSmall
  $scope.pagerSmall = {
    page: 0,
    size: 5,
    get itemsProduct() {
      var start = this.page * this.size;
      return $scope.productSmall.slice(start, start + this.size);
    },
    get count() {
      return Math.ceil((1.0 * $scope.productSmall.length) / this.size);
    },

    first() {
      this.page = 0;
    },
    prev() {
      this.page--;
      if (this.page < 0) {
        this.last();
      }
    },
    next() {
      this.page++;
      if (this.page >= this.count) {
        this.first();
      }
    },
    last() {
      this.page = this.count - 1;
    },
  };

  $scope.checkKyThangInput = function (obj) {
    var thangNam = document.getElementById(obj);
    validDateFormat(thangNam, "mm/yyyy");
  };

  function validDateFormat(dateField, dateFormat) {
    var dt = dateField;

    var returndate;

    if (dt.value == null || dt.value == "") return true;

    if (dateFormat.toUpperCase() == "DD/MM/YYYY") {
      returndate = isDate(dt.value);
    } else if (dateFormat.toUpperCase() == "MM/YYYY") {
      returndate = isMonth(dt.value);
    } else if (dateFormat.toUpperCase() == "YYYY") {
      returndate = isYear(dt.value);
    } else {
      returndate = false;
    }

    if (returndate == false) {
      alert(
        "Đề nghị NNT nhập giá trị dạng (" + dateFormat + ") cho trường này"
      );
      dt.value = "";
      dt.focus();
      return false;
    } else {
      dt.value = returndate;
      return returndate;
    }
  }
  var strSeparatorArray = new Array("-", " ", "/", ".");
  var minYear = 1000;
  var maxYear = 9999;
  function isMonth(dtStr) {
    var dtCh = "*";
    if (dtStr == "") return "";

    for (
      var intElementNr = 0;
      intElementNr < strSeparatorArray.length;
      intElementNr++
    ) {
      if (dtStr.indexOf(strSeparatorArray[intElementNr]) != -1)
        dtCh = strSeparatorArray[intElementNr];
    }

    if (dtCh != "*") {
      //neu co ky hieu phan cach
      var pos1 = dtStr.indexOf(dtCh);

      var strMonth = dtStr.substring(0, pos1);
      var strYear = dtStr.substring(pos1 + 1);
    } //khong co ky hieu phan cach
    else {
      if (dtStr.length > 3) {
        strMonth = dtStr.substr(0, 2);
        strYear = dtStr.substr(2);
      } else return false;
    }

    if (!isInteger(strYear) || !isInteger(strMonth)) return false;
    strYr = strYear;

    if (strMonth.charAt(0) == "0" && strMonth.length > 1)
      strMonth = strMonth.substring(1);

    for (var i = 1; i <= 3; i++) {
      if (strYr.charAt(0) == "0" && strYr.length > 1)
        strYr = strYr.substring(1);
    }

    month = parseInt(strMonth);
    year = parseInt(strYr);

    if (strMonth.length < 1 || month < 1 || month > 12) {
      return false;
    }

    if (year < 50) year += 2000;
    if (year > 50 && year < 1000) year += 1900;
    if (strYear.length < 1 || year == 0 || year < minYear || year > maxYear) {
      return false;
    }
    if (month < 10) month = "0" + month;
    return "" + month + "-" + year;
  }

  function isInteger(s) {
    var isInteger = RegExp(/^[0-9]+$/);
    return isInteger.test(s);
  }

  // hoa don
  $scope.order2 = [];
  $scope.orderDetail = [];
  $scope.total2 = [];
  $scope.form = {};
  $scope.choDuyet = [];
  $scope.dangXuLy = [];
  $scope.dangGiao = [];
  $scope.hoanThanh = [];
  $scope.daHuy = [];
  $scope.yeuCauHoan = [];
  $scope.donHoan = [];
  $scope.initialize = function () {
    $http.get("/rest/orders/all").then((resp) => {
      $scope.order2 = resp.data;
      $scope.total2 = resp.data.length;
      console.log(resp.data);
    });
  };
  $scope.initialize();
  $scope.updateUp = function (item) {
    var t = confirm(
      "Bạn muốn Chuyển trạng thái đơn hàng " + item.orderId + " không?"
    );
    if (t == false) {
    } else {
      $http.put(`/rest/orders/up/${item.orderId}`, item).then((resp) => {
        $scope.initialize();
        alert("Đơn hàng đã được chuyển trạng thái");
        // $window.location.reload();
        location.reload(true);
      });
    }
  };
  $scope.confirmRefund = function (item) {
    var t = confirm("Bạn muốn xác nhận hoàn đơn " + item.orderId + " không?");
    if (t == false) {
    } else {
      $http.put(`/rest/orders/up/${item.orderId}`, item).then((resp) => {
        $scope.initialize();
        alert("Đơn hàng đã được chuyển trạng thái");
        // $window.location.reload();
        location.reload(true);
      });
    }
  };

  $scope.updateDown = function (item) {
    var t;
    if (item.status == 1) {
      t = confirm("Bạn muốn hủy đơn hàng này?");
    }
    if (t == false) {
    } else {
      $http.put(`/rest/orders/down/${item.orderId}`, item).then((resp) => {
        $scope.initialize();
        alert("Đơn hàng đã được chuyển trạng thái");
        // $window.location.reload();
        location.reload(true);
      });
    }
  };
  $scope.updateClose = function (item) {
    var t = confirm("Bạn muốn hủy đơn hàng?");
    if (t == false) {
    } else {
      $http.put(`/rest/orders/close/${item.orderId}`, item).then((resp) => {
        $scope.initialize();
        alert("Đơn hàng đã được hủy thành công");
        location.reload(true);
      });
    }
  };
  $scope.sayByeRefund = function (item) {
    var t = confirm("Bạn từ chối hoàn đơn hàng?");
    if (t == false) {
    } else {
      $http
        .put(`/rest/orders/saybyerefund/${item.orderId}`, item)
        .then((resp) => {
          $scope.initialize();
          alert("Đơn hàng đã được từ chối hoàn lại");
          location.reload(true);
        });
    }
  };
  $scope.findId = function (item) {
    $http.get(`/rest/orders/${item.orderId}`, item).then((resp) => {
      $scope.orderDetail = resp.data;
      //alert(orderDetail)
    });
  };

  $scope.timKiemDh = function (item) {
    var tenTk = document.getElementById("tenTk").value;
    var ngayTk = document.getElementById("ngayTk").value;
    var tthaiTk = document.getElementById("tthaiTk").value;
    if (tenTk == "") {
      tenTk = null;
    }
    if (ngayTk == "") {
      ngayTk = null;
    }
    if (tthaiTk == "") {
      tthaiTk = null;
    }
    $http
      .get(`/rest/orders/searchDH/${tenTk}/${ngayTk}/${tthaiTk}`, item)
      .then((resp) => {
        $scope.order2 = resp.data;
      });
  };

  $scope.pager = {
    page: 0,
    size: 10,
    get items() {
      var start = this.page * this.size;
      return $scope.order2.slice(start, start + this.size);
    },
    get count() {
      return Math.ceil((1.0 * $scope.order2.length) / this.size);
    },
    first() {
      this.page = 0;
    },
    prev() {
      this.page--;
      if (this.page < 0) {
        this.last();
      }
    },
    next() {
      this.page++;
      if (this.page >= this.count) {
        this.first();
      }
    },
    last() {
      this.page = this.count - 1;
    },
  };

  //cho duyet

  $http.get("/rest/dashboard/choDuyet").then((resp) => {
    $scope.choDuyet = resp.data;
  });

  //dang xu lý
  $http.get("/rest/dashboard/dangXuLy").then((resp) => {
    $scope.dangXuLy = resp.data;
  });
  //dang giao
  $http.get("/rest/dashboard/dangGiao").then((resp) => {
    $scope.dangGiao = resp.data;
  });

  //hoan thanh
  $http.get("/rest/dashboard/hoanThanh").then((resp) => {
    $scope.hoanThanh = resp.data;
  });
  //da huy
  $http.get("/rest/dashboard/daHuy").then((resp) => {
    $scope.daHuy = resp.data;
  });
  //yeu cau hoan
  $http.get("/rest/dashboard/yeuCauHoan").then((resp) => {
    $scope.yeuCauHoan = resp.data;
  });
  //don hoan
  $http.get("/rest/dashboard/donHoan").then((resp) => {
    $scope.donHoan = resp.data;
  });

  $scope.showErrorDetails = function (item) {
    // Đặt đối tượng hiện tại để hiển thị trong modal
    $scope.currentSelectedItem = item;

    // Mở modal
    $("#errorModal").modal("show");
  };
});
