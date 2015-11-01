package fenice.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SecurityOperation extends AbstractModelOperation {
	
	public SecurityOperation() {
		super();
	}
	
	public boolean hasThisSecurity(String securityId) {
		
		String sql = "SELECT count(*) FROM fenice.t_security WHERE Fsecurity_id = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(securityId));
			rs = preStmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean result = false;
		try {
			if (rs.next()) {
				int num = rs.getInt(1);
				result = num > 0 ? true : false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public double getSecurityPrice(String securityId) {
		
		ResultSet rs = null;
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("Fprice");
		String table = "fenice.t_security";
		String where = "WHERE Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		try {
			rs = dbInstance.select(table, fields, where);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double result = 0.0;
		try {
			if (rs.next()) {
				result = rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getSecurityShares(String securityId) {
		
		ResultSet rs = null;
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("Fshares");
		String table = "fenice.t_security";
		String where = "WHERE Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		try {
			rs = dbInstance.select(table, fields, where);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int result = -1;
		try {
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateSecurityPrice(String securityId, double price) {
		int shares = getSecurityShares(securityId);
		HashMap<String, String> data = new HashMap<String, String>();
		double value = shares * price;
		data.put("Fprice", String.valueOf(price));
		data.put("Fvalue", String.valueOf(value));
		String where = "WHERE Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		boolean result = false;
		try {
			dbInstance.update("fenice.t_security", data, where);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateSecurityShares(String securityId, int shares) {
		double price = getSecurityPrice(securityId);
		HashMap<String, String> data = new HashMap<String, String>();
		double value = price * shares;
		data.put("Fshares", String.valueOf(shares));
		data.put("Fvalue", String.valueOf(value));
		System.out.println("price:  " + price);
		System.out.println("shares: " + shares);
		System.out.println("value:  " + value);
		String where = "WHERE Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		boolean result = false;
		try {
			dbInstance.update("fenice.t_security", data, where);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String args[]) {
		SecurityOperation security = new SecurityOperation();
		System.out.println(security.getSecurityPrice("MORGAN"));
		System.out.println(security.getSecurityShares("FENICESUN"));
		System.out.println(security.hasThisSecurity("FENICE"));
		//System.out.println(security.updateSecurityPrice("HASAYAKE", 8.89));
		//System.out.println(security.updateSecurityShares("MORGAN", 200000000));
	}
}
