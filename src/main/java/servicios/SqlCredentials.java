/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author Diego Carcamo
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SqlCredentials {

    private String user;
    private String database;
    private String port;
    private String password;
    private String host;

    public SqlCredentials(String user, String database, String port, String password, String host) {
        this.user = user;
        this.database = database;
        this.port = port;
        this.password = password;
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public String getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public void readCredentialsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("USER:")) {
                    this.user = line.substring(5).trim();
                } else if (line.startsWith("PORT:")) {
                    this.port = (line.substring(5).trim());
                } else if (line.startsWith("HOST:")) {
                    this.host = line.substring(5).trim();
                } else if (line.startsWith("PASSWORD:")) {
                    this.password = line.substring(9).trim();
                } else if (line.startsWith("DATABASE:")) {
                    this.database = line.substring(9).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCredentialsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("USER: " + user);
            writer.newLine();
            writer.write("PORT: " + port);
            writer.newLine();
            writer.write("HOST: " + host);
            writer.newLine();
            writer.write("PASSWORD: " + password);
            writer.newLine();
            writer.write("DATABASE: " + database);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
