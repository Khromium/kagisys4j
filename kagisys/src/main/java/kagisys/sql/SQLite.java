package kagisys.sql;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class SQLite {

    /**
     * ドライバの登録
     */
    public static void dbInit() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        Properties prop = new Properties();
        prop.put("journal_mode", "MEMORY");
        prop.put("sync_mode", "OFF");
        return prop;
    }

    /**
     * IFIDの保存部分
     *
     * @param dbFile
     * @param userId
     */
    public static synchronized void putId(File dbFile, String userId) {
        String TABLE = "id";
        Statement stmt;
        String dbHeader = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        PreparedStatement pstmt;
        dbInit();
        try (Connection conn = DriverManager.getConnection(dbHeader, getProperties())) {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE +
                    " ( user_id TEXT PRIMARY KEY)");
            pstmt = conn.prepareStatement("INSERT OR IGNORE INTO " + TABLE + " VALUES (?)");
            pstmt.setString(1, userId);
            pstmt.addBatch();
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定されたキーが存在しているか確認するコード
     * @param dbFile
     * @param userId
     * @return
     */
    public static boolean isExist(File dbFile, String userId) {
        if (!dbFile.exists()) return false;
        String TABLE = "id"; //もしくは　profiles　にする。 profilesの方が早い。DBの欠損で一度データが消えたので変えてみる
        String dbHeader = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        PreparedStatement pstmt;
        dbInit();
        try (Connection conn = DriverManager.getConnection(dbHeader, getProperties())) {
            pstmt = conn.prepareStatement("SELECT * FROM " + TABLE + " WHERE user_id = \"" + userId+"\"");
            pstmt.setFetchSize(100);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

}
