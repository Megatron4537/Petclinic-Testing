package org.springframework.samples.petclinic.visit;

import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.system.DatabaseToggles;

import java.util.List;

public class VisitStorage {

    VisitRepository visitRepository;

    public VisitStorage(VisitRepository visitRepository) {
        if(DatabaseToggles.isEnableNewDb) {
            System.out.println("New DB is running");
        }

        if(DatabaseToggles.isEnableOldDb) {
            this.visitRepository = visitRepository;
            System.out.println("Old Database is running");
        }

        if(DatabaseToggles.isUnderTest) {
            System.out.println("System is under test");
        }
    }

    public List<Visit> findByPetId(Integer petId) {
        System.out.println("Shadow Read findByLastName");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            List<Visit> expectedVisits = visitRepository.findByPetId(petId);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Visit> actualVisits = Sqlite.findVisitsByPetId(petId);
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return visitRepository.findByPetId(petId);
        }

        return Sqlite.findVisitsByPetId(petId);
    }

    /**
     * Shadow write Visit to old and new datastore
     * @param visit
     */
    public void save(Visit visit) {
        if(DatabaseToggles.isEnableOldDb) {
            visitRepository.save(visit);
        }

        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sqlite.addVisit(visit.getPetId(), visit.getDate(), visit.getDescription());
                    // consistencyCheck();
                }
            });
        }
    }
}
