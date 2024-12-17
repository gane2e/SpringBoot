package com.shop.repository;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Cart;
import com.shop.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class CartRepositoryTest {
    
    @Autowired
    CartRepository cartRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    private MemberRepository memberRepository;

    public Member createMember() {
        MemberFormDto memberFormDto =
                MemberFormDto.builder()
                        .email("NewTest5@test.com")
                        .name("홍길동")
                        .address("서울시 마포구 합정동")
                        .password("1234")
                        .build();
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
//    @Commit -> 트랜잭션 처리하면서 테스트후 롤백시키지않고 DB에 커밋한다.
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);

        cartRepository.save(cart);

        em.flush(); //2차캐시저장(DB에반영 / 롤백가능)
        em.clear(); //캐시클리어

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        log.info("savedCart : " + savedCart);


        /* 두값이 같은지 비교 */
        assertEquals(savedCart.getMember().getId(), member.getId());
    }

}