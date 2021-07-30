package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 회원_가입() {
        Member member = new Member();
        member.setName("회원");

        Long join = memberService.join(member);

        assertEquals(member, memberRepository.findOne(join));
    }

    @Test
    public void 중복된_이름_테스트() {
        Member member1 = new Member();
        member1.setName("회원");

        Member member2 = new Member();
        member2.setName("회원");

        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member1));
    }

}
