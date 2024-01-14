package org.example.catproj;

import java.sql.Connection;
import java.sql.DriverManager;


public class database {

    public static Connection connectDB() {

        String url = "jdbc:sqlite:./src/main/resources/dat/project.db" ;

        try{

            Connection conn = DriverManager.getConnection(url);

            return conn;

        }catch (Exception e){e.printStackTrace();}

        return null;
    }

}