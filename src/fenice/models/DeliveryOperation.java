/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-4-20
 * @Version : 1.0.1
 */
package fenice.models;

import java.sql.SQLException;
import java.util.HashMap;

public class DeliveryOperation extends AbstractModelOperation{
	
	public DeliveryOperation() {
		super();
	}
	
	
	/*
	 *  add trade record
	 *  @param bidId, askId, status, dealTime, price, quantity
	 *  @return true/false
	 */
	public boolean addTradeRecord(String bidId, String askId, String status, java.util.Date dealTime, double price, int quantity) {
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Fbid_order_id", DBOperationAdapter.filterSQL(bidId));
		data.put("Fask_order_id", DBOperationAdapter.filterSQL(askId));
		data.put("Fstatus", status);
		data.put("Fdeal_time", String.valueOf(dealTime.getTime()));
		data.put("Fprice", String.valueOf(price));
		data.put("Fquantity", String.valueOf(quantity));
		
		boolean result = false;
		try {
			dbInstance.insert("fenice.t_clear_and_delivery", data);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	
	/*
	 *  add deal record 
	 *  @param accountId, tradeDirection, securityId, price, quantity, dealTime, status
	 *  @return true/false
	 */
	public boolean addDealRecord(String accountId, String tradeDirection, String securityId, double price, int quantity, java.util.Date dealTime, String status) {
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Faccount", DBOperationAdapter.filterSQL(accountId));
		data.put("Ftrade_direction", DBOperationAdapter.filterSQL(tradeDirection));
		data.put("Fsecurity_id", DBOperationAdapter.filterSQL(securityId));
		data.put("Fprice", String.valueOf(price));
		data.put("Fquantity", String.valueOf(quantity));
		data.put("Fdeal_time", String.valueOf(dealTime.getTime()));
		data.put("Fstatus", DBOperationAdapter.filterSQL(status));
		
		boolean result = false;
		try {
			dbInstance.insert("fenice.t_deal_record", data);
			result = true;
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		DeliveryOperation delivery = new DeliveryOperation();
		delivery.addDealRecord("000001", "BID", "FENICE", 10, 100000, new java.util.Date(), "agreed");
	}
	
}
