package org.example;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectH2 {

private final static String DB_DRIVER = "org.h2.Driver";                           
private final static String DB_URL = "jdbc:h2:tcp://localhost/~/test";                             
private final static String DB_USERNAME ="sa";                                     
private final static String DB_PASSWORD =	 "";                                      
//https://stackoverflow.com/questions/10656213/java-sql-syntax-error-insert-into - sql injections

public static Connection getConnection(){                                                 
    Connection connection = null;                                                  
    try {                                                                          
        Class.forName(DB_DRIVER);                                                  
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        System.out.println("Connection OK");                                       
    }catch(SQLException e){                                                        
        e.printStackTrace();                                                       
        System.out.println("Connection ERROR");                                    
    }                                                                              
    catch (ClassNotFoundException e){                                              
        e.printStackTrace();                                                       
        System.out.println("Connection ERROR");                                    
    }                                                                              
    return connection;                                                             
}                                                                                  


}