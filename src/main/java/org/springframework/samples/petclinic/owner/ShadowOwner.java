package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.config.Sqlite;

import java.sql.*;

public class ShadowOwner {

    public ShadowOwner() {
    }

    public Owner findById(int ownerId) {
        Sqlite.connect();
        Owner owner = Sqlite.findOwnerById(ownerId);
        return owner;
    }
}
