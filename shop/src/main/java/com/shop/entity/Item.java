package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDTO;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity : 클래스를 entity로 설정
//@Table : 엔티티와 매필할 테이블 설정
@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//상품코드

    @Column(nullable=false, length = 50)
    private String itemName;//상품명

    @Column(name = "price", nullable = false)
    private int price;//가격

    //재고수량
    @Column(nullable = false)
    private int stockNumber;

    //@Lob : BLOB, CLOB 타입 매핑
    //BLOB, CLOB : 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입
    @Lob
    @Column(nullable = false)
    private String itemDetail;//상품설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;//상품판매상태


    public void updateItem(ItemFormDTO itemFormDTO){
        System.err.println("entity.Item.updateItem");
        this.itemName = itemFormDTO.getItemName();
        this.price = itemFormDTO.getPrice();
        this.stockNumber = itemFormDTO.getStockNumber();
        this.itemDetail = itemFormDTO.getItemDetail();
        this.itemSellStatus = itemFormDTO.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        System.err.println("entity.Item.removeStock");
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다.");
        }
        this.stockNumber=restStock;
    }
    public void addStock(int stockNumber){
        System.err.println("entity.Item.addStock");
        this.stockNumber = stockNumber;
    }


}
