package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository //수동 커리는  Repository 에 합치지 말고 분리된 class 만들어 빈등록이 효율적
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;
    List<Member> findAllMembers() {
         return em.createQuery("select m from Member m")
                .getResultList();
    }
}
