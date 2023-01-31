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

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"trem"})
    @Query("select m from Member m")
    List <Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username")String username);

    @QueryHints(value= @QueryHint(name = "org.hibernate.readOnly", value ="true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
