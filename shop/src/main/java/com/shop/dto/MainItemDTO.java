package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MainItemDTO {
    private Long id;
    private String itemName;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection
    public MainItemDTO(Long id, String itemName, String itemDetail, String imgUrl, Integer price){
        System.err.println("dto.MainItemDTO.생성자");
        this.id = id;
        this.itemDetail = itemDetail;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
