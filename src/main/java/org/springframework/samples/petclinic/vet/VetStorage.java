package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.system.DatabaseToggles;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;

import java.util.Collection;

public class VetStorage {
    VetRepository vetRepository;

    public VetStorage(VetRepository vetRepository) {
        if(DatabaseToggles.isEnableNewDb) {
            System.out.println("New DB is running");
        }

        if(DatabaseToggles.isEnableOldDb) {
            this.vetRepository = vetRepository;
            System.out.println("Old Database is running");
        }

        if(DatabaseToggles.isUnderTest) {
            System.out.println("System is under test");
        }
    }

    public Collection<Vet> findAll() {
        System.out.println("Shadow Read findByLastName");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            Collection<Vet> expectedVets = vetRepository.findAll();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Collection<Vet> actualVets = Sqlite.findAllVets();
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return vetRepository.findAll();
        }

        return Sqlite.findAllVets();
    }
}
