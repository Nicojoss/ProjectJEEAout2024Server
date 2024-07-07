package be.jossart.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DbConnection {
	private static Connection instance = null;

    private DbConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String ip = "193.190.64.10";
            String port = "1522";
            String service_name = "XEPDB1";
            String chaineConnexion = "jdbc:oracle:thin:@//"+ip+":"+port+"/"+service_name;
            String username = "student03_10";
            String password = "nico2023";
            instance = DriverManager.getConnection(
            chaineConnexion, username, password);
            
            
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Classe de driver introuvable" + ex.getMessage());
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur JDBC : " + ex.getMessage());
        }
        if (instance == null) {
            JOptionPane.showMessageDialog(null, "La base de données est inaccessible, fermeture du programme.");
            System.exit(0);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DbConnection();
        }
        return instance;
    }
}
