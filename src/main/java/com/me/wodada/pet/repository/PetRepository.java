package com.me.wodada.pet.repository;

import com.me.wodada.member.domain.Member;
import com.me.wodada.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByIdAndMember(Long petId, Member member);
}
