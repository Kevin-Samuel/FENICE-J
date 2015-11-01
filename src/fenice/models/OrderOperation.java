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
import java.util.HashMap;

import fenice.engine.orderbook.EntrustType;
import fenice.engine.orderbook.TradeDirection;

public class OrderOperation extends AbstractModelOperation {
	
	public OrderOperation() {
		super();
	}
	
	/*
	 *  check if has this order id
	 *  @param orderId
	 *  @return true/false
	 */
	public boolean hasThisOrder(String orderId) {
		
		String sql = "SELECT count(*) as num FROM fenice.t_order WHERE Forder_id = ?";
		ResultSet rs = null;

		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(orderId));
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
	 *  create a new order
	 *  @param orderId, securityId, entrustType, tradeDirection, price, quantity, orderTime
	 *  @return true/false
	 */
	public boolean newOrder(String orderId, String securityId, EntrustType entrustType, TradeDirection tradeDirection, double price, int quantity, java.util.Date orderTime) {
		String _entrustType = entrustType == EntrustType.MARKET ? "MARKET" : "LIMIT";
		String _tradeDirection = tradeDirection == TradeDirection.ASK ? "ASK" : "BID";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Forder_id", orderId);
		data.put("Fsecurity_id", securityId);
		data.put("Fentrust_type", _entrustType);
		data.put("Ftrade_direction", _tradeDirection);
		data.put("Fprice", String.valueOf(price));
		data.put("Fquantity", String.valueOf(quantity));
		data.put("Forder_time", String.valueOf(orderTime.getTime()));
		data.put("Fstatus", "delegating");
		
		boolean result =false; 
		try {
			dbInstance.insert("fenice.t_order", data);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 *  update order status
	 *  @param orderId, status
	 *  @return true/false
	 */
	public boolean updateOrderStatus(String orderId, String status) {
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Fstatus", DBOperationAdapter.filterSQL(status));
		String where = " WHERE Forder_id = '" + DBOperationAdapter.filterSQL(orderId) + "'";
		boolean result = false;
		try {
			dbInstance.update("fenice.t_order", data, where);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 *  agree this order
	 * 
	 */
	public boolean agreeOrder(String orderId) {
		return updateOrderStatus(orderId, "agreed");
	}
	
	/*
	 *  withdraw this order
	 * 
	 */
	public boolean withdrawOrder(String orderId) {
		return updateOrderStatus(orderId, "canceled");
	}
	
	/*
	 *  relate order and account
	 *  @param accountId, orderId
	 *  @return true/false
	 */
	public boolean relateOrderAndAccount(String accountId, String orderId) {
		
		HashMap<String, String> data = new HashMap<String, String> ();
		data.put("Faccount", DBOperationAdapter.filterSQL(accountId));
		data.put("Forder_id", DBOperationAdapter.filterSQL(orderId));
		
		boolean result = false;
		try {
			dbInstance.insert("fenice.t_account_order_relation", data);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 
	 * 
	 */
	public String getAccountId(String orderId) {
		
		String sql = "SELECT Faccount FROM fenice.t_account_order_relation WHERE Forder_id = ?";
		ResultSet rs = null;
		
		try {
			PreparedStatement preStmt = dbInstance.getConnection().prepareStatement(sql);
			preStmt.setString(1, DBOperationAdapter.filterSQL(orderId));
			rs = preStmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String accountId = null;
		try {
			if (rs.next()) {
				accountId = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountId;
	}
	
	public static void main(String[] args) {
		
		OrderOperation order = new OrderOperation();
		//order.newOrder("320925199308271416", "FENICE", "MARKET", "BID", 1, 1000000, new java.util.Date());
		//System.out.println(order.hasThisOrder("320925199308271417"));
		//System.out.println(order.withdrawOrder("320925199308271416"));
		//System.out.println(order.relateOrderAndAccount("000001", "320925199308271416"));
		//System.out.println(order.getAccountId("320925199308271416"));
		System.out.println(order.updateOrderStatus("B201505042359350000021000", "agreed"));
	}
}
