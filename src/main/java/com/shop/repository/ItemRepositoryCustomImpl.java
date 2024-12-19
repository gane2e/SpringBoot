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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    /* QueryDSL을 사용하여 JPA 쿼리를 생성하고 실행하는 데 필요한 클래스 */
    private JPAQueryFactory jpaQueryFactory;

    /* EntityManager를 주입받아 초기화 */
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    /* 상품 판매상태를 선택해 검색했으면 해당 상태를 조건으로 데이터를 찾는다. */
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    /* 최근1일 / 1주일 / 1달 / 6개월 검색 */
    private BooleanExpression regDtsAfter(String searchDateType){

        /* 현재 로컬시간을 기준으로 검색함 */
        LocalDateTime dateTime = LocalDateTime.now();

        /* searchDateType이 "all" 이거나 null이면 날짜 조건을 적용하지 않음  */
        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    /* 검색키워드 */
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if (StringUtils.equals("itemNm", searchBy)){ /* 검색조건이 itenNm(상품명)이면 */
            return QItem.item.itemNm.like("%"+searchQuery+"%"); /* 상품명 검색 */
        } else if (StringUtils.equals("createdBy", searchBy)) { /* 검색조건이 createdBy(작성자)면 */
            return QItem.item.createdBy.like("%"+searchQuery+"%"); /* 작성자 검색 */
        }
        return null;
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        
        log.info("------------------------itemSearchDto 로그 START------------------------------");

        log.info("최근일자별 조회 ==>" + regDtsAfter(itemSearchDto.getSearchDateType()));
        log.info("판매상태별 조회 ==>" + itemSearchDto.getSearchSellStatus());
        log.info("키워드 조회 ==>" + itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery());
        log.info("pageable.getOffset() ==>" + pageable.getOffset());
        log.info("pageable.getPageSize() ==>" + pageable.getPageSize());

        log.info("itemSearchDto ==>" + itemSearchDto);

        log.info("------------------------itemSearchDto 로그 END------------------------------");


        List<Item> content = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        log.info("---total--->" + total);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = jpaQueryFactory.select(
                        /* 조인해서 가져올 컬럼 */
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

       /* select count(*)
        from itemImg
        join item
        on item_img.item_id = item.item_id
        where itemimg.repimgYn == 'Y' and
        item.itemNm.like '%검색어%'*/

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }
}
