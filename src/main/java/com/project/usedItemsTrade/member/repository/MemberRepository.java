package com.project.usedItemsTrade.member.repository;

import com.project.usedItemsTrade.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailCode(String emailCode);
    Optional<Member> findByPasswordResetKey(String passwordResetKey);

    @Query("select m from Member m where m.email = :email and m.fromSocial = :fromSocial")
    Optional<Member> findByEmailAndFromSocial(String email, boolean fromSocial);
}
