package com.udacity.jdnd.course3.critter.pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public Pet save(Pet pet){
        Pet petNew = petRepository.save(pet);
        return petNew;
    }

    public Pet getPetById(Long petId){
        Pet pet = petRepository.getOne(petId);
        return pet;
    }

    public List<Pet> getAllPets(){
        List<Pet> petList = petRepository.findAll();
        return petList;
    }

    public List<Pet> getPetsByOwner(Long customerId){
        List<Pet> petList = petRepository.findAllPetsByCustomerId(customerId);
        return petList;
    }



}
