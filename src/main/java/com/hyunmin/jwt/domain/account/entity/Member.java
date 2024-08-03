package com.hyunmin.jwt.domain.account.entity;

import com.hyunmin.jwt.domain.account.entity.enums.MemberRole;
import com.hyunmin.jwt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_USER;
}
