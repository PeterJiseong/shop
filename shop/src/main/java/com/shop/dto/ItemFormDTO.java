package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Repository("itemFormDTO")
@Getter
@Setter
@ToString
public class ItemFormDTO {
    private Long id;

    @NotBlank(message = "상품 이름은 필수 입력 요소 입니다.")
    private String itemName;

    @NotNull(message = "상품 가격은 필수 입력 요소 입니다.")
    private Integer price;

    @NotBlank(message = "상품 이름은 필수 입력 요소 입니다.")
    private String itemDetail;

    @NotNull(message = "상품 재고는 필수 입력 요소 입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
//    public void setItemImgDTOList(List<MultipartFile> itemImgDTOList){
//        for(int i = 0 ; i < itemImgDTOList.size() ; i++){
//
//        }
//    }
    public Item createItem(){
        System.err.println("dto.ItemFormDTO.createItem");
        System.err.println("ItemFormDTO.createItem진입");
        return modelMapper.map(this,Item.class);
    }
    public static ItemFormDTO of(Item item){
        System.err.println("dto.ItemFormDTO.of");
        return modelMapper.map(item, ItemFormDTO.class);
    }
}
