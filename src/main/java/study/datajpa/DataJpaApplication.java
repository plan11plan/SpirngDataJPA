package study.datajpa;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@EnableJpaAuditing  // 스프링 데이터 JPA의 수정날짜.생성날짜 생성자.수성자 기능 제공하는 애너테이션 //예제 ->BaseEntity.class
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
 	// 생성자 .수정자 생성
}
