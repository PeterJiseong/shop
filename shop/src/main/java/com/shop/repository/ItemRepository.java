package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//<엔티티 타입, 해당 엔티티의 기본키 타입>
public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {
    List<Item> findByItemName (String itemName);
    List<Item> findByItemNameOrItemDetail (String itemName, String itemDetail);
    List<Item> findByItemNameContaining (String itemName);
    List<Item> findByPriceLessThan (Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc (Integer price);

    //i는 데이터 베이스 자체, (Item i)로 선언

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail (@Param("itemDetail") String itemDetail);
    @Query(value = "select * from item where item_Detail like %:a% order by price desc",nativeQuery = true)
    List<Item> findByItemDetailByNative (@Param("a")String itemDetail);




}
