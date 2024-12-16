package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    
    //회원가입 처리
    public Member saveMember(Member member) {
        validateDuplicateMember(member); // 아이디 중복체크
        return memberRepository.save(member);
    }

    //회원중복체크
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        /*해당 아이디가 DB에 있을경우*/
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override //로그인로직
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("------------------loadUserByUsername-------------------");

        Member member = memberRepository.findByEmail(email);
        
        //email 해당하는 member 없으면
        if(member == null) {
            throw new UsernameNotFoundException(email);
        }


        return User.builder()
                .username(member.getEmail()) //id
                .password(member.getPassword()) //pw
                .roles(member.getRole().toString()) //권한정보
                .build();
    }
}
