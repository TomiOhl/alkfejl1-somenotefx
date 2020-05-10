package com.tomiohl.somenotefx.dao;

import com.tomiohl.somenotefx.model.Note;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDAO {

    private final static String DB_STRING = "jdbc:sqlite:recents.db";
    private static final String CREATE_RECENT = "CREATE TABLE IF NOT EXISTS Recents (" +
                                                    "id integer PRIMARY KEY AUTOINCREMENT," +
                                                    "filename text NOT NULL," +
                                                    "filepath text NOT NULL," +
                                                    "savedate date NOT NULL);";

    private static final String INSERT_RECENT = "INSERT INTO Recents (filename, filepath, savedate) VALUES (?,?,?);";

    private static final String DELETE_RECENT = "DELETE FROM Recents WHERE id=?";

    private static final String SELECT_RECENT = "SELECT * FROM Recents;";

    private static final String SAVE_CHECK_EXISTENCE = "SELECT id FROM Recents WHERE filename = ? AND filepath = ?;";

    private static final String SAVE_EXISTING = "UPDATE Recents SET savedate = ? WHERE id = ?;";

    public void initializeTables() {
        try (Connection conn = DriverManager.getConnection(DB_STRING); Statement st = conn.createStatement()) {
            st.executeUpdate(CREATE_RECENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public NoteDaoImpl() {
        initializeTables();
    }

    @Override
    public boolean add(Note n) {
        try (Connection conn = DriverManager.getConnection(DB_STRING); PreparedStatement st = conn.prepareStatement(INSERT_RECENT)) {
            st.setString(1, n.getFilename());
            st.setString(2, n.getFilePath());
            st.setLong(3, n.getSaveDate());
            int res = st.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteFromRecents(Note n) {
        try (Connection conn = DriverManager.getConnection(DB_STRING); PreparedStatement st = conn.prepareStatement(DELETE_RECENT)) {
            st.setString(1, Integer.toString(n.getId()));
            int res = st.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String open(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
                line = reader.readLine();
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(Note note) {
        // check if letezik a note az adatbazisban, ez alapjan insert vagy update
        ResultSet rs;
        int id = -1;    // ha letezik mar a parameterben kapott Note, akkor ez felulirodik
        try (Connection conn = DriverManager.getConnection(DB_STRING); PreparedStatement st = conn.prepareStatement(SAVE_CHECK_EXISTENCE)) {
            st.setString(1, note.getFilename());
            st.setString(2, note.getFilePath());
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (id == -1) {
                // nincs note az adatbazisban, insert
                return add(note);
            } else {
                // van ilyen note, update savedate
                PreparedStatement upDate = conn.prepareStatement(SAVE_EXISTING);
                upDate.setLong(1, note.getSaveDate());
                upDate.setInt(2, id);
                int res = upDate.executeUpdate();
                if (res == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveToStorage(String path, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path),
                StandardCharsets.UTF_8))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Note> getAll() {
        List<Note> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_STRING); Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_RECENT);

            while (rs.next()) {
                Note n = new Note(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getLong(4)
                );
                result.add(n);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
