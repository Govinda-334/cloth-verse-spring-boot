package com.govind.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AdminOrderResponse {

    private Long id;

    // 🔥 Product details for admin table
    private String productName;
    private String productImage;
    private int totalQuantity;   // total quantity of all items

    private BigDecimal totalAmount;
    private String address;



    private String userName;
    private String userPhone;

    private String status;
}
