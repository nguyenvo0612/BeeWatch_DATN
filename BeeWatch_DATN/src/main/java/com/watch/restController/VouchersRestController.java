package com.watch.restController;

import com.watch.dao.VoucherDao;
import com.watch.entity.Accounts;
import com.watch.entity.Orders;
import com.watch.entity.UserAcounts;
import com.watch.entity.Vouchers;
import com.watch.service.OrdersService;
import com.watch.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/vouchers")
public class VouchersRestController {
	@Autowired 
	private VoucherService voucherService;
	@Autowired
    UserAcounts useAcc;
	@Autowired
	OrdersService orderService;
	@Autowired 
	VoucherDao voucherDao;
	
	@GetMapping()
	public List<Vouchers> getall() {
		List<Vouchers> lstVoucher = voucherService.findAll();
		Date today = new Date();
		for(Vouchers voucher : lstVoucher) {
			if(voucher.getNgayKetThuc().compareTo(today) <0 ) {
				voucher.setStatustt(false);
				voucherDao.save(voucher);
			}
		}
		return lstVoucher;
//		return voucherDao.getVoucherWithAcc();
	}
	@GetMapping("{voucher_Name}")
	public Vouchers getOne(@PathVariable("voucher_Name") String Voucher_Name) {
		return voucherService.getOne(Voucher_Name);
	}
	
	@PutMapping("{voucher_Name}")
	public Vouchers update(@PathVariable("voucher_Name") String Voucher_Name,@RequestBody Vouchers vouchers) {
		return voucherService.save(vouchers);
	}
	
	@PutMapping("/delete/{voucher_Name}")
	public Vouchers updateXoa(@PathVariable("voucher_Name") String Voucher_Name,@RequestBody Vouchers vouchers) {
		vouchers.setStatustt(false);
		return voucherService.save(vouchers);
	}
	
//	@GetMapping
//	public List<Vouchers> getIndex(){
//		return voucherService.findAll();
//	}

	@PostMapping
	public Vouchers on(@RequestBody Vouchers vouchers) {
		return voucherService.save(vouchers);
	}

	@PutMapping("{id}")
	public Vouchers update(@PathVariable("id") Integer id, @RequestBody Vouchers vouchers) {
		return voucherService.save(vouchers);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") String id) {
		voucherService.deleteById(id);
	}

	@GetMapping("/timKiem/{name}/{status}")
	public List<Vouchers> timKiem(@PathVariable("name") String name,@PathVariable("status") String status){
		//System.out.println("tên= "+name + " status= "+ status);
		if (status.equals("null")) {
			System.out.println("tên= "+name);
			return voucherService.findByName1("%"+name+"%");
		} else {
			boolean in  = Boolean.parseBoolean(status);
			System.out.println("tên= "+name + " status="+ in);
			return voucherService.findByName("%"+name+"%" , in);
		}
	}

	@GetMapping("/timKiem/{status}")
	public List<Vouchers> getStatus(@PathVariable("status") Boolean status){
		return voucherService.findByStatus(status);
	}
	
	@GetMapping("/check/{voucher_Name}")
	public int check(@PathVariable("voucher_Name") String Voucher_Name){
		int stt = 0;
		 Accounts account = useAcc.User();
		List<Orders> lst = orderService.getByIdVoucher(account.getAccountId());
		if(lst.isEmpty()) {
			return stt;
		}
		
		for (int i = 0; i < lst.size(); i++) {
			String voucherName = lst.get(i).getVoucher().getVoucherName();
			if(voucherName.equals(Voucher_Name)) {
				stt += 1; 
			}
		}
		return stt;
	}
	
}