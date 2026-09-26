package com.acme.benefits.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Member data access.
 *
 * Created:  02 Feb 1998  R. Whitfield
 * Modified: 30 Jul 2002  M. Okamoto  - moved from Oracle OCI to thin driver
 */
public class MemberDAO {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@benefits-db:1521:BENE";
    private static final String USER = "benefits_app";
    private static final String PASS = "benefits";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void saveEnrollment(Hashtable record) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ENROLLMENT (MEMBER_ID, SSN_ENC, PLAN_CODE) VALUES (?, ?, ?)");
            ps.setString(1, (String) record.get("memberId"));
            ps.setString(2, (String) record.get("ssn"));
            ps.setString(3, (String) record.get("planCode"));
            ps.executeUpdate();
            ps.close();
        } finally {
            conn.close();
        }
    }

    public String findPasswordHash(String user) throws Exception {
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT PWD_HASH FROM MEMBER WHERE USERNAME = '" + user + "'");
            String hash = null;
            if (rs.next()) {
                hash = rs.getString(1);
            }
            rs.close();
            st.close();
            return hash;
        } finally {
            conn.close();
        }
    }

    public String findSalt(String user) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT PWD_SALT FROM MEMBER WHERE USERNAME = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            String salt = null;
            if (rs.next()) {
                salt = rs.getString(1);
            }
            rs.close();
            ps.close();
            return salt;
        } finally {
            conn.close();
        }
    }

    public Vector listPlans() {
        Vector plans = new Vector();
        plans.addElement("PPO-1998");
        plans.addElement("HMO-2001");
        plans.addElement("HDHP-2009");
        return plans;
    }
}
