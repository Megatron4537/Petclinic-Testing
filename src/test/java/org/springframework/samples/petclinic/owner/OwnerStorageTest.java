/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.system.ConsistencyChecker;

/**
 *
 * @author Daniewl
 */
public class OwnerStorageTest {
    
    OwnerStorage ownerStorage;
    
    @MockBean
    private OwnerRepository owners;
    
    @Test
    public void testOwnerStorageConsistent() {
        
        OwnerStorage storage = new OwnerStorage(owners);
        
        //get the consistency value
        ConsistencyChecker checker = new ConsistencyChecker();
        checker.updateOwnerConsistencyCheck();
        checker.checkConsistency();
        
        //before changing anything
        assertTrue(checker.checkConsistency());
        
        Owner owner = new Owner();
        owner.setFirstName("Peter");
        owner.setLastName("Rigby");
        owner.setId(123);
        owner.setAddress("address");
        owner.setCity("montreal");
        owner.setTelephone("5145555555");

        System.out.print(owner);
        storage.save(owner);
        
        //after adding a new owner
        assertFalse(checker.checkConsistency());//TODO: Add shadow writes so that this will work.
        
        checker.updateOwnerConsistencyCheck();
        
        assertTrue(checker.checkConsistency());

    }
}
