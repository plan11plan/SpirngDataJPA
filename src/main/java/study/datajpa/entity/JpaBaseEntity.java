package study.datajpa.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 진짜 상속은 아님. 속성만 상속
@Data
@Getter
/**
 *  생성날짜. 수정날짜 박아주는 클래스
 */
public class JpaBaseEntity {
    @Column(updatable = false) // 변경불가.
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
