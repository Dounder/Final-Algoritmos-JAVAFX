package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase es la encargada de hacer la conexion a la base de datos en PostgreSQL
 */
public class Connect {

    public Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/algoritmos",
                    "postgres","password");
            if (connection != null) System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

    public void closeConnection(){
        try {
            getConnection().close();
            System.out.println("Connection to DB closed");
        } catch (SQLException e) {
            System.out.println("Connection to the DB could not be closed" + e);
        }
    }

}
