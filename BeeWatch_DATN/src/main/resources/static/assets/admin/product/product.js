app.controller("product-ctrl", function ($scope, $http) {
    //alert("product")
    $scope.itemsProduct = [];
    $scope.formProduct = [];

    $scope.cates = [];
    $scope.brand = [];
    $scope.size = [];
    $scope.glass = [];
    $scope.strap = [];
    $scope.water = [];
    $scope.shell = [];


    $scope.itemsImagePr = []
    $scope.formImagePr = [];

    var productId;
    $scope.initialize = function () {
        //load product
        $http.get("/rest/products").then(resp => {
            $scope.itemsProduct = resp.data;
            $scope.itemsProduct.forEach(item => {
                item.createDate = new Date(item.createDate)
            })
            $scope.reset();
        })
        //load category
        $http.get("/rest/categories/getAllStatus").then(resp => {
            $scope.cates = resp.data;
        })
        //load Brand
        $http.get("/rest/brand/getAllStatus").then(resp => {
            $scope.brand = resp.data;
        })
        $http.get(`/rest/ImageProduct`).then(resp => { 
            $scope.itemsImagePr = resp.data;
        })
        
        //load size
        $http.get("/rest/size/getAllStatus").then(resp => {
            $scope.size = resp.data;
        })
        
        //load glass
        $http.get("/rest/glass/getAllStatus").then(resp => {
            $scope.glass = resp.data;
        })
        
        //load strap
        $http.get("/rest/strap/getAllStatus").then(resp => {
            $scope.strap = resp.data;
        })
        //load water
        $http.get("/rest/water/getAllStatus").then(resp => {
            $scope.water = resp.data;
        })
        
        //load shell
        $http.get("/rest/shell/getAllStatus").then(resp => {
            $scope.shell = resp.data;
        })
        productId = "";
    }
    //khởi đầu
    $scope.initialize();
$scope.reset1 = function () {
		//alert("reset")
		$scope.form = {
			status: true,
		
		}
	}
	$scope.reset1 = function () {
        //alert("Xóa Form")
        $("#image").val("");
        $scope.formProduct = {
            status: true,
        }
        $scope.formImagePr = {
            images: 'cloud-upload.jpg',
        }
    }
    //Thêm mới
    $scope.create = function () { 
        //alert("Create")
        var item = angular.copy($scope.formProduct);
        item.createDate = item.createDate; 
        
        var productName = document.getElementById("productName").value;
		if(productName.trim() =="" || productName ==null){
			alert("Tên sản phẩm không được để trống");
			return false;
		}
		var categoySelect = document.getElementById("categoySelect").value;
		if(categoySelect =="" || categoySelect ==null){
			alert("Loại đồng hồ không được để trống");
			return false;
		}
		var brandSelect = document.getElementById("brandSelect").value;
		if(brandSelect =="" || brandSelect ==null){
			alert("Hãng sản phẩm không được để trống");
			return false;
		}
		var waterSelect = document.getElementById("waterSelect").value;
		if(waterSelect =="" || waterSelect ==null){
			alert("Chống nước không được để trống");
			return false;
		}
		
		var sizeSelect = document.getElementById("sizeSelect").value;
		if(sizeSelect =="" || sizeSelect ==null){
			alert("Size không được để trống");
			return false;
		}
		var glassSelect = document.getElementById("glassSelect").value;
		if(glassSelect =="" || glassSelect ==null){
			alert("Chất liệu thủy tinh không được để trống");
			return false;
		}
		var strapSelect = document.getElementById("strapSelect").value;
		if(strapSelect =="" || strapSelect ==null){
			alert("Chất dây đeo không được để trống");
			return false;
		}
		var productPrice = document.getElementById("productPrice").value;
		if(productPrice =="" || productPrice ==null){
			alert("Giá sản phẩm không được để trống");
			return false;
		}
		var productQuantity = document.getElementById("productQuantity").value;
		if(productQuantity =="" || productQuantity ==null){
			alert("Số lượng sản phẩm không được để trống");
			return false;
		} 
		var shellSelect = document.getElementById("shellSelect").value;
		if(shellSelect =="" || shellSelect ==null){
			alert("Vỏ bọc sản phẩm không được để trống");
			return false;
		} 
		var productOrigin = document.getElementById("productOrigin").value;
		if(productOrigin =="" || productOrigin ==null){
			alert("Xuất xứ sản phẩm không được để trống");
			return false;
		} 
		var genderRadio = document.getElementsByName("genderRadio");
		var len = genderRadio.length;
		var checkValue = '';
		var checkGender = 0;
		for (var i = 0; i < len; i++){
		    if (genderRadio.item(i).checked){
		        checkValue = genderRadio.item(i).value;
		        if(checkValue != ""){
					checkGender++;
				}
		    }
		} 
		if(checkGender == 0){
			alert("Yêu cầu chọn giới tính.");
			return false;
		}
		
		var image = document.getElementById("image").value;
		if(image =="" || image ==null){
			alert("Chọn hình cho sản phẩm");
			return false;
		}
        $http.post(`/rest/products/checkName`, item).then(resp => {
            if(resp.data !=null && resp.data !=""){
				alert("Mã sản phẩm đã tồn tại");
				return false;
			}else{
				$http.post(`/rest/products`, item).then(resp => {
	            resp.data.createDate = new Date(resp.data.crateDate)
	            $scope.itemsProduct.push(resp.data);
	            //$scope.reset();
	            $scope.initialize();
	            alert("Thêm mới sản phẩm thành công!");
	            $(".nav-tabs a:eq(0)").tab('show')
		        }).catch(error => {
		            alert("Thêm mới thất bại");
		            console.log(error);
		        });
			}
        }).catch(error => {
            alert("Thêm mới thất bại");
            console.log(error);
        });
        
    }
    //cập nhật sp
    $scope.update = function () {
        //alert("update sp")
        var item = angular.copy($scope.formProduct);
        item.crateDate = item.createDate;
        //alert("id " + item.productId +" "+ "crateDate " + item.crateDate);
        
        
        var productName = document.getElementById("productName").value;
		if(productName =="" || productName ==null){
			alert("Tên sản phẩm không được để trống");
			return false;
		}
		var categoySelect = document.getElementById("categoySelect").value;
		if(categoySelect =="" || categoySelect ==null){
			alert("Loại đồng hồ không được để trống");
			return false;
		}
		var brandSelect = document.getElementById("brandSelect").value;
		if(brandSelect =="" || brandSelect ==null){
			alert("Hãng sản phẩm không được để trống");
			return false;
		}
		var waterSelect = document.getElementById("waterSelect").value;
		if(waterSelect =="" || waterSelect ==null){
			alert("Chống nước không được để trống");
			return false;
		}
		
		var sizeSelect = document.getElementById("sizeSelect").value;
		if(sizeSelect =="" || sizeSelect ==null){
			alert("Size không được để trống");
			return false;
		}
		var glassSelect = document.getElementById("glassSelect").value;
		if(glassSelect =="" || glassSelect ==null){
			alert("Chất liệu kính không được để trống");
			return false;
		}
		var strapSelect = document.getElementById("strapSelect").value;
		if(strapSelect =="" || strapSelect ==null){
			alert("Chất dây đeo không được để trống");
			return false;
		}
		var productPrice = document.getElementById("productPrice").value;
		if(productPrice =="" || productPrice ==null){
			alert("Giá sản phẩm không được để trống");
			return false;
		}
		var productQuantity = document.getElementById("productQuantity").value;
		if(productQuantity =="" || productQuantity ==null){
			alert("Số lượng sản phẩm không được để trống");
			return false;
		} 
		var shellSelect = document.getElementById("shellSelect").value;
		if(shellSelect =="" || shellSelect ==null){
			alert("Vỏ bọc sản phẩm không được để trống");
			return false;
		} 
		var productOrigin = document.getElementById("productOrigin").value;
		if(productOrigin =="" || productOrigin ==null){
			alert("Xuất xứ sản phẩm không được để trống");
			return false;
		} 
		var genderRadio = document.getElementsByName("genderRadio");
		var len = genderRadio.length;
		var checkValue = '';
		var checkGender = 0;
		for (var i = 0; i < len; i++){
		    if (genderRadio.item(i).checked){
		        checkValue = genderRadio.item(i).value;
		        if(checkValue != ""){
					checkGender++;
				}
		    }
		} 
		if(checkGender == 0){
			alert("Yêu cầu chọn giới tính.");
			return false;
		}
		
		
		 
        $http.post(`/rest/products/checkName`, item).then(resp => { 
			 if(resp.data !=null && resp.data !="" && resp.data.name !='' && nameCheck!=resp.data.name){ 
				alert("Mã sản phẩm đã tồn tại");
				return false;
			}else{
				$http.put(`/rest/products/${item.productId}`, item).then(resp => {
	            var index = $scope.itemsProduct.findIndex(p => p.productId == item.productId);
	            $scope.itemsProduct[index] = item;
	            alert("Cập nhật thành công");
	            $(".nav-tabs a:eq(0)").tab('show')
        }).catch(error => {
            alert("Xảy ra lỗi trong quá trình cập nhật " + error)
            console.log(error);
        })
			}
		}).catch(error => {
            alert("Xảy ra lỗi trong quá trình cập nhật ");
            console.log(error);
        });;
    }
    //xóa sp
    $scope.delete = function (item) {
        //alert("delete sp" + item.productId)
        var t = confirm("Bạn muốn xóa");
        if (t == false) {
            //alert("Không Xóa");
        } else {
            //alert("có xóa");
            /*$http.delete(`/rest/products/${item.productId}`).then(resp => {
                var index = $scope.itemsProduct.findIndex(p => p.productId == item.productId);
                $scope.itemsProduct.splice(index, 1);
                $scope.reset();
                alert("Xóa Thành Công");
            }).catch(error => {
                alert("Lỗi xóa!")
                console.log("Error", error);
            })*/
            $http.put(`/rest/products/delete/${item.productId}`, item).then(resp => {
	            var index = $scope.itemsProduct.findIndex(p => p.productId == item.productId);
	            $scope.itemsProduct[index] = item;
	            alert("Xóa sp thành công");
	            $scope.initialize();
		        }).catch(error => {
		            alert("Lỗi Xóa Sp" + error)
		            console.log(error);
		        })
            
        }

    }
    
    $scope.formatSoNguyenDuong = function (value,obj) {
    var strvalue;
    strvalue = value;
    var str = strvalue.split('.');
    var num;
    var val = strvalue;
    strvalue = '';
    for (var i = 0; i < val.length; i++) {
        strvalue += $scope.getVal(val.charAt(i));
    }
    num = strvalue.toString().replace(/\$|\./g, '');
    if (num.length > 15) num = num.substring(0, 15);
    if (!$scope.IsNumeric(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    num = Math.floor(num / 100).toString();
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
        num = num.substring(0, num.length - (4 * i + 3))  +
        num.substring(num.length - (4 * i + 3));
    //return (((sign)?'':'-') + num);
   	value = (((sign) ? '' : '-') + num);
   	if(obj =="gia"){
		  $scope.formProduct.price = value;
	  }
	  
	  if(obj =="sl"){
		  $scope.formProduct.quantity = value;
	  }
	  
}

 $scope.allowPressNeg = function(value) {
    var dsChar = '-';
    if (value.indexOf(dsChar) >= 0) {
        if ((event.keyCode < 48 && event.keyCode != 9) || event.keyCode > 57) event.returnValue = false;
    } else {
        if ((event.keyCode < 48 && event.keyCode != 9 && event.keyCode != 45) || event.keyCode > 57) event.returnValue = false;
    }
}

 $scope.formatSoNguyenDuongMaxLength = function(value,maxLength) {
    var strvalue;
    strvalue = value;
    var strvalue2 = strvalue.replace(/\./g, '');
    if(strvalue2.length > maxLength){
    	strvalue = strvalue2.substr(0,maxLength);
    }
    var str = strvalue.split('.');
    var num;
    var val = strvalue;
    strvalue = '';
    for (var i = 0; i < val.length; i++) {
        strvalue += getVal(val.charAt(i));
    }
    num = strvalue.toString().replace(/\$|\./g, '');
    if (num.length > 15) num = num.substring(0, 15);
    if (!IsNumeric(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    num = Math.floor(num / 100).toString();
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
        num = num.substring(0, num.length - (4 * i + 3)) + '.' +
        num.substring(num.length - (4 * i + 3));
    //return (((sign)?'':'-') + num);
    eval(obj).value = (((sign) ? '' : '-') + num);
}

$scope.IsNumeric = function(sText) {
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
}

 $scope.getVal = function(num){
	if(num==''|| $scope.checkNumeric(num)){
	    return '';
	}else{
	    return (num);
	}
}

 $scope.checkNumeric = function(sText)
{
    var ValidChars = "0123456789";
    var IsNumber=true;
    if (ValidChars.indexOf(sText) != -1)
      {
        IsNumber = false;
      }
    return IsNumber;
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
            $scope.formProduct.image = resp.data.name;
            $scope.formImagePr.images = resp.data.name;
        }).catch(error => {
            alert("Lỗi upload hình" + error);
            console.log("Error", error);
        })
    }

    //xóa form
    $scope.reset = function () {
        //alert("Xóa Form")
        $("#image").val("");
        $scope.formProduct = {
			productId: $scope.formProduct.productId,
            createDate: new Date(),
            image: 'cloud-upload.jpg',
            status: true,
        }
        $scope.formImagePr = {
            images: 'cloud-upload.jpg',
        }
    }
    ///tìm kiếm
    $scope.timKiem = function () {
        var name = document.getElementById("keyword").value;
        var trangThai = document.getElementById("trangThai").value;
        if (trangThai == "") {
            trangThai = null;
            //alert("Tìm Kiếm: " + name + " trang thai= " + trangThai)
            $http.get(`/rest/products/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.itemsProduct = resp.data;
            })
        } else {
            $http.get(`/rest/products/timKiem/${name}/${trangThai}`).then(resp => {
                $scope.itemsProduct = resp.data;
            })
        }
       
    }
    $scope.genderStart = function (obj) {
        var trangThai = document.getElementById("trangThai").value;
        //alert("Trang thái " + trangThai )
        if (trangThai == "") {
            $http.get("/rest/products").then(resp => {
                $scope.itemsProduct = resp.data;
            })
        } else {
            $http.get(`/rest/products/timKiem/${trangThai}`).then(resp => {
                $scope.itemsProduct = resp.data;
            })
        }

    }

    //imageProduct
    //hiện lên form product
    var nameCheck="";
    $scope.edit = function (item) { 
        $scope.formProduct = angular.copy(item);
        //$scope.formProduct.createDate = $filter('date')(item.createDate, "dd/MM/yyyy");
        $(".nav-tabs a:eq(1)").tab('show');
        nameCheck = $scope.formProduct.name;
        $("#image").val("");
    }
    //hiện image
    $scope.editImage = function (item) {
        alert("Thêm ảnh chi tiết mã " + item.name)
        console.log(item)
        $scope.formImagePr = {
            images: item.image,
        }
        $scope.formImagePr.product = {
            productId: item.productId,
        }
        productId = item.productId;
        $scope.tableImage(item.productId);
        $(".nav-tabs a:eq(2)").tab('show')
    }

    $scope.image = function (item) {
        // alert("product " + item.product.productId)
        $("#bt1").attr("disabled", true);
        console.log(item)
        $scope.formImagePr = angular.copy(item);
        $scope.tableImage(item.product.productId);
        $(".nav-tabs a:eq(2)").tab('show')
    }

   
    $scope.genderChanged = function () {
        productId = $scope.formImagePr.product.productId;
        if (productId == "") {
            //alert("chọn tất cả ")
            $http.get(`/rest/ImageProduct`).then(resp => { 
                $scope.itemsImagePr = resp.data;
            })
            productId = "";
        } else { 
            //alert("genderChanged " + productId)
            productId = $scope.formImagePr.product.productId;
            $scope.tableImage(productId);
            $scope.pagerImage();

        }
    }

    $scope.tableImage = function (id) { 
        $http.get(`/rest/ImageProduct/${id}`).then(resp => {
            $scope.itemsImagePr = resp.data;
        })
    }


    //Thêm mới ảnh
    $scope.createImage = function () { 
        //$scope.formImagePr.image = resp.data.name;
        var item = angular.copy($scope.formImagePr);
        item.imageId = "";
        if (productId == "") {
            alert("Chưa chọn sp")
        } else {
            //alert("Thêm Mới Anh cho SP " + item.product.productId)
            console.log(item)
            $http.post(`/rest/ImageProduct`, item).then(resp => { 
                $scope.itemsImagePr.push(resp.data);
                //$scope.reset();
                $scope.tableImage(productId);
                alert("Thêm mới ảnh thành công!");
            }).catch(error => {
                alert("Thêm mới ảnh thất bại");
                console.log(error);
            });
        }
    }
    //cập nhật anh
    $scope.updateImage = function () {
        //alert("updateImage")
        var item = angular.copy($scope.formImagePr);
        $http.put(`/rest/ImageProduct/${item.imageId}`, item).then(resp => { 
            var index = $scope.itemsImagePr.findIndex(p => p.imageId == item.imageId);
            $scope.itemsImagePr[index] = item;
            alert("Cập nhật thành công");
            $("#bt1").attr("disabled", false);
        }).catch(error => {
            alert("Xảy ra lỗi trong quá trình cập nhật " + error)
            console.log(error);
        })
    }
    //xóa ảnh
    $scope.deleteImage = function (item) {
        //alert("delete Iamge" + item.imageId)
        $http.delete(`/rest/ImageProduct/${item.imageId}`).then(resp => {debugger
            var index = $scope.itemsImagePr.findIndex(p => p.imageId == item.imageId);
            $scope.itemsImagePr.splice(index, 1);

            $scope.formImagePr = {
                images: 'cloud-upload.jpg',
                product: $scope.formImagePr.product,
            }
            $("#image").val("");
            alert("Xóa Thành Công");
        })
            .catch(error => {
                alert("Lỗi xóa!")
                console.log("Error", error);
            })
    }


    //phân trang product
    $scope.pager = {
        page: 0,
        size: 10,
        get itemsProduct() {
            var start = this.page * this.size;
            return $scope.itemsProduct.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.itemsProduct.length / this.size);
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

    ///phân trang image
    $scope.pagerImage = {
        page: 0,
        sizeImage: 4,
        get itemsImagePr() { 
            var start = this.page * this.sizeImage;
            return $scope.itemsImagePr.slice(start, start + this.sizeImage);
        },
        get countImage() {
            return Math.ceil(1.0 * $scope.itemsImagePr.length / this.sizeImage);
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
            if (this.page >= this.countImage) {
                this.first();
            }
        },
        last() {
            this.page = this.countImage - 1;

        }

    }

})