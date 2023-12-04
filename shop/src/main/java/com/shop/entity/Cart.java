package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{
    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch=FetchType.EAGER)//--->즉시 로딩 : 엔티이 한번에 조회
    @JoinColumn(name="member_id")
    private Member member;
    public static Cart createCart(Member member){
        System.err.println("entity.Cart.createCart-DB에 값 생성");
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }


}
