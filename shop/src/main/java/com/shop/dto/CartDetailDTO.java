package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CartDetailDTO {

    private Long cartItemId;
    private String itemName;
    private int price;

    public CartDetailDTO(Long cartItemId, String itemName, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }

    private int count;
    private String imgUrl;
}
