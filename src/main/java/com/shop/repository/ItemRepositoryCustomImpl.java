package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
  
  private JPAQueryFactory queryFactory;
  
  public ItemRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }
  
  private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
    return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
  }
  
  private BooleanExpression regDtsAfter(String searchDateType) {
    LocalDateTime dateTime = LocalDateTime.now();
    
    if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
      return null;
    } else if (StringUtils.equals("1d", searchDateType)) {
      dateTime = dateTime.minusDays(1);
    } else if (StringUtils.equals("1w", searchDateType)) {
      dateTime = dateTime.minusWeeks(1);
    } else if (StringUtils.equals("1m", searchDateType)) {
      dateTime = dateTime.minusMonths(1);
    } else if (StringUtils.equals("6m", searchDateType)) {
      dateTime = dateTime.minusMonths(6);
    }
    return QItem.item.regTime.after(dateTime);
  }
  
  private BooleanExpression searchByLike(String searchBy, String searchQuery) {
    if (StringUtils.equals("itemNm", searchBy)) {
      return QItem.item.itemNm.like("%" + searchQuery + "%");
    } else if (StringUtils.equals("createdBy", searchBy)) {
      return QItem.item.createdBy.like("%" + searchQuery + "%");
    }
    return null;
  }
  
  
  @Override
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    
    QueryResults<Item> results = queryFactory.selectFrom(QItem.item)   //","단위로 넣어줄 경우 and조건으로 인식
        .where(regDtsAfter(itemSearchDto.getSearchDateType()),
            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
            searchByLike(itemSearchDto.getSearchBy(),
                itemSearchDto.getSearchQuery()))
        .orderBy(QItem.item.id.desc())
        .offset(pageable.getOffset())  //데이터를 갖고 올 시작 인덱스 지정
        .limit(pageable.getPageSize())
        .fetchResults();
    
    List<Item> content = results.getResults();
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
  }
  
  
  private BooleanExpression itemNmLike(String searchQuery) {
    return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
  }
  @Override
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    QItem item = QItem.item;
    QItemImg itemImg = QItemImg.itemImg;
    
    List<MainItemDto> content = queryFactory
        .select(
            new QMainItemDto( //생성자에 반환할 값을을 넣어서 변환 과정 없이 만듬.
                item.id,
                item.itemNm,
                item.itemDetail,
                itemImg.imgUrl,
                item.price)
        )
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repImgYn.eq("Y"))
        .where(itemNmLike(itemSearchDto.getSearchQuery()))
        .orderBy(item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    
    
    long total = queryFactory
        .select(Wildcard.count)
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repImgYn.eq("Y"))
        .where(itemNmLike(itemSearchDto.getSearchQuery()))
        .fetchOne();
    
    return new PageImpl<>(content, pageable, total);
    
  }
}
