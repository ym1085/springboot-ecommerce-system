package com.shoppingmall.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "account", length = 300)
    private String account;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "picture", length = 15)
    private String picture;

    @Column(name = "birth_date", length = 12)
    private String birthDate;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "cert_yn", length = 1)
    private String certYn;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "gender", length = 10)
    private String gender;

    @Builder
    public MemberEntity(String name, String account, String password, String email, String phoneNumber, String picture, String birthDate, String useYn, String certYn, String role, LocalDateTime createDate, LocalDateTime updateDate, String gender) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.birthDate = birthDate;
        this.useYn = useYn;
        this.certYn = certYn;
        this.role = role;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.gender = gender;
    }
}
