/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.mockito.Mock;

/**
 *
 * @author Daniewl
 */
public class OwnerStorageTest {
    
    OwnerStorage ownerStorage;
    
    @Test
    public void testOwnerStorageConsistent() {
        
        
        
        OwnerStorage storage = new OwnerStorage(ownerRepository);
        
        
    }
}
