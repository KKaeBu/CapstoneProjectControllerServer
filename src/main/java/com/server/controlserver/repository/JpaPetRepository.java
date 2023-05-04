package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPetRepository implements PetRepository{
    private final EntityManager em;

    public JpaPetRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Pet save(Pet pet){
        em.persist(pet);
        return pet;
    } // 펫 저장

    @Override
    public Pet update(Pet pet) {
        em.merge(pet);
        return pet;
    } // 회원 업데이트

    @Override
    public Optional<Pet> findById(Long id) {
        Pet pet = em.find(Pet.class, id);
        return Optional.ofNullable(pet);
    } //id로 찾아서 반환

    @Override
    public Optional<Pet> findByName(String name){
        List<Pet> result = em.createQuery("select p from Pet p where p.name = :name", Pet.class)
                .setParameter("name",name)
                .getResultList();
        return result.stream(   ).findAny();
    } //이름으로 찾아서 반환

    @Override
    public List<Pet> findAll(){
        List<Pet> result = em.createQuery("select p from Pet p", Pet.class)
            .getResultList();
        return result;
    } //모든 회원 반환
}
