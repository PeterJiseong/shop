package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDTO;
import com.shop.dto.MainItemDTO;
import com.shop.dto.QMainItemDTO;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    //BooleanExpression : querydsl에서 조건을 표현하기 위해 사용되는 인터페이스
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        System.err.println("repository.ItemRepositoryCustomImpl.searchSellStatusEq");
        return searchSellStatus ==
                null?null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        System.err.println("repository.ItemRepositoryCustomImpl.regDtsAfter");
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all",searchDateType)||searchDateType==null){
            return null;
        }else if(StringUtils.equals("id",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        System.err.println("repository.ItemRepositoryCustomImpl.searchByLike");
        if(StringUtils.equals("itemName",searchBy)){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createBy.like("%"+searchQuery+"%");
        }

        return null;
    }



    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        System.err.println("repository.ItemRepositoryCustomImpl.getAdminItemPage");
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDTO.getSearchDateType()),//','로 and 조건, BooleanBuilder로 or조건
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()))
                .orderBy(QItem.item.id.desc())//정렬
                .offset(pageable.getOffset())//데이터를 가져오도록 시작 인덱스 설정
                .limit(pageable.getPageSize())//한번에 가져올 페이지의 개수
                .fetch();
        //전체항목수
        long total = queryFactory.select(Wildcard.count)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchDTO.getSearchDateType()),//','로 and 조건, BooleanBuilder로 or조건
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery())
                )
                .fetchOne();


        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        System.err.println("repository.ItemRepositoryCustomImpl.getMainItemPage");
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;


        List<MainItemDTO> content = queryFactory.select(
                        new QMainItemDTO(
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDTO.getSearchQuery()))
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        System.err.println("repository.ItemRepositoryCustomImpl.itemNameLike");
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
    }


}
