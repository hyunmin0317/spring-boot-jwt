package com.hyunmin.jwt.domain.account.repository;

import com.hyunmin.jwt.domain.account.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
