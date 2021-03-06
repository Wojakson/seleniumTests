package com.undeadcrud.repository;

import com.undeadcrud.domain.Undead;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UndeadRepositoryImpl implements UndeadRepository {

    private Connection connection;
    private PreparedStatement addUndeadStatement;
    private PreparedStatement getAllStatement;
    private PreparedStatement getByIdStatement;
    private PreparedStatement deleteTableStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteByIdStatement;
   // private PreparedStatement getUndeadStatement;
    private PreparedStatement deleteAllUndeadsStatement;

    public UndeadRepositoryImpl(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isReady()) {
            createTables();
        }
        setConnection(connection);
    }

    public UndeadRepositoryImpl() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:62124/workdb");
        if (!isReady()) {
            createTables();
        }
        setConnection(this.connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) throws SQLException {
        this.connection = connection;
        addUndeadStatement = connection.prepareStatement("INSERT INTO Undead (nazwa, typ, tier, lokacja, zdolnoscSpecjalna) VALUES (?, ?, ?, ?, ?)");
        getAllStatement = connection.prepareStatement("SELECT * FROM Undead");
        getByIdStatement = connection.prepareStatement("SELECT * FROM Undead WHERE ID = ?");
        updateStatement = connection.prepareStatement("UPDATE Undead SET nazwa = ?, typ = ?, tier = ?, lokacja = ?, zdolnoscSpecjalna = ? WHERE ID = ?");
        deleteTableStatement = connection.prepareStatement("DROP TABLE Undead");
        deleteByIdStatement = connection.prepareStatement("DELETE FROM Undead WHERE ID = ?");
        //getUndeadStatement = connection.prepareStatement("SELECT id, nazwa, typ, tier, lokacja, zdolnoscSpecjalna FROM Undead WHERE id = ?");
        deleteAllUndeadsStatement = connection.prepareStatement("DELETE FROM Undead");
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE" + " Undead(id int GENERATED BY DEFAULT AS IDENTITY, " + "nazwa varchar(40), " + "typ varchar(40), " + "tier int, " + " lokacja varchar(40), " + "zdolnoscSpecjalna varchar(40))");
    }

    public boolean isReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if (introduceSelf().equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public List<Undead> getAll() {
        List<Undead> undeads = new ArrayList<>();
        try {
            ResultSet rs = getAllStatement.executeQuery();

            while (rs.next()) {
                Undead undead = new Undead();
                //undead.setId(rs.getInt("id"));
                undead.setNazwa(rs.getString("nazwa"));
                undead.setTyp(rs.getString("typ"));
                undead.setTier(rs.getInt("tier"));
                undead.setLokacja(rs.getString("lokacja"));
                undead.setZdolnoscSpecjalna(rs.getString("zdolnoscSpecjalna"));
                undeads.add(undead);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return undeads;
    }

    @Override
    public Undead getUndeadById(int id) {
        Undead undead = new Undead();

        try {
            getByIdStatement.setInt(1, id);
            ResultSet rs = getByIdStatement.executeQuery();

            while (rs.next()) {
                undead.setId(rs.getInt("id"));
                undead.setNazwa(rs.getString("nazwa"));
                undead.setTyp(rs.getString("typ"));
                undead.setTier(rs.getInt("tier"));
                undead.setNazwa(rs.getString("lokacja"));
                undead.setNazwa(rs.getString("zdolnoscSpecjalna"));
                return undead;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return undead;
    }


    @Override
    public int addUndead(Undead undead) {
        try {
            addUndeadStatement.setString(1, undead.getNazwa());
            addUndeadStatement.setString(2, undead.getTyp());
            addUndeadStatement.setInt(3, undead.getTier());
            addUndeadStatement.setString(4, undead.getLokacja());
            addUndeadStatement.setString(5, undead.getZdolnoscSpecjalna());
            return addUndeadStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }


    @Override
    public int deleteUndead(Undead undead) throws SQLException {
        try {
            deleteByIdStatement.setInt(1, undead.getId());
            return deleteByIdStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

    }

    @Override
    public int updateUndead(int Idbefore, Undead undead) throws SQLException {
        try {
            updateStatement.setString(1, undead.getNazwa());
            updateStatement.setString(2, undead.getTyp());
            updateStatement.setInt(3, undead.getTier());
            updateStatement.setString(4, undead.getLokacja());
            updateStatement.setString(5, undead.getZdolnoscSpecjalna());
            updateStatement.setInt(6, Idbefore);
            return updateStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

    }

   /* @Override
    public Undead getUndead(int id) throws SQLException {
        try {
            getUndeadStatement.setLong(1, id);
            ResultSet rs = getUndeadStatement.executeQuery();

            if (rs.next()) {
                Undead undead = new Undead();
                undead.setId(rs.getInt("id"));
                undead.setNazwa(rs.getString("nazwa"));
                undead.setTyp(rs.getString("typ"));
                undead.setTier(rs.getInt("yob"));
                undead.setNazwa(rs.getString("lokacja"));
                undead.setNazwa(rs.getString("zdolnoscSpecjalna"));
                return undead;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        throw new SQLException("Undead with id " + id + " does not exist");
    }
*/
    @Override
    public String introduceSelf() {
        return "Undead";
    }

    @Override
    public int deleteAll() {
        try {
            return deleteAllUndeadsStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

    }
}