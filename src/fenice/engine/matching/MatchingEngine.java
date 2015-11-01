/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.matching;

import fenice.engine.orderbook.Order;
import fenice.engine.orderbook.OrderBookQueue;
import fenice.engine.orderbook.OrderBookQueueFactory;
import fenice.engine.quotation.TransactionBroadcast;
import fenice.models.SecurityOperation;

/*
 * core module
 * Description: Engine class
 */

public class MatchingEngine {
	
	protected String securityId;
	protected OrderBookQueue<Order> AskOrderBookQueue;
	protected OrderBookQueue<Order> BidOrderBookQueue;
	//public ExecutorService engineExecutor = Executors.newCachedThreadPool();
	public MatchingStrategy matchingStrategy;
	public Thread matchingThread;
	public FrontServerDispatcher dispatcher;
	public Thread frontServer;
	public TransactionBroadcast broadcast;
	public Thread broadcastServer;
	
	/*
	 * constructor
	 * 
	 */
	public MatchingEngine(String securityId) {
		setSecurityId(securityId);
		OrderBookQueueFactory<Order> factory = new OrderBookQueueFactory<Order>();
		setAskOrderBookQueue(factory.createQueue("ASK"));
		setBidOrderBookQueue(factory.createQueue("BID"));
		setMatchingStrategy(new StandardMatchingStrategy(securityId, AskOrderBookQueue, BidOrderBookQueue));
		//set opening price
		SecurityOperation security = new SecurityOperation();
		setOpeningPrice(security.getSecurityPrice(securityId));
		matchingThread = new Thread(matchingStrategy);	
		setDispatcher(new FrontServerDispatcher());
		frontServer = new Thread(dispatcher);
		setBroadcast(new TransactionBroadcast());
		broadcastServer = new Thread(broadcast);
	}
	
	/*
	 *  Description: getter & setter of securityId
	 * 
	 */
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
	
	public String getSecurityId() {
		return securityId;
	}
	
	/*
	 * Description: set opening price
	 * 
	 */
	public void setOpeningPrice(double openingPrice) {
		this.matchingStrategy.setOpeningPrice(openingPrice);
		this.matchingStrategy.setCurrentPrice(openingPrice);
	}
	

	/*
	 * Description: getter & setter of queues
	 * 
	 */
	

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
	
	public MatchingStrategy getMatchingStrategy() {
		return matchingStrategy;
	}
	
	public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
		this.matchingStrategy = matchingStrategy; 
	}
	
	public FrontServerDispatcher getDispatcher() {
		return dispatcher;
	}
	
	public void setDispatcher(FrontServerDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public TransactionBroadcast getBroadcast() {
		return broadcast;
	}
	
	public void setBroadcast(TransactionBroadcast broadcast) {
		this.broadcast = broadcast;
	}

	/*
	 * Description: matching worker
	 * 
	 */
	
	/*
	 * run matching thread
	 * 
	 */
	public void runEngine() {
		matchingThread.start();
	}
	
	/*
	 * whether the matching thread is running
	 * 
	 */
	public boolean isMatchingThreadAlive() {
		return matchingThread.isAlive();
	}
	
	/*
	 * set priority of matching thread
	 * 
	 */
	public void setMatchingThreadPriority(int priority) {
		matchingThread.setPriority(priority);
	}
	
	/*
	 * join the end of matching thread
	 * 
	*/
	public void joinMatchingThread() throws InterruptedException {
			matchingThread.join();
	}
	
	//////////////////////////////////////////////////////////////////////
	
	/*
	 * Description: request accept worker
	 * 
	 */
	
	/*
	 * run front server
	 * 
	 */
	public void runFrontServer() {
		frontServer.start();
	}
	
	/*
	 * whether the receiver thread is running
	 * 
	 */
	public boolean isFrontServerAlive() {
		return frontServer.isAlive();
	}
	
	/*
	 * set priority of receiver thread
	 * 
	 */
	public void setFrontServerThreadPriority(int priority) {
		frontServer.setPriority(priority);
	}
	
	/*
	 * join the end of front server thread
	 * 
	 */
	public void joinFrontServerThread() throws InterruptedException {
		frontServer.join();
	}
	
	//////////////////////////////////////////////////////////////////////
	/*
	 * Description: transaction delivery worker
	 * 
	 */
	
	/*
	 * run transaction broadcast server
	 * 
	 */
	public void runTransactionBroadcastServer() {
		broadcastServer.start();
	}
	
	/*
	 * whether the transaction broadcast thread is running
	 * 
	 */
	public boolean isTransactionBroadcastServerAlive() {
		return broadcastServer.isAlive();
	}
	
	/*
	 * set priority of transaction broadcast server
	 * 
	 */
	public void setTransactionBroadcastServerPriority(int priority) {
		broadcastServer.setPriority(priority);
	}
	
	/*
	 * join the end of transaction broadcast server thread
	 * 
	 */
	public void joinTransactionBroadcastServerThread() throws InterruptedException {
		broadcastServer.join();
	}
	
	
	public static void main(String[] args) {
		MatchingEngine engine = new MatchingEngine("FENICE");
		engine.runFrontServer();
		engine.runEngine();
		engine.runTransactionBroadcastServer();
	}
	
}
