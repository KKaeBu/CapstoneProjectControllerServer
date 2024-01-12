package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
  Pet save(Pet pet); // 펫 저장

  Pet update(Pet pet); // 회원 업데이트

  Optional<Pet> findById(Long id); // id로 찾아서 반환

  Optional<Pet> findByName(String name); // 이름으로 찾아서 반환

  List<Pet> findAll(); // 모든 회원 반환

  Long findHighestId(); // 펫의 id 중 가장 높은 값을 반환
}
