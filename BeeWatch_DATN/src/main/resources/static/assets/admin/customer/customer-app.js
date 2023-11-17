app.controller("customer-ctrl", function ($scope, $http) {
	//alert("customer-ctrl")
	$scope.items = [];
	$scope.form = [];
	$scope.cates = [];

	var check ;
	$scope.initialize = function () {
		//load product
		$http.get("/rest/customer").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
						item.birthdate = new Date(item.birthdate)
						})
			$scope.reset();
		})

	}
	//khởi đầu
	$scope.initialize();
	//xóa form
	$scope.reset = function () {
		//alert("reset")
		$scope.form = {
			accountId: $scope.form.accountId,
			status: true,
			image: 'cloud-upload.jpg',
		}
	}
	$scope.reset1 = function () {
		//alert("reset")
		$scope.form = {
			status: true,
		
		}
	}
	//hiện lên form
	$scope.edit = function (item) {
		//alert("edit")
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(1)").tab('show')
	}

	$///tìm kiếm
    $scope.timKiem = function () {
        var name = document.getElementById("keyword").value;
        var trangThai = document.getElementById("trangThai").value;
        if (trangThai == "") {
            trangThai = null;
             if(name ==""){
				name ="null";
			}
            //alert("Tìm Kiếm: " + name + " trang thai= " + trangThai)
            $http.get(`/rest/customer/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        } else {
            $http.get(`/rest/customer/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        }
       
    }
    $scope.genderStart = function (obj) {
        var trangThai = document.getElementById("trangThai").value;
        //alert("Trang thái " + trangThai )
        if (trangThai == "") {
            $http.get("/rest/customer").then(resp => {
                $scope.items = resp.data;
            })
        } else {
            $http.get(`/rest/customer/timKiem/${trangThai}`).then(resp => {
                $scope.items = resp.data;
            })
        }

    }
    
	//Thêm mới
	$scope.create = function () {
		//alert("Create ")
		var item = angular.copy($scope.form);
		//alert("Tên Thương Hiệu " + item.name)
		$http.get(`/rest/customer/${item.name}`).then(resp =>{
			if (resp.data == '') {
				//alert("Thương hiệu mới")
				$http.post(`/rest/customer`, item).then(resp => {
					$scope.items.push(resp.data);
					$scope.initialize();
					alert("Thêm mới thành công!");
					$(".nav-tabs a:eq(0)").tab('show')
				}).catch(error => {
					alert("Thêm mới thất bại");
					console.log(error);
				});
			} else {
				alert("Đã có khách hàng này")
				console.log(resp.data)
			}
		})		


	}

	//cập nhật 
	$scope.update = function () {
		//alert("update ")
		var item = angular.copy($scope.form);
		
		var hoTen = document.getElementById("hoTen").value;
		if(hoTen.trim() =="" || hoTen ==null){
			alert("Họ tên không được để trống");
			return false;
		}
		var email = document.getElementById("email").value;
		if(email.trim() =="" || email ==null){
			alert("Email không được để trống");
			return false;
		}
		var pass = document.getElementById("pass").value;
		if(pass.trim() =="" || pass ==null){
			alert("Password không được để trống");
			return false;
		}
		var sdt = document.getElementById("sdt").value;
		if(sdt.trim() =="" || sdt ==null){
			alert("Số điện thoại không được để trống");
			return false;
		}
		var diaChi = document.getElementById("diaChi").value;
		if(diaChi.trim() =="" || diaChi ==null){
			alert("Địa chỉ không được để trống");
			return false;
		}
		
		if(sdt != ""){
			var vnf_regex = /((09|03|07|08|05)+([0-9]{8,10})\b)/g;
			if (vnf_regex.test(sdt) == false){
            	alert('Số điện thoại của bạn không đúng định dạng!');
            	return false;
        	}
		}
		if(sdt.length <10 || sdt.length >12){
			alert('Số điện thoại của bạn phải từ 10-12 số!');
	        return false;
		}
		if(sdt.substring(0,2) =="00"){
				alert('Số điện thoại của bạn không đúng định dạng!');
		        return false;
			}
		
		if(email!=""){
			var email =  document.getElementById('email');
			if(email.value.length > 0){
				var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
				var flag = re.test(email.value);
				if(flag == false){
					alert('Đề nghị NTT nhập email đúng định dạng quy định');
					setTimeout(function(){
						$(email).focus();
					}, 100);
					$(email).val('');
					return false;
				}
				//return flag;
			}
			
		}
		
		//alert("id " + item.discountId); 
		$http.put(`/rest/customer/${item.accountId}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.accountId == item.accountId);
			$scope.items[index] = item;
			alert("Cập nhật thành công");
			$(".nav-tabs a:eq(0)").tab('show')
		}).catch(error => {
			alert("Xảy ra lỗi trong quá trình cập nhật " + error)
			console.log("error", error);
		})
	}
	//xóa 
	$scope.delete = function (item) {
		//alert("delete")
		var t = confirm("Bạn muốn xóa");
		if (t == false) {
			//alert("Không Xóa");
		} else {
			//alert("có xóa");
			$http.delete(`/rest/customer/${item.accountId}`).then(resp => {
				var index = $scope.items.findIndex(p => p.accountId == item.accountId);
				$scope.items.splice(index, 1);
				$scope.reset();
				alert("Xóa Thành Công");
			})
				.catch(error => {
					alert("Lỗi xóa!")
					console.log("Error", error);
				})

		}
	}

	$scope.check = function (name){
		$http.get(`/rest/customer/${name}`).then(resp =>{
			if (resp.data == '') {
				//alert("Không tìm thấy  " + name)
				check = true;
			} else {
				//alert("Tìm thấy  " + name)
				check = false;
				console.log(resp.data)
			}
		})
	}


	//upload hình
	$scope.imageChanged = function (files) {
		//alert("hìn")
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/images', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.form.image = resp.data.name;
		}).catch(error => {
			alert("Lỗi upload hình" + error);
			console.log("Error", error);
		})
	}


	$scope.pager = {
		page: 0,
		size: 10,
		get items(){
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count(){
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first(){
			this.page = 0;
		},
		prev(){
			this.page--;
			if(this.page < 0){
				this.last();
			}
		},
		next(){
			this.page++;
			if(this.page >= this.count){
				this.first();
			}
		},
		last(){
			this.page = this.count - 1;
		}
	}
});