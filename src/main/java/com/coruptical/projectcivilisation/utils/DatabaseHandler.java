package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;

public class DatabaseHandler {
    private ProjectCivilisation plugin = null;
    private static final DatabaseHandler instance = new DatabaseHandler();
    private static final String DB_NAME = "data";
    private File databaseFile = null;
    Connection conn = null;

    public static DatabaseHandler getInstance() {
        return instance;
    }

    private DatabaseHandler(){

    }

    public void init(ProjectCivilisation plugin){
        this.plugin = plugin;

        databaseFile = new File(plugin.getDataFolder().getAbsolutePath(), DB_NAME+".db");
        if (!databaseFile.exists()){
            try {
                databaseFile.createNewFile();
                plugin.getLogger().log(Level.WARNING,
                        "Database file created.");
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().log(Level.SEVERE,
                        "File write error for database file.");
            }
        }

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            plugin.getLogger().log(Level.WARNING, "Opened database connection.");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error attempting to make a connection to database.");
        }
    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error while getting database connection.");

            return null;
        }
    }

    public void openConnection(){
        conn = getConnection();
        plugin.getLogger().log(Level.INFO,
                "Opened database connection.");
    }

    public boolean closeConnection(){
        try {
            conn.close();
            conn = null;
            plugin.getLogger().log(Level.INFO, "Closed database connection.");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error caused while trying to close database connection.");
            return false;
        }
    }
    /**
     * Within the string, add values and names to add
     * to the table by comma separating the things you want.
     * Separate the name and data type with a colon, like
     * "id:int,factionName:varchar(255),factionData:blob" and so on.
     */
    public String generateCreateTableSQL(String tableName, String values){
        // TODO: Test if logic works in this.
        StringBuilder toStore = new StringBuilder();

        String[] variables = values.split(",");
        for (String var: variables){
            String[] identifierAndType = var.split(":");
            String identifier = identifierAndType[0];
            String type = identifierAndType[1];

            toStore.append(identifier).append(" ").append(type).append(", ");
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                "( " +
                toStore +
                ");";

        return createTableSQL;
    }

    public void createGeneralTable(){
        String tableSQL = generateCreateTableSQL("factions", "id:int,name:varchar(255),description:text,ownerUUID:text,power:int,memberUUIDs:text,chunks:blob");
        try {
            Statement statement = conn.createStatement();
            statement.execute(tableSQL);
            plugin.getLogger().log(Level.INFO,
                    "Created factions data table in database.");
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error occurred while trying to create table in database.");
        }
    }



}
