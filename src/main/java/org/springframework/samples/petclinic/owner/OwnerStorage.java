package org.springframework.samples.petclinic.owner;

import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.system.DatabaseToggles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.stereotype.Repository;


public class OwnerStorage {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    public OwnerStorage(OwnerRepository ownerRepository) {
        if(DatabaseToggles.isEnableNewDb) {
            System.out.println("New DB is running");
        }

        if(DatabaseToggles.isEnableOldDb) {
            this.ownerRepository = ownerRepository;
            System.out.println("Old Database is running");
        }

        if(DatabaseToggles.isUnderTest) {
            System.out.println("System is under test");
        }
    }

    public Collection<Owner> findByLastName(String lastName) {
        System.out.println("Shadow Read findByLastName");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            Collection<Owner> expectedOwners = ownerRepository.findByLastName(lastName);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Collection<Owner> actualOwners = Sqlite.findByLastName(lastName);
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return ownerRepository.findByLastName(lastName);
        }

        return Sqlite.findByLastName(lastName);
    }

    public int consistencyCheck() {
        if(!(DatabaseToggles.isEnableNewDb && DatabaseToggles.isEnableOldDb)) {
            return 0; //return 0 since were not trying to migrate the data
        }
        
        int inconsistencies = 0;
        
        for(Owner owner : ownerRepository.findAll()) {
            
            Owner expectedOwner = ownerRepository.findById(owner.getId());
            Owner actualOwner = Sqlite.findOwnerById(expectedOwner.getId());
            System.out.println("Comparing expected owner: " + expectedOwner.getFirstName() + " " + 
                    expectedOwner.getLastName() + " with actual owner: " + 
                    actualOwner.getFirstName() + actualOwner.getLastName());
            if(!expectedOwner.sameOwner(actualOwner)) {
                //increment number of inconsistencies
                inconsistencies++;
                
                //print it
                printViolation(owner, actualOwner, expectedOwner);
                
                //update in new datastore if not consistent
                Sqlite.addOwner(expectedOwner.getFirstName(), expectedOwner.getLastName(), expectedOwner.getAddress(), expectedOwner.getCity(), expectedOwner.getTelephone());
            }
        }
        
        return inconsistencies;
    }
    
    public void printViolation(Owner owner, Owner actualOwner, Owner expectedOwner) {
        System.out.println("Consistency Violation. Expected: " + 
                expectedOwner.getFirstName() + " " + expectedOwner.getLastName() + " with ID: " + expectedOwner.getId() +
                ", but got: " + actualOwner.getFirstName() + actualOwner.getLastName() + " with ID: " + actualOwner.getId()
        );
    }
    
    public Owner findById(Integer ownerId) {
        System.out.println("Shadow Read findById");
        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            Owner expectedOwner = ownerRepository.findById(ownerId);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Owner actualOwner = Sqlite.findOwnerById(ownerId);
                    //Consistency check stuff between expected and actual
                }
            }).start();
        }

        // If old database is enabled, return old values.
        if(DatabaseToggles.isEnableOldDb) {
            return ownerRepository.findById(ownerId);
        }

        return Sqlite.findOwnerById(ownerId);
    }

    /**
     * Shadow write Owner to old and new datastore
     * @param owner
     */
    @Autowired
    public void save(Owner owner) {
        if(DatabaseToggles.isEnableOldDb) {
            ownerRepository.save(owner);
        }

        if(DatabaseToggles.isEnableOldDb && DatabaseToggles.isEnableNewDb) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sqlite.addOwner(owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
                    consistencyCheck();
                }
            });
        }
    }
}
