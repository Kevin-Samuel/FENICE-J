/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-4-20
 * @Version : 1.0.1
 */

package fenice.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Description: base database operation class
 *  function: 
 * 
 */

public class DBOperationAdapter {
	
	private static DBOperationAdapter instance = null;
	
	private Connection connection = null;
	
	private Statement stmt = null;
	
	private HashMap<String, String> dbConfig = new HashMap<String, String>();
	
	/**
	 * 
	 * @return Singlton instance
	 */
	public static DBOperationAdapter GetInstance() {
		
		if (instance == null) {
			instance = new DBOperationAdapter();
		}
		return instance;
	}
	
	public static String filterSQL(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", " ");
	}
	
	
	/**
	 * Constructor
	 * 
	 */
	private DBOperationAdapter() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			HashMap<String, String> config = new HashMap<String, String>();
			config.put("url", "jdbc:mysql://localhost/fenice");
			config.put("user", "fenicesun");
			config.put("pwd", "930709");
			setDbConfig(config);
			
			setConnection(DriverManager.getConnection(config.get("url"), config.get("user"), config.get("pwd")));
			
			setStmt(connection.createStatement());
			
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public HashMap<String, String> getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(HashMap<String, String> dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public boolean execute(String sql) throws SQLException {
		return stmt.execute(sql);
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		return stmt.executeQuery(sql);
	}
	
	public int executeUpdate(String sql) throws SQLException {
		return stmt.executeUpdate(sql);
	}
	
	public static String generateSQL(String type, String table, HashMap<String, String> data, String where) {
		String sqlStmt = new String();
		ArrayList<String> keys = new ArrayList<String>(data.keySet());
		ArrayList<String> values = new ArrayList<String>(data.values());
		
		
		switch(type) {
			
			case "INSERT":
				sqlStmt = "insert into " + table + " ( ";
				for (int i = 0; i < keys.size(); i++) {
					sqlStmt += keys.get(i);
					if (i != keys.size() - 1) {
						sqlStmt += " , ";
					}
				}
				sqlStmt += " ) values ( ";
				for (int i = 0; i < values.size(); i ++) {
					sqlStmt += "'" + values.get(i) + "'";
					if (i != values.size() - 1) {
						sqlStmt += " , ";
					}
				}
				sqlStmt += " ) " + where;
				break;
			case "UPDATE":
				sqlStmt = "update " + table + " set ";
				for (int i = 0; i < keys.size(); i++) {
					sqlStmt += keys.get(i) + " = '" + values.get(i) + "' ";
					if (i != keys.size() - 1) {
						sqlStmt += " , ";
					}
				}
				sqlStmt += where;
				break;
			case "DELETE":
				sqlStmt = "delete from " + table + " " + where;
				break;
		}
		return sqlStmt;
	}
	
	public ResultSet select(String table, ArrayList<String> fields, String where) throws SQLException {
		String sql = "select ";
		for (int i = 0; i < fields.size(); i++) {
			sql += fields.get(i);
			if (i != fields.size() - 1) {
				sql += " , ";
			}
		}
		sql += " from " + table + " " + where;
		return executeQuery(sql);
	}
	
	public boolean insert(String table, HashMap<String, String> data) throws SQLException {
		String sql = generateSQL("INSERT", table, data, "");
		return execute(sql);
	}
	
	public boolean update(String table, HashMap<String, String> data, String where) throws SQLException {
		String sql = generateSQL("UPDATE", table, data, where);
		return execute(sql);
	}
	
	public boolean delete(String table, String where) throws SQLException {
		String sql = generateSQL("DELETE", table, null, where);
		return execute(sql);
	}
	
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		DBOperationAdapter db = DBOperationAdapter.GetInstance();
		ResultSet rs = db.executeQuery("select * from t_security");
		while (rs.next()) {
			String security_id = rs.getString("Fsecurity_id");
			System.out.println(security_id);
		}
		/*
		String sql = "SELECT * FROM t_security WHERE Fsecurity_id = ?";
		PreparedStatement prestmt = db.getConnection().prepareStatement(sql);
		prestmt.setString(1, "FENICE");
		ResultSet rr = prestmt.executeQuery();
		while (rr.next()) {
			System.out.println(rr.getString("Fsecurity_id"));
		}
		rs.close();
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Fsecurity_id", "HASAYAKE");
		map.put("Fsecurity_name", "YAKE");
		map.put("Fprice", "8.88");
		map.put("Fshares", "100000");
		map.put("Fvalue", "888000");
		map.put("Fnet_asset", "5.0");
		//String where = "where Fsecurity_id = 'FENICE'";
		String str = DBOperationAdapter.generateSQL("INSERT", "t_security", map, "");
		db.insert("fenice.t_security", map);
		System.out.println(str);
		*/
		ArrayList<String> field = new ArrayList<String>();
		field.add("Fsecurity_id");
		field.add("Fsecurity_name");
		String where = new String("where Fsecurity_id = 'MORGAN'");
		ResultSet rrrs = db.select("t_security", field, where);
		while (rrrs.next()) {
			String id = rrrs.getString(1);
			String name = rrrs.getString(2);
			System.out.println(id + "   " + name);
		}
		db.close();
	}
}
