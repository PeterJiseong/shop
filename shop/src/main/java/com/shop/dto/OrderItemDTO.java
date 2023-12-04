package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    public OrderItemDTO(OrderItem orderItem, String imgUrl) {
        System.err.println("dto.OrderItemDTO.생성자");
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemName;
    private int count;
    private int orderPrice;
    private String imgUrl;

}
