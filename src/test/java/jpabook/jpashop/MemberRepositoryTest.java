package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    /**
     * 이 코드는 실패합니다.
     * 이유는 MemberRepository 에서 사용하고 있는 EntityManager 를 주입해주기 위해서
     * 트랜젝션 내부에서 EntityManagerFactory 를 찾아야 하는데 트랜젝션이 없는 코드이기 때문입니다. (스프링 웹에서는 새로운 트랜젝션을 하나 생성해서 주입해줌)
     *  => 엔티티 메니저를 통한 데이터 변경은 모두 트랜젝션 내부에서 이루어져야 합니다.
     *
     * 따라서 Transactional 어노테이션을 사용한다.
     * 이때 이 어노테이션은 Spring 소속 것을 사용하여야 한다.
     *
     * Transactional 어노테이션은 Repository 내부에서 사용해도 되고
     * */
    /*
    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member();

        member.setUsername("멤버");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }
    */

    /**
     * @Transactional 어노테이션은 Repository 에 집어 넣어도 됩니다.
     *
     * hash 코드를 구현한 것이 없기 때문에 지금 isEqualTo 는 == 비교라고 보면 됩니다.
     * */
    @Transactional
    @Rollback(false)
    @Test
    public void testMemberWithTransactional() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("멤버");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }


}
