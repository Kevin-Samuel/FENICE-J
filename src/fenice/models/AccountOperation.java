/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-4-20
 * @Version : 1.0.1
 */

package fenice.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountOperation extends AbstractModelOperation {
	
	public AccountOperation() {
		super();
	}
	
	public boolean hasThisAccount(String accountId) {
		String sql = "SELECT count(*) FROM fenice.t_account WHERE Faccount = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(accountId));
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
	
	/*
	 *  Get Security Holding data by accountId
	 *  @param accountId
	 *  @return SecurityHolding list
	 */
	public HashMap<String, Integer> querySecurityHolding(String accountId) {
		
		String sql = "SELECT Fsecurity_id, sum(Fquantity) FROM fenice.t_security_holding WHERE Faccount = ? GROUP BY Faccount, Fsecurity_id";
		ResultSet rs = null;
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, accountId);
			rs = preStmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		HashMap<String, Integer> securityHoldingList = new HashMap<String, Integer>();
		
		try {
			while (rs.next()) {
				String securityId = rs.getString(1);
				int quantity = rs.getInt(2);
				securityHoldingList.put(securityId, quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return securityHoldingList;
	}
	
	/*
	 *  Get number of particular security by accountId and securityId
	 *  @param accountId, securityId
	 *  @return number of security holding
	 */
	public int querySecurityHoldingNum(String accountId, String securityId) {
		
		String sql = "SELECT sum(Fquantity) AS Fquantity FROM fenice.t_security_holding WHERE Faccount = ? AND Fsecurity_id = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(accountId));
			preStmt.setString(2, DBOperationAdapter.filterSQL(securityId));
			rs = preStmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int result = 0;
		try {
			if (rs.next())
				result = rs.getInt(1);
			else 
				result = 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 *  check if this account has holding of this security
	 *  @param accountId, securityId 
	 *  @return true/false
	 */
	public boolean hasSecurityHolding(String accountId, String securityId) {
		
		String sql = "SELECT count(Fid) as holdingNum FROM fenice.t_security_holding WHERE Faccount = ? AND Fsecurity_id = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(accountId));
			preStmt.setString(2, DBOperationAdapter.filterSQL(securityId));
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
	
	/*
	 *  query orders relate to account
	 * 
	 * 
	 */
	public ArrayList<String> queryOrder(String accountId) {
		
		String sql = "SELECT Forder_id FROM fenice.t_account_order_relation WHERE Faccount = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(accountId));
			rs = preStmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				String orderId = rs.getString(1);
				list.add(orderId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/*
	 * Open new security holding, add security to account
	 * @param accountId, securityId, quantity
	 * @return true/false
	 */
	public boolean openSecurityHolding(String accountId, String securityId, int quantity) {
		//check if has holding by accountId and securityId
		boolean hasThisSecurity = hasSecurityHolding(accountId, securityId);
		
		if (hasThisSecurity == true) 
			return false;
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Faccount", accountId);
		data.put("Fsecurity_id", securityId);
		data.put("Fquantity", String.valueOf(quantity));
		
		boolean result =false; 
		try {
			dbInstance.insert("fenice.t_security_holding", data);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	
	/*
	 *  increase security holding, add holding to particular security
	 *  @param accountId, securityId, quantity
	 *  @return true/false
	 */
	public boolean increaseSecurityHolding(String accountId, String securityId, int quantity) {
		
		boolean hasThisSecurity = hasSecurityHolding(accountId, securityId);
		
		if (hasThisSecurity == false) {
			return openSecurityHolding(accountId, securityId, quantity);
		}
		
		int currentHoldingNum = querySecurityHoldingNum(accountId, securityId);
		if (currentHoldingNum + quantity < 0) {
			return false;
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Faccount", accountId);
		data.put("Fsecurity_id", securityId);
		data.put("Fquantity", String.valueOf(currentHoldingNum + quantity));
		String where = " WHERE Faccount = '" + DBOperationAdapter.filterSQL(accountId) + "' AND Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		
		boolean result = false;
		try {
			result = dbInstance.update("fenice.t_security_holding", data, where);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 
	 * 
	 */
	public boolean decreaseSecurityHolding(String accountId, String securityId, int quantity) {
		
		boolean hasThisSecurity = hasSecurityHolding(accountId, securityId);
		
		if (hasThisSecurity == false) {
			return false;
		}
		
		int currentHoldingNum = querySecurityHoldingNum(accountId, securityId);
		if (currentHoldingNum - quantity < 0) {
			return false;
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Faccount", accountId);
		data.put("Fsecurity_id", securityId);
		data.put("Fquantity", String.valueOf(currentHoldingNum - quantity));
		String where = " WHERE Faccount = '" + DBOperationAdapter.filterSQL(accountId) + "' AND Fsecurity_id = '" + DBOperationAdapter.filterSQL(securityId) + "'";
		
		boolean result = false;
		try {
			result = dbInstance.update("fenice.t_security_holding", data, where);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 
	 * 
	 */
	public double queryBalance(String accountId) {
		
		String sql = "SELECT Fbalance FROM fenice.t_account WHERE Faccount = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(accountId));
			rs = preStmt.executeQuery();
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
	
	
	/*
	 * 
	 */
	public boolean updateBalance(String accountId, double amount) {
		
		double balance = queryBalance(accountId);
		if (balance + amount < 0) {
			return false;
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Fbalance", String.valueOf(balance + amount));
		String where = "WHERE Faccount = " + DBOperationAdapter.filterSQL(accountId);
		boolean result = false;
		try {
			dbInstance.update("fenice.t_account", data, where);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		AccountOperation account = new AccountOperation();
		HashMap<String, Integer> holding = account.querySecurityHolding("000001");
		for (Map.Entry<String, Integer> entry : holding.entrySet()) {
			String id = entry.getKey().toString();
			int holdingnum = entry.getValue().intValue();
			System.out.println(id + "   " + holdingnum);
		}
		
		System.out.println(account.decreaseSecurityHolding("000001", "MORGAN", 50000000));
		ArrayList<String> list = account.queryOrder("000001");
		for(String str : list) {
			System.out.println(str);
		}
		System.out.println(account.queryBalance("000001"));
		//System.out.println(account.updateBalance("000001", 1000000));
		System.out.println(account.hasThisAccount("000002"));
	}
	
}
