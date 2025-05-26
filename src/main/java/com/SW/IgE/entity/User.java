package com.SW.IgE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // ID는 자동 생성되며, 외부에서 임의로 수정할 수 없도록 제한
    private Integer id;

    @Column(unique = true, nullable = false) // 이메일은 유니크하고 null 값을 허용하지 않음
    @NotBlank // 이메일 필드는 공백일 수 없음
    @Email // 이메일 형식 유효성 검사
    private String useremail;

    @NotBlank // 비밀번호 필드는 공백일 수 없음
    private String password;

    @NotBlank // 이름 필드는 공백일 수 없음
    private String name;

    @NotNull // 나이 필드는 null 값을 허용하지 않음
    @Positive // 나이 필드는 양수여야 함
    private Integer age;

    @Builder.Default
    @ElementCollection // 별도의 테이블에 user_ige 데이터를 저장하도록 설정
    @CollectionTable(name = "user_ige", joinColumns = @JoinColumn(name = "user_id")) // user_ige 테이블에 user_id 컬럼을 FK로 설정
    @Column(name = "user_ige") // user_ige 컬렉션의 컬럼 이름을 지정
    private List<String> user_ige = new ArrayList<>(); // 알레르기 목록, 기본적으로 빈 리스트로 초기화

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 데이터베이스에 저장
    private Role role; // 사용자 역할 (USER 또는 ADMIN)

    // 사용자 역할 정의
    public enum Role {
        USER, // 일반 사용자
        ADMIN // 관리자
    }
}
