app.controller("thongke-ctrl", function ($scope, $http) {
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
