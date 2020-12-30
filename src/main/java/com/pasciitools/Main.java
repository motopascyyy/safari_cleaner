package com.pasciitools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    private static String historyDBURL = "/Users/pasdeignan/Library/Safari/History.db";

    private static String deleteItems = "DELETE from history_items";
    private static String deleteVisits = "DELETE from history_visits";
    private static String deleteEvents = "DELETE from history_events";

    public static void main(String[] args) {
        deleteHistory();
        purgeFiles();
    }

    public static void purgeFiles () {
        System.out.println("Purging Files");
        String userHome = System.getProperty("user.home");
        String binaryCookies = userHome + "/Library/Containers/com.apple.Safari/Data/Library/Cookies/Cookies.binarycookies";
        String lastSession = userHome + "/Library/Safari/LastSession.plist";
        String recentlyClosedTabs = userHome + "/Library/Safari/RecentlyClosedTabs.plist";
        File f = new File (binaryCookies);
        System.out.println ("Deleting " + f.getAbsoluteFile() + ": " + f.delete());
        f = new File (lastSession);
        System.out.println ("Deleting " + f.getAbsoluteFile() + ": " + f.delete());
        f = new File (recentlyClosedTabs);
        System.out.println ("Deleting " + f.getAbsoluteFile() + ": " + f.delete());

        System.out.println("File Purging Completed");
    }

    public static void deleteHistory() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + historyDBURL;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement pstmt = conn.prepareStatement(deleteItems);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(deleteEvents);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(deleteVisits);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Deletes successfully completed");
        } catch (SQLException e) {
            System.out.println("Could not connect to the DB. Please make sure the path is correct and that you have drive permission access: " + historyDBURL + "\n" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Connection Closed to DB");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
