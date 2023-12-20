package com.watch.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Entity

public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private Date createDate = new Date();
    @Column(columnDefinition = "NVARCHAR(200) Not NULL")
    private String address;
    @Column(columnDefinition = "NVARCHAR(50) Not NULL")
    private int status;
    private double total;
    @Column(columnDefinition = "int default 0")
    private int tthaiThanhToan;

    @Column(columnDefinition = "NVARCHAR(200)  NULL")
    private String tenNn;

    @Column(columnDefinition = "NVARCHAR(15)  NULL")
    private String sdtNn;


    @ManyToOne
    @JoinColumn(name = "accountId")
    private Accounts account;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "voucherName")
    private Vouchers voucher;

    @ManyToOne
    @JoinColumn(name = "vistingGuestId")
    @JsonBackReference
    private VistingGuest vistingGuest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;


    @Column(columnDefinition = "NVARCHAR(200)  NULL")
    private String reason;

    @Column(columnDefinition = "NVARCHAR(100)  NULL")
    private String anhLoiSanPham;

    @Column(columnDefinition = "NVARCHAR(200)  NULL")
    private String sendToCustomer;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_end")
    private Date dateEnd;

    @Column(name = "tien_coc")
    private float tienCoc;


    public void setTienSauGiam(double tienSauGiam) {
        this.tienSauGiam = tienSauGiam;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public float getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(float tienCoc) {
        this.tienCoc = tienCoc;
    }

    public void setSendToCustomer(String sendToCustomer) {
        this.sendToCustomer = sendToCustomer;
    }

    public String getSendToCustomer() {
        return sendToCustomer;
    }

    public VistingGuest getVistingGuest() {
        return vistingGuest;
    }

    @Column(name = "tien_sau_giam")
    private double tienSauGiam;

    public Double getTienSauGiam() {
        return tienSauGiam;
    }


    public void setTienSauGiam(Double tienSauGiam) {
        this.tienSauGiam = tienSauGiam;
    }

    public void setVistingGuest(VistingGuest vistingGuest) {
        this.vistingGuest = vistingGuest;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAnhLoiSanPham() {
        return anhLoiSanPham;
    }

    public void setAnhLoiSanPham(String anhLoiSanPham) {
        this.anhLoiSanPham = anhLoiSanPham;
    }

    public int getTthaiThanhToan() {
        return tthaiThanhToan;
    }

    public void setTthaiThanhToan(int tthaiThanhToan) {
        this.tthaiThanhToan = tthaiThanhToan;
    }

    public String getTenNn() {
        return tenNn;
    }

    public void setTenNn(String tenNn) {
        this.tenNn = tenNn;
    }

    public String getSdtNn() {
        return sdtNn;
    }

    public void setSdtNn(String sdtNn) {
        this.sdtNn = sdtNn;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public Vouchers getVoucher() {
        return voucher;
    }

    public void setVoucher(Vouchers voucher) {
        this.voucher = voucher;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }


    public Orders(int orderId, Date createDate, String address, int status, double total, int tthaiThanhToan, String tenNn, String sdtNn, Accounts account, Vouchers voucher, VistingGuest vistingGuest, List<OrderDetail> orderDetails, String reason, String anhLoiSanPham, Double tienSauGiam) {
        this.orderId = orderId;
        this.createDate = createDate;
        this.address = address;
        this.status = status;
        this.total = total;
        this.tthaiThanhToan = tthaiThanhToan;
        this.tenNn = tenNn;
        this.sdtNn = sdtNn;
        this.account = account;
        this.voucher = voucher;
        this.vistingGuest = vistingGuest;
        this.orderDetails = orderDetails;
        this.reason = reason;
        this.anhLoiSanPham = anhLoiSanPham;
        this.tienSauGiam = tienSauGiam;
    }

    public Orders() {
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                ", createDate=" + createDate +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", total=" + total +
                ", tthaiThanhToan=" + tthaiThanhToan +
                ", tenNn='" + tenNn + '\'' +
                ", sdtNn='" + sdtNn + '\'' +
                ", account=" + account +
                ", voucher=" + voucher +
                ", vistingGuest=" + vistingGuest +
                ", orderDetails=" + orderDetails +
                ", reason='" + reason + '\'' +
                ", anhLoiSanPham='" + anhLoiSanPham + '\'' +
                ", tienSauGiam=" + tienSauGiam +
                '}';
    }
}
