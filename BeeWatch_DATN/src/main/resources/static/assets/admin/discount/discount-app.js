app.controller("discount-ctrl", function($scope, $http) {
	//alert("discount-ctrl")
	$scope.items = [];
	$scope.form = [];
	$scope.cates = [];

	$scope.initialize = function() {
		//load product
		$http.get("/rest/discount").then(resp => {
			$scope.items = resp.data;
			$scope.reset();
		})

	}
	$scope.reset1 = function () {
		//alert("reset")
		$scope.form = {
			status: true,
		
		}
	}
	//khởi đầu
	$scope.initialize();
	//xóa form
	$scope.reset = function() {
		//alert("reset")
		$scope.form = {
			discountId: $scope.form.discountId,
			status: true,
		}
	}
	//hiện lên form
	$scope.edit = function(item) {
		//alert("edit")
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(1)").tab('show')
	}

	$scope.onkeyup = function() {
		var keyword = document.getElementById("keyword").value;
		alert("Tìm Kiếm:" + keyword)
	}
	//Thêm mới
	$scope.create = function() {
		var item = angular.copy($scope.form);
		//alert("Create " + item.percentDiscount)
        if (item.percentDiscount > 0 && item.percentDiscount % 1 ==0 ) {
            //alert("ok")
            if(item.percentDiscount > 30){
				alert("% giảm giá không được lớn hơn 30%");
				return false;
			}
            $http.post(`/rest/discount`, item).then(resp => {
                $scope.items.push(resp.data);
                $scope.initialize();
                alert("Thêm mới thành công!");
                $(".nav-tabs a:eq(0)").tab('show')
            }).catch(error => {
                alert("Thêm mới thất bại");
                console.log(error);
            });
        }else{
                alert("% giảm giá phải là số nguyên dương và lớn hơn 0");
        }
	}

	//cập nhật 
	$scope.update = function() {
		//alert("update ")
		var item = angular.copy($scope.form);
		//alert("id " + item.discountId); 
        if (item.percentDiscount > 0 && item.percentDiscount % 1 == 0) {
			
			if(item.percentDiscount > 30){
				alert("% giảm giá không được lớn hơn 30%");
				return false;
			}
            $http.put(`/rest/discount/${item.discountId}`, item).then(resp => {
                var index = $scope.items.findIndex(p => p.discountId == item.discountId);
                $scope.items[index] = item;
                alert("Cập nhật thành công");
                $(".nav-tabs a:eq(0)").tab('show')
            }).catch(error => {
                alert("Xảy ra lỗi trong quá trình cập nhật " + error)
                console.log("error", error);
            })
        } else {
            alert("% giảm giá phải là số nguyên dương và lớn hơn 0")
        }
		
	}
	//xóa 
	$scope.delete = function(item) {
		//alert("delete")
        var t = confirm("Bạn muốn xóa");
        if (t == false) {
            //alert("Không Xóa");
        } else {
            //alert("có xóa");
            /*$http.delete(`/rest/discount/${item.discountId}`).then(resp => {
                var index = $scope.items.findIndex(p => p.discountId == item.discountId);
                $scope.items.splice(index, 1);
                $scope.reset();
                alert("Xóa Thành Công");
            })
                .catch(error => {
                    alert("Lỗi xóa!")
                    console.log("Error", error);
                })*/
                
             $http.put(`/rest/discount/delete/${item.discountId}`, item).then(resp => {
                var index = $scope.items.findIndex(p => p.discountId == item.discountId);
                $scope.items[index] = item;
                alert("Xóa thành công");
                $scope.initialize();
                $(".nav-tabs a:eq(0)").tab('show')
            }).catch(error => {
                alert("Lỗi xóa " + error)
                console.log("error", error);
            })   

        }
	}

///tìm kiếm
    $scope.timKiem = function () {
        var name = document.getElementById("keyword").value;
        var trangThai = document.getElementById("trangThai").value;
        if (trangThai == "") {
            trangThai = null;
            //alert("Tìm Kiếm: " + name + " trang thai= " + trangThai)
            $http.get(`/rest/discount/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        } else {
            $http.get(`/rest/discount/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        }
       
    }
    $scope.genderStart = function (obj) {
        var trangThai = document.getElementById("trangThai").value;
        //alert("Trang thái " + trangThai )
        if (trangThai == "") {
            $http.get("/rest/discount").then(resp => {
                $scope.items = resp.data;
            })
        } else {
            $http.get(`/rest/discount/timKiem/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        }

    }
    
	$scope.pager = {
		page: 0,
		size: 10,
		get items() {
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
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
		}
	}
});