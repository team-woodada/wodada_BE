package com.me.wodada.pet.controller;

import com.me.wodada.member.domain.Member;
import com.me.wodada.pet.dto.request.PetInfoReq;
import com.me.wodada.pet.dto.request.UpdatePetInfoReq;
import com.me.wodada.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    @PostMapping()
    public ResponseEntity<Void> addPetInfo(@AuthenticationPrincipal Member member,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @Valid @RequestPart PetInfoReq petInfoReq){
        petService.addPetInfo(member, petInfoReq, image);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{petId}")
    public void updatePetInfo(@AuthenticationPrincipal Member member,
                              @PathVariable Long petId,
                              @RequestPart(value = "image", required = false) MultipartFile image,
                              @RequestPart @Valid UpdatePetInfoReq updatePetInfoReq){
        petService.updatePetInfo(member, petId, updatePetInfoReq, image);
    }

    @DeleteMapping("/{petId}")
    public void deletePetInfo(@AuthenticationPrincipal Member member,
                              @PathVariable Long petId){
        petService.deletePetInfo(member, petId);
    }

}
