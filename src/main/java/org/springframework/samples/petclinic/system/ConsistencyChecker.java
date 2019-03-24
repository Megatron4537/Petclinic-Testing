package org.springframework.samples.petclinic.system;

import java.util.List;
import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.owner.Owner;
import org.apache.commons.codec.digest.DigestUtils;

public class ConsistencyChecker {
    
    String oldCheck;

    public boolean compareOwners(Owner a, Owner b) {
        System.out.println("------ CONSISTENCY CHECKER ------");
        if (a.sameOwner(b)) {
            System.out.println("Owners are the same.");
            return a.sameOwner(b);
        } else {
            System.out.println("Owners are NOT the same.");
            return false;
        }
    }
    
    public void updateOwnerConsistencyCheck() {
        oldCheck = calculateOwnersConsistency();
        System.out.println("Old consistency hash: " + oldCheck);
    }
    
    private String calculateOwnersConsistency() {

            String items = "";
            List<Owner> ownersCopy= Sqlite.getAllOwners();
            
            for(Owner owner : ownersCopy) {
                items = hashValue(items + owner.getFirstName() + //getting the first and last name to remove variable object Ids ie. Owner@f6c48ac
                        owner.getLastName() + owner.getId());
            }
            
            return items;
    }
    
    public boolean checkConsistency() {
        String actualHash = calculateOwnersConsistency();
        System.out.println("Old hash: " + oldCheck + " | Actual hash: " + actualHash);
        return oldCheck.equals(actualHash);
    }
    
    
    /**
     * Taken from class notes 
     * https://github.com/rigbypc/StoreCheckout/blob/ddbc60949cba38a9371906713d5eeaf4fdaf821e/StoreCheckout/src/main/java/point/of/sale/StoreConsistencyChecker.java
     * @param value
     * @return 
     */
    private String hashValue(String value) {
            return DigestUtils.shaHex(value).toUpperCase();
    }
}
