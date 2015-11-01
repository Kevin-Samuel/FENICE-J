/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */


package fenice.engine.matching;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import fenice.engine.orderbook.EntrustType;
import fenice.engine.orderbook.Order;
import fenice.engine.orderbook.OrderBookQueue;
import fenice.engine.orderbook.OrderBookQueueFactory;
import fenice.engine.orderbook.OrderFactory;
import fenice.engine.orderbook.TradeDirection;
import fenice.engine.delivery.DeliveryThread;
import fenice.engine.quotation.Transaction;
import fenice.engine.quotation.TransactionPump;
import fenice.fix.business.OrderSingleRequest;
import fenice.models.OrderOperation;

public class StandardMatchingStrategy extends MatchingStrategy {

	private static String ORDER_RECEIVE_QUEUE = "ORDER_RECEIVE_QUEUE";
	private static QueueingConsumer orderReceiver = null;
	public OrderVerifier orderVerifier;
	public OrderOperation orderOper;
	
	public StandardMatchingStrategy(
			String securityId,
			OrderBookQueue<Order> askOrderBookQueue,
			OrderBookQueue<Order> bidOrderBookQueue) {
		super(securityId, askOrderBookQueue, bidOrderBookQueue);
		// 
		orderVerifier = new OrderVerifier(getSecurityId());
		orderOper = new OrderOperation();
		//init the price param
		minPrice = getOpeningPrice();
		maxPrice = getOpeningPrice();
		changeRatio = 0.0;
	}

	/*
	 * execute this function after thread start
	 * @see fenice.engine.matchingengine.MatchingStrategy#run()
	 */
	@Override
	public void run() {
		
		try {
			QueueingConsumer orderreceiver = getOrderReceiver();
			System.out.println(" [x] receive order from mom-server");
			while (true) {
				//accept order message and parse it to order
				QueueingConsumer.Delivery delivery = orderreceiver.nextDelivery();
				byte[] message = delivery.getBody();
				String orderStr = new String(message);
				System.out.println(orderStr);
				Order order = OrderSingleRequest.convertMsgToOrder(orderStr);	
				//matching this order
				if (orderVerifier.checkOrder(order) == true) {
					orderOper.newOrder(order.getOrderId(), order.getSecurityId() , order.getEntrustType(), order.getTradeDirection(), order.getPrice(), order.getQuantity(), order.getOrderTime());
					orderOper.relateOrderAndAccount(order.getAccountId(), order.getOrderId());
					this.match(order);
				}
			}
		} catch (IOException | InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*
	 *  <Singleton> get the instance of OrderReceiver (Queue consumer)
	 *  
	 */
	public static QueueingConsumer getOrderReceiver() throws IOException, InterruptedException {
		if (orderReceiver != null) {
			return orderReceiver;
		} else {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			orderReceiver = new QueueingConsumer(channel);
			channel.queueDeclare(ORDER_RECEIVE_QUEUE, true, false, false, null);
			channel.basicConsume(ORDER_RECEIVE_QUEUE, true, orderReceiver);
			return orderReceiver;
		}
	}
	
	/*
	 * standard match strategy
	 * 
	 */
	@Override
	public void match(Order orderbook) {
		TradeDirection tradeDirection = orderbook.getTradeDirection();
		switch(tradeDirection) {
			case BID : 
				matchBid(orderbook);
				break;
			case ASK:
				matchAsk(orderbook);
				break;
		}
	}
	
	public void matchBid(Order orderbook) {
		
		EntrustType entrustType = orderbook.getEntrustType();
		if(entrustType == EntrustType.MARKET) {
					
			while(orderbook.getQuantity() > 0 && AskOrderBookQueue.size() > 0) {
				
				Order askOrderBook = AskOrderBookQueue.poll();
				int dealQuantity = 0;
				double dealPrice = currentPrice;
				
				if(orderbook.getQuantity() > askOrderBook.getQuantity()) {
					dealQuantity = askOrderBook.getQuantity();
					orderbook.setQuantity(orderbook.getQuantity() - dealQuantity);
					//
					if (askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;
					} else if(askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = askOrderBook.getPrice();
					}
		
				} else if(orderbook.getQuantity() < askOrderBook.getQuantity()) {
					dealQuantity = orderbook.getQuantity();
					//update bid order book
					orderbook.setQuantity(0);
					
					if(askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;		
					} else if (askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = askOrderBook.getPrice();
					}
					//update ask order book
					askOrderBook.setQuantity(askOrderBook.getQuantity() - dealQuantity);
					AskOrderBookQueue.offer(askOrderBook);
					
				} else {
					dealQuantity = askOrderBook.getQuantity();
					orderbook.setQuantity(0);
					askOrderBook.setQuantity(0);
					
					if(askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;	
					} else if(askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = askOrderBook.getPrice();
					}
					
				}
				
				setCurrentPrice(dealPrice);
				updateTransaction(currentPrice);
				
				//printDealInfo(orderbook, askOrderBook, dealPrice, dealQuantity, new Date());
		
				new Thread(new DeliveryThread(orderbook.getOrderId(), askOrderBook.getOrderId(), orderbook.getSecurityId(), dealPrice, dealQuantity)).start();
				Transaction currentDeal = getDealGenerator().newTransaction(orderbook.getSecurityId(), orderbook.getSecurityName(), orderbook.getOrderId(), askOrderBook.getOrderId(), dealPrice, dealQuantity);
				TransactionPump pump;
				try {
					pump = new TransactionPump(currentDeal);
					new Thread(pump).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} else if (entrustType == EntrustType.LIMIT) {
			
			while (orderbook.getQuantity() > 0 && AskOrderBookQueue.size() > 0) {
				
				Order askOrderBook = AskOrderBookQueue.poll();
				int dealQuantity = 0;
				double dealPrice = currentPrice;
				
				if (askOrderBook.getEntrustType() == EntrustType.LIMIT && orderbook.getPrice() < askOrderBook.getPrice()) {
					AskOrderBookQueue.offer(askOrderBook);
					break;
				}
				
				
				if (orderbook.getQuantity() > askOrderBook.getQuantity()) {
					dealQuantity = askOrderBook.getQuantity();
					orderbook.setQuantity(orderbook.getQuantity() - dealQuantity);
					//
					if (askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getPrice();
					} else if (askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), askOrderBook.getPrice()});
					}
						
				} else if (orderbook.getQuantity() < askOrderBook.getQuantity()) {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					
					if (askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getPrice();
					} else if (askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), askOrderBook.getPrice()});
					}
					askOrderBook.setQuantity(askOrderBook.getQuantity() - dealQuantity);
					AskOrderBookQueue.offer(askOrderBook);
				} else {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					askOrderBook.setQuantity(0);
						
					if (askOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getPrice();
					} else if (askOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), askOrderBook.getPrice()});
					}
				}
					
				setCurrentPrice(dealPrice);
				updateTransaction(currentPrice);
				//printDealInfo(orderbook, askOrderBook, dealPrice, dealQuantity, new Date());
				new Thread(new DeliveryThread(orderbook.getOrderId(), askOrderBook.getOrderId(), orderbook.getSecurityId(), dealPrice, dealQuantity)).start();
				Transaction currentDeal = getDealGenerator().newTransaction(orderbook.getSecurityId(), orderbook.getSecurityName(), orderbook.getOrderId(), askOrderBook.getOrderId(), dealPrice, dealQuantity);
				TransactionPump pump;
				try {
					pump = new TransactionPump(currentDeal);
					new Thread(pump).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		if (orderbook.getQuantity() > 0) {
			BidOrderBookQueue.offer(orderbook);
		}
	}
	
	public void matchAsk(Order orderbook) {
		EntrustType entrustType = orderbook.getEntrustType();
		
		if (entrustType == EntrustType.MARKET) {
			
			while (orderbook.getQuantity() > 0 && BidOrderBookQueue.size() > 0) {
				
				Order bidOrderBook = BidOrderBookQueue.poll();
				int dealQuantity = 0;
				double dealPrice = currentPrice;
				
				if (orderbook.getQuantity() > bidOrderBook.getQuantity()) {
					dealQuantity = bidOrderBook.getQuantity();
					orderbook.setQuantity(orderbook.getQuantity() - dealQuantity);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = bidOrderBook.getPrice();
					}
					
				} else if (orderbook.getQuantity() < bidOrderBook.getQuantity()) {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = bidOrderBook.getPrice();
					}
					
					bidOrderBook.setQuantity(bidOrderBook.getQuantity() - dealQuantity);
					BidOrderBookQueue.offer(bidOrderBook);
				} else {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					bidOrderBook.setQuantity(0);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = currentPrice;
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = bidOrderBook.getPrice();
					}
				}
				
				setCurrentPrice(dealPrice);
				updateTransaction(currentPrice);
				//printDealInfo(bidOrderBook, orderbook, dealPrice, dealQuantity, new Date());
				new Thread(new DeliveryThread( bidOrderBook.getOrderId(), orderbook.getOrderId(), orderbook.getSecurityId(), dealPrice, dealQuantity)).start();
				Transaction currentDeal = getDealGenerator().newTransaction(orderbook.getSecurityId(), orderbook.getSecurityName(), bidOrderBook.getOrderId(), orderbook.getOrderId(), dealPrice, dealQuantity);
				TransactionPump pump;
				try {
					pump = new TransactionPump(currentDeal);
					new Thread(pump).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		} else if (entrustType == EntrustType.LIMIT) {
			
			while (orderbook.getQuantity() > 0 && BidOrderBookQueue.size() > 0) {
				
				Order bidOrderBook = BidOrderBookQueue.poll();
				int dealQuantity = 0;
				double dealPrice = currentPrice;
				
				if (bidOrderBook.getEntrustType() == EntrustType.LIMIT && orderbook.getPrice() > bidOrderBook.getPrice()) {
					BidOrderBookQueue.offer(bidOrderBook);
					break;
				}
				
				if (orderbook.getQuantity() > bidOrderBook.getQuantity()) {
					dealQuantity = bidOrderBook.getQuantity();
					orderbook.setQuantity(orderbook.getQuantity() - dealQuantity);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getPrice();
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), bidOrderBook.getPrice()});
					}
					
				} else if (orderbook.getQuantity() < bidOrderBook.getQuantity()) {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getPrice();
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), bidOrderBook.getPrice()});
					}
					
					bidOrderBook.setQuantity(bidOrderBook.getQuantity() - dealQuantity);
					BidOrderBookQueue.offer(bidOrderBook);
				} else {
					dealQuantity = orderbook.getQuantity();
					orderbook.setQuantity(0);
					bidOrderBook.setQuantity(0);
					
					if (bidOrderBook.getEntrustType() == EntrustType.MARKET) {
						dealPrice = orderbook.getQuantity();
					} else if (bidOrderBook.getEntrustType() == EntrustType.LIMIT) {
						dealPrice = middlePrice(new double[] {currentPrice, orderbook.getPrice(), bidOrderBook.getPrice()});
					}
					
				}
				
				setCurrentPrice(dealPrice);
				updateTransaction(currentPrice);
				//printDealInfo(bidOrderBook, orderbook, dealPrice, dealQuantity, new Date());
				new Thread(new DeliveryThread( bidOrderBook.getOrderId(), orderbook.getOrderId(), orderbook.getSecurityId(), dealPrice, dealQuantity)).start();
				Transaction currentDeal = getDealGenerator().newTransaction(orderbook.getSecurityId(), orderbook.getSecurityName(), bidOrderBook.getOrderId(), orderbook.getOrderId(), dealPrice, dealQuantity);
				TransactionPump pump;
				try {
					pump = new TransactionPump(currentDeal);
					new Thread(pump).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (orderbook.getQuantity() > 0 ) {
			AskOrderBookQueue.offer(orderbook);
		}
	}
	
	/*
	 * print the information of deal
	 * @param order bid, order ask, dealPrice, dealQuantity, dealTime
	 */
	public static void printDealInfo(Order bidorderbook, Order askorderbook, double dealPrice, int dealQuantity, Date dealTime) {
		System.out.println("|-----------------------------------" + dealTime.toString() + "-----------------------------|");
		System.out.println("| bid order book ID: " + String.format("%s", bidorderbook.getOrderId()) + "  | ask order book ID: " + askorderbook.getOrderId() + "  |");
		System.out.println("| security ID: " + String.format("%-30s", bidorderbook.getSecurityId()) + "| security Name: " + String.format("%-30s", bidorderbook.getSecurityName()) + " |");
		System.out.println("| deal price : " + String.format("%-77s", String.valueOf(dealPrice)) + " |");
		System.out.println("| deal quantity : " + String.format("%-74s", String.valueOf(dealQuantity)) + " |");
		System.out.println("|--------------------------------------------------------------------------------------------|");
	}
	
	
	/*
	 * find the middle number in three numbers
	 * 
	 */

	public static double middlePrice(double[] args) {
		Arrays.sort(args);
		return args[1];
	}
	
	/*
	 * @test matching strategy
	 * 
	 */
	public static void main(String[] args) {
		OrderBookQueueFactory<Order> factory = new OrderBookQueueFactory<Order>();
		OrderBookQueue<Order> bidqueue = factory.createQueue("BID");
		OrderBookQueue<Order> askqueue = factory.createQueue("ASK");
		
		OrderFactory orderfactory = new OrderFactory();
		Order[] bidorder = new Order[6];
		bidorder[0] = orderfactory.newLimitOrder("fenice", "000002", "WKA", 100, 7.1, OrderFactory.BID);
		bidorder[1] = orderfactory.newLimitOrder("kevin", "000002", "WKA", 100, 7.0, OrderFactory.BID);
		bidorder[2] = orderfactory.newLimitOrder("samuel", "000002", "WKA", 100, 6.9, OrderFactory.BID);
		bidorder[3] = orderfactory.newLimitOrder("kobe", "000002", "WKA", 100, 6.6, OrderFactory.BID);
		bidorder[4] = orderfactory.newLimitOrder("james", "000002", "WKA", 100, 6.8, OrderFactory.BID);
		bidorder[5] = orderfactory.newLimitOrder("pual", "000002", "WKA", 100, 6.7, OrderFactory.BID);
		
		for (int i = 0; i < bidorder.length; i++) {
			bidqueue.offer(bidorder[i]);
		}
		
		Order[] askorder = new Order[5];
		askorder[0] = orderfactory.newLimitOrder("jordan", "000002", "WKA", 100, 7.6, OrderFactory.ASK);
		askorder[1] = orderfactory.newLimitOrder("carter", "000002", "WKA", 100, 7.3, OrderFactory.ASK);
		askorder[2] = orderfactory.newLimitOrder("garnett", "000002", "WKA", 100, 7.2, OrderFactory.ASK);
		askorder[3] = orderfactory.newLimitOrder("onill", "000002", "WKA", 100, 7.5, OrderFactory.ASK);
		askorder[4] = orderfactory.newLimitOrder("ducan", "000002", "WKA", 100, 7.4, OrderFactory.ASK);
		
		for (int i = 0; i < askorder.length; ++i) {
			askqueue.offer(askorder[i]);
		}
		
		StandardMatchingStrategy strategy = new StandardMatchingStrategy("FENICE", askqueue, bidqueue);
		strategy.setCurrentPrice(7.0);
		Order neworderbook1 = orderfactory.newLimitOrder("owen", "000002", "WKA", 150, 7.3, OrderFactory.BID);
		
		strategy.match(neworderbook1);
		
		Order neworderbook2 = orderfactory.newMarketOrder("yanwei", "000002", "WKA", 150, OrderFactory.BID);
		strategy.match(neworderbook2);
	
		Order neworderbook3 = orderfactory.newLimitOrder("lisp", "000002", "WKA", 100, 6.5, OrderFactory.BID);
		strategy.match(neworderbook3);
		
		while(bidqueue.size() > 0) {
			Order book = bidqueue.poll();
			book.printOrder();
		}
	}
}


