package jpabook.jpashop;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 사이드 이펙트를 방지하기 위해서 Member 를 리턴하지 않고 아이디만 리턴한다.
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
