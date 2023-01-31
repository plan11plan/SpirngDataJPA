package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private  final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return  member.getUsername();
    }

    /** 추천 안함.
     * id는 pk니까 도메인 클래스 컨버터를 사용합니다.
     * 스프링이 중간에 컨버팅 하는과정을 다 끝내고 바로 파라미터 주입해줍니다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id")Member member){
        return member.getUsername();
    }

    /**
     *http://localhost:8080/members?page=0&size=3  이렇게 파라미터 넣으면 PageRequest 객체가 자동생성 그리고.바인딩되서
     *    pageable에 들어감
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
//        page.map(member -> new MemberDto(member));
        page.map(MemberDto::new);  //반환할때도 DTO!!
        return page;
    }
    //디테일 설정
    @GetMapping("/members")
    public Page<Member> list2(@PageableDefault(size =5,sort="username") Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
//        page.map(member -> new MemberDto(member));
        page.map(MemberDto::new);  //반환할때도 DTO!!
        return page;
    }
    @PostConstruct
    public void init(){
        for(int i=0; i<100;i++){
            memberRepository.save(new Member("user"+i,i));
        }

    }
}
