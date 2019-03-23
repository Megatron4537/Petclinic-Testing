package org.springframework.samples.petclinic.owner;

import org.hibernate.dialect.Database;
import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.system.DatabaseToggles;

import java.util.ArrayList;
import java.util.Collection;

public class OwnerStorage {

    OwnerRepository ownerRepository;

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
                    ArrayList<Owner> actualOwners = Sqlite.findByLastName(lastName);
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
            System.out.println(owner);
        }
        return inconsistencies;
    }
    
    public Owner findById(Integer ownerId) {


        return null;
    }

    public void save(Owner owner) {
        ownerRepository.save(owner);
        consistencyCheck();
        //add the shadow writes here like if(oldDB) then oldDb.save()...
        
    }
}
