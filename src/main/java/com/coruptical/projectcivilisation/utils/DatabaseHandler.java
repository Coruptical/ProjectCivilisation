package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.models.StoredChunk;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
     * Separate the name and data type with a colon, E.g.
     * "id:int,factionName:varchar(255),factionData:blob" and so on.
     */
    public String generateCreateTableSQL(String tableName, String values){
        StringBuilder toStore = new StringBuilder();

        String[] variables = values.split(",");
        for (String var: variables){
            String[] identifierAndType = var.split(":");
            String identifier = identifierAndType[0];
            String type = identifierAndType[1];

            toStore.append(identifier).append(" ").append(type).append(", ");
        }

        // We need to remove the last comma added otherwise we have SQL error.
        toStore.deleteCharAt(toStore.lastIndexOf(","));

        return "CREATE TABLE IF NOT EXISTS " +
                tableName +
                "( " +
                toStore +
                ");";
    }

    public void createFactionsTable(){
        String tableSQL = generateCreateTableSQL("factions", "id:int,name:text,description:text,ownerUUID:text,power:int,memberUUIDs:text,chunks:blob");
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

    public void insertFaction(Faction faction){
        openConnection();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO factions (id, name, description, ownerUUID, power, memberUUIDs, chunks) VALUES (?,?,?,?,?,?,?)"
            );
            preparedStatement.setShort(1, faction.getFactionID());
            preparedStatement.setString(2, faction.getName());
            preparedStatement.setString(3, faction.getDescription());
            preparedStatement.setString(4, faction.getOwnerUUID());
            preparedStatement.setInt(5, faction.getPower());
            preparedStatement.setString(6, Utils.getMembersAsString(faction.getMemberUUIDs()));

            Set<StoredChunk> storedChunks = Utils.getStoredFromChunks(faction.getChunks());
            preparedStatement.setBytes(7, SerializeHandler.toBytes(storedChunks));

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error while trying to insert faction.");
        }
    }

    public void updateFaction(Faction faction){
        // id:int,name:text,description:text,ownerUUID:text,power:int,memberUUIDs:text,chunks:blob
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE factions SET name=?,description=?,ownerUUID=?,power=?,memberUUIDs=?,chunks=? WHERE id = ?"
            );

            preparedStatement.setShort(7, faction.getFactionID());

            preparedStatement.setString(1, faction.getName());
            preparedStatement.setString(2, faction.getDescription());
            preparedStatement.setString(3, faction.getOwnerUUID());
            preparedStatement.setInt(4, faction.getPower());
            preparedStatement.setString(5, Utils.getMembersAsString(faction.getMemberUUIDs()));
            preparedStatement.setBytes(6, SerializeHandler.toBytes(Utils.getStoredFromChunks(faction.getChunks())));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error while trying to update faction into database.");
        }
    }

    public void updateAllFactions(){
        for (short id : plugin.getIdsFactionsMap().keySet()){
            Faction fac = plugin.getIdsFactionsMap().get(id);
            updateFaction(fac);
        }
    }

    public void loadFactions(){
        openConnection();
        createFactionsTable();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from factions");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                // Take the result set data from each row and make a new faction in memory.
                short id = resultSet.getShort(0);
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                String ownerUUID = resultSet.getString(3);
                int power = resultSet.getInt(4);
                String memberUUIDString = resultSet.getString(5);
                byte[] chunkDataBytes = resultSet.getBytes(6);

                List<String> memberUUIDs = Arrays.asList(memberUUIDString.split(","));
                Faction faction = new Faction(name,
                        description,
                        Utils.getChunksFromStored(id, chunkDataBytes),
                        ownerUUID,
                        memberUUIDs,
                        power,
                        id);

                // Put this into our ids and factions hashmap so we can use it.
                plugin.getIdsFactionsMap().put(id, faction);
                plugin.getNamesIdsMap().put(name, id);
            }
            preparedStatement.close();

            for (short factionID: plugin.getIdsFactionsMap().keySet()){
                Faction faction = plugin.getIdsFactionsMap().get(factionID);
                for (FastChunk c : faction.getChunks()){
                    plugin.getChunkIdMap().put(c.getId(), c);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE,
                    "Error while trying to load all factions.");
        }

        closeConnection();
    }

}
