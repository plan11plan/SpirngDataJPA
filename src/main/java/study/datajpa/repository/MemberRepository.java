package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom{
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
//    @Query(name="Member.findByUsername")
    List<Member> findByUsername(@Param("username")String username);


    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findUser (@Param("username") String username,@Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();
    /**
     * @Query로 DTO 조회하기
     * DTO로 조회할때는 무조건 New Operation을 써야합니다.
    */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String>names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionByUsername(String username); //단건 Optional

    /**
     * 페이징이 들어간 스프링 데이터 JPA
     */
//    @Query(value = "select m from Member m", 
//            countQuery ="select count(m) from Member m")  --> 카운트만 가져올때 쓰는 쿼리(성능 최적화)
    Page<Member> findByAge(int age, Pageable pageable);  // Pageable 인터페이스(쿼리 조건 들어감) 넘김.

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age")int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();
 /**
  *
  * @EntityGraph jpql에 페치조인 해왔지만.
  * 메소드명으로 패치조인 할 수 있게함
  *  엔티티그래프는 패치조인이 베이스로 돼있음
  *  jpql 없이도 객체 그래프를 엮어서 성능 최적화!
  * */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL에 엔티티 그래프 같이 사용 가능해요.
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List <Member> findMemberEntityGraph();

    // 메소드 이름으로 엔티티 그래프도 가능 !
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username")String username);


    /**
     * 진짜 조회만 할때 최적화 하는 코드 -> @QueryHints
     * 근데 별 효과 없긴함. 진짜 중요한 API 만 적용!
     */
    @QueryHints(value= @QueryHint(name = "org.hibernate.readOnly", value ="true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
