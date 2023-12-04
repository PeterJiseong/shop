package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count){
        System.err.println("entity.CartItem.createCartItem - DB에 cartItem 값 생성");
        //cartItem 객체를 만들고
        CartItem cartItem = new CartItem();
        //cart 값을 받아와서 set 하고
        cartItem.setCart(cart);
        //받아온 item값으로 셋하고
        cartItem.setItem(item);
        cartItem.setCount(count);
        //그 객체를 리턴
        return cartItem;
    }
    public void addCount(int count){
        this.count +=count;
    }
    public void updateCount(int count){
        this.count = count;
    }

}
