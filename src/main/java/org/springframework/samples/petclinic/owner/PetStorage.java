package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.system.DatabaseToggles;

import java.util.List;

public class PetStorage {

    PetRepository petRepository;

    public PetStorage(PetRepository petRepository) {
        if(DatabaseToggles.isEnableNewDb) {
            System.out.println("New DB is running");
        }

        if(DatabaseToggles.isEnableOldDb) {
            this.petRepository = petRepository;
            System.out.println("Old Database is running");
        }

        if(DatabaseToggles.isUnderTest) {
            System.out.println("System is under test");
        }
    }

    public List<PetType> findPetTypes() {
        System.out.println("Shadow Read findPetTypes");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            List<PetType> expectedTypes = petRepository.findPetTypes();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<PetType> actualTypes = Sqlite.findPetTypes();
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return petRepository.findPetTypes();
        }

        return Sqlite.findPetTypes();
    }

    public Pet findById(Integer petId) {
        System.out.println("Shadow Read findById");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            Pet expectedPet = petRepository.findById(petId);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Pet actualPet = Sqlite.findPetById(petId);
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return petRepository.findById(petId);
        }

        return Sqlite.findPetById(petId);
    }

    /**
     * Shadow write Pet to old and new datastore
     * @param pet
     */
    public void save(Pet pet) {
        if(DatabaseToggles.isEnableOldDb) {
            petRepository.save(pet);
        }

        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sqlite.addPet(pet.getName(), pet.getBirthDate(), pet.getOwner().getId(), pet.getType().getId());
                    // consistencyCheck();
                }
            });
        }
    }
}
