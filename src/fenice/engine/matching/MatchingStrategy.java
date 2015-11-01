/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.matching;

import fenice.engine.orderbook.Order;
import fenice.engine.orderbook.OrderBookQueue;
import fenice.engine.quotation.TransactionFactory;


/*
 * Abstract Matching Strategy
 * 
 */
public class MatchingStrategy implements Runnable {
	
	protected String securityId;
	protected OrderBookQueue<Order> AskOrderBookQueue;
	protected OrderBookQueue<Order> BidOrderBookQueue;
	protected double openingPrice;
	protected double currentPrice;
	protected double minPrice;
	protected double maxPrice;
	protected double changeRatio;
	
	private TransactionFactory dealGenerator;
	
	public MatchingStrategy(String securityId, OrderBookQueue<Order> askOrderBookQueue, OrderBookQueue<Order> bidOrderBookQueue) {
		setSecurityId(securityId);
		setAskOrderBookQueue(askOrderBookQueue);
		setBidOrderBookQueue(bidOrderBookQueue);
		setDealGenerator(new TransactionFactory());
	}

	public String getSecurityId() {
		return securityId;
	}
	
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
	
	public OrderBookQueue<Order> getAskOrderBookQueue() {
		return AskOrderBookQueue;
	}

	public void setAskOrderBookQueue(OrderBookQueue<Order> askOrderBookQueue) {
		AskOrderBookQueue = askOrderBookQueue;
	}

	public OrderBookQueue<Order> getBidOrderBookQueue() {
		return BidOrderBookQueue;
	}

	public void setBidOrderBookQueue(OrderBookQueue<Order> bidOrderBookQueue) {
		BidOrderBookQueue = bidOrderBookQueue;
	}
	
	public double getOpeningPrice() {
		return openingPrice;
	}
	
	public void setOpeningPrice(double openingPrice) {
		this.openingPrice = openingPrice;
	}
	
	public double getCurrentPrice() {
		return currentPrice;
	}
	
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public double getMinPrice() {
		return minPrice;
	}
	
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	
	public void updateMinPrice(double currentPrice) {
		if (getCurrentPrice() < getMinPrice()) {
			setMinPrice(getCurrentPrice());
		}
	}
	
	public double getMaxPrice() {
		return maxPrice;
	}
	
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public void updateMaxPrice(double currentPrice) {
		if (getCurrentPrice() > getMaxPrice()) {
			setMaxPrice(getCurrentPrice());
		}
	}
	
	public double getChangeRatio() {
		return changeRatio;
	}
	
	public void updateChangeRatio() {
		this.changeRatio = (this.currentPrice - this.openingPrice) / this.openingPrice;
	}
	
	public void updateTransaction(double currentPrice) {
		setCurrentPrice(currentPrice);
		updateChangeRatio();
		updateMinPrice(currentPrice);
		updateMaxPrice(currentPrice);
	}
	
	public TransactionFactory getDealGenerator() {
		return dealGenerator;
	}

	public void setDealGenerator(TransactionFactory dealGenerator) {
		this.dealGenerator = dealGenerator;
	}

	/*
	 * operate this function after thread start
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	}
	
	
	/*
	 * core function, override this function in sub class
	 * 
	 */
	public void match(Order orderbook) {
		
		
	}
}
