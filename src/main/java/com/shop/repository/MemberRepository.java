package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //Member 반환타입으로 email기준 찾겠음 (아이디 중복체크)
    Member findByEmail(String email);

}
