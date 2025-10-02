package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";
    private static final String USER = "root"; 
    private static final String PASS = "Delfin123."; 
    
    private ConexionBD() {}
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver JDBC. Asegúrate de que el archivo .jar esté en el classpath.");
            throw new SQLException("Error de driver JDBC", e);
        }
    }
}
