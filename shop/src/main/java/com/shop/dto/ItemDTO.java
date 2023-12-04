package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository("itemDTO")
@Getter
@Setter
public class ItemDTO {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer stockNumber;
    private String itemDetail;
    private String sellStatCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
