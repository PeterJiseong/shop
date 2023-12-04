package com.shop;

import com.querydsl.core.BooleanBuilder;
import com.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest//테스트 어노테이션
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {
    @PersistenceContext // EntityManager를 빈으로 주입할 때 사용하는 어노테이션
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("1")
    public void createItemTest(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);

        System.out.println(saveItem.toString());
    }

    public void createItemList(){
        List<Item> list = new ArrayList<>();
        for(int i = 0 ; i <=10 ; i++){
            Item item = new Item();
            item.setItemName("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item saveItem = itemRepository.save(item);


            list.add(item);
        }
        for(int i = 0 ; i <=10 ; i++){
            Item item = new Item();
            item.setItemName("하기스 상품"+i);
            item.setPrice(100000+i);
            item.setItemDetail("하기스 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item saveItem = itemRepository.save(item);


            list.add(item);
        }
        //System.out.println(list);

    }
    @Test
    @DisplayName("native테스트")
    public void findByItemNameTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("2");
        for(Item item : itemList){
            System.err.println("----- 결과 : "+item.toString());
        }
    }
    @Test
    @DisplayName("query DSLTest")
    public void queryDSLTest(){
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qitem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qitem).where(qitem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qitem.itemDetail.like("%"+"테스트 상품 상세 설명"+"%")).orderBy(qitem.price.desc());
        List<Item> itemList = query.fetch();
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    public void createItemList2(){
        List<Item> list = new ArrayList<>();
        for(int i = 0 ; i <= 5 ; i++){
            Item item = new Item();
            item.setItemName("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item saveItem = itemRepository.save(item);


            list.add(item);
        }
        for(int i = 6 ; i <= 10 ; i++){
            Item item = new Item();
            item.setItemName("하기스 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("하기스 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item saveItem = itemRepository.save(item);


            list.add(item);
        }
        //System.out.println(list);

    }
    @Test
    @DisplayName("이상한 인터페이스")
    public void queryDSLTest2(){
        this.createItemList2();
        //쿼리에 들어갈 조건을 만들어주는 빌더
        //동적으로 조건을 만들기 위해 사용
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellState = "SELL";

        //지정된 문자열을 포함사는 상품을 검색하도록 조건을 추가
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));

        //판매상태가 'SELL'인 경우, 해당하는 상품을 검색하도록 조건을 추가
        if(StringUtils.equals(itemSellState, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder,pageable);
        System.err.println("total elements : " + itemPagingResult.getTotalElements());
        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item reSltItem : resultItemList){
            System.err.println(reSltItem.toString());
        }
    }

}
