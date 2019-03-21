package org.springframework.samples.petclinic.config;

import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import org.springframework.jdbc.datasource.init.ScriptUtils;


public class Sqlite{

    private static final String url = "jdbc:sqlite::newDb.db";
    private static final String schemaPath = "db/sqlite/schema.sql";
    private static final String dataPath = "db/sqlite/data.sql";

    /**
     * Creates Sqlite table & populates them with default data
     */
    public static void connect() {

            Connection conn = null;

            try {

                conn = DriverManager.getConnection(url);

                ScriptUtils.executeSqlScript(conn,new ClassPathResource(schemaPath));
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
}
