package ots;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql_JDBC {

	public static final String DBDRIVER = "com.mysql.jdbc.Driver";

	public static final String DBURL = "jdbc:mysql://localhost:3306/otsdb";

	public static final String DBUSER = "ots";

	public static final String DBPASS = "welcomeots";

	public static void main(String[] args) {
		Connection con = null;
		
		try {
			Class.forName(DBDRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(con);
		
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}