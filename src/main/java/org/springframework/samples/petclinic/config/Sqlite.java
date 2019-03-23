package org.springframework.samples.petclinic.config;

import org.springframework.core.io.ClassPathResource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.Vets;
import org.springframework.samples.petclinic.visit.Visit;


public class Sqlite{

    private static final String url = "jdbc:sqlite:newDb.db";
    private static final String schemaPath = "db/sqlite/schema.sql";
    private static final String dataPath = "db/sqlite/data.sql";

    /**
     * Creates Sqlite table & populates them with default data
     */
    public static void connect() {

            Connection conn = null;

            try {

                System.out.println("------ GET CONNECTION ------");
                conn = DriverManager.getConnection(url);

                ScriptUtils.executeSqlScript(conn,new ClassPathResource(schemaPath));
                System.out.println("------ LOADING DATA INTO DB ------");
                ScriptUtils.executeSqlScript(conn,new ClassPathResource(dataPath));

            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

    /**
     * TODO set a proxy so we never pass in raw queries like this & return row object
     * @param query raw SQL query
     * @return
     */
    public static String query(String query){
        Connection conn = null;
        String result = null;

        try {
            conn = DriverManager.getConnection(url);
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()) {
                result = rs.getString("last_name");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return result;
    }

    public static Owner findOwnerById(int ownerId){
        Connection conn = null;
        Owner owner = new Owner();
        String query = "SELECT * FROM owners WHERE id = ?";
        try {
            conn = DriverManager.getConnection(url);
            Statement state = conn.createStatement();
            PreparedStatement pstmt  = conn.prepareStatement(query);
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                owner.setId(rs.getInt("id"));
                owner.setFirstName(rs.getString("first_name"));
                owner.setLastName(rs.getString("last_name"));
                owner.setCity(rs.getString("city"));
                owner.setAddress(rs.getString("address"));
                owner.setTelephone(rs.getString("telephone"));
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return owner;
    }

    public static Collection<Owner> findByLastName(String lastName) {
        return null;
    }

    public static List<PetType> findPetTypes() {
        return null;
    }

    public static Pet findPetById(Integer id) {
        return null;
    }

    public static Collection<Vet> findAllVets() {
        return null;
    }

    public static List<Visit> findVisitsByPetId(Integer id) {
        return null;
    }

    /*public static Vets getVets(){
        Connection conn = null;
        Vets vets = new Vets();
        String query = "SELECT * FROM vets";
        try {
            conn = DriverManager.getConnection(url);
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            Vet vet;

            while(rs.next()) {
                vet.
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return vets;
    }*/
}
