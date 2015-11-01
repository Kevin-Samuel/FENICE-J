/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.quotation;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.TWaverUtil;
import twaver.chart.LineChart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import fenice.fix.business.SingleTransactionGenerator;

public class TransactionBroadcast implements Runnable {
	
	private static String TRANSACTION_BROADCAST_QUEUE = "TRANSACTION_BROADCAST_QUEUE";
	private static QueueingConsumer orderReceiver = null;
	
	public static String getTRANSACTION_BROADCAST_QUEUE() {
		return TRANSACTION_BROADCAST_QUEUE;
	}

	public static void setTRANSACTION_BROADCAST_QUEUE(
			String tRANSACTION_BROADCAST_QUEUE) {
		TRANSACTION_BROADCAST_QUEUE = tRANSACTION_BROADCAST_QUEUE;
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
			channel.queueDeclare(TRANSACTION_BROADCAST_QUEUE, true, false, false, null);
			channel.basicConsume(TRANSACTION_BROADCAST_QUEUE, true, orderReceiver);
			return orderReceiver;
		}
	}

	@Override
	public void run() {
		
		//
		TDataBox priceBox = new TDataBox();
		LineChart dealPriceChart = new LineChart(priceBox);
		dealPriceChart.setYAxisVisible(true);
		dealPriceChart.setYScaleTextVisible(true);
		dealPriceChart.setXAxisVisible(true);
		dealPriceChart.setXScaleTextVisible(true);
		dealPriceChart.setInflexionVisible(true);
		//dealPriceChart.setXScaleTextSpanCount(10);
		dealPriceChart.setXScaleTextOrientation(TWaverConst.LABEL_ORIENTATION_RIGHT);
		
		Element dealPrice = new Node();
		dealPrice.setName("Price");
		dealPrice.putChartColor(Color.RED);
		dealPrice.putChartInflexionStyle(TWaverConst.INFLEXION_STYLE_TRIANGLE);
		priceBox.addElement(dealPrice);
		
		ArrayList<String> dealTime = new ArrayList<String>();
		dealPriceChart.setXScaleTextList(dealTime);
		
		TDataBox quantityBox = new TDataBox();
		LineChart dealQuantityChart = new LineChart(quantityBox);
		dealQuantityChart.setEnableYTranslate(false);
		dealQuantityChart.setEnableYZoom(false);
		dealQuantityChart.setLineType(TWaverConst.LINE_TYPE_POLE);
		dealQuantityChart.setLowerLimit(0);
		//dealQuantityChart.setXScaleTextSpanCount(10);
		dealQuantityChart.setXScaleTextOrientation(TWaverConst.LABEL_ORIENTATION_RIGHT);
		
		Element dealQuantity = new Node();
		dealQuantity.setName("Quantity");
		dealQuantity.putChartColor(Color.GREEN);
		dealQuantity.putChartInflexionStyle(TWaverConst.INFLEXION_STYLE_TRIANGLE);
		quantityBox.addElement(dealQuantity);
		
		dealQuantityChart.setXScaleTextList(dealTime);
		
		JPanel pane = new JPanel(new GridLayout(2, 1));
		pane.add(dealPriceChart);
		pane.add(dealQuantityChart);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setContentPane(pane);
		TWaverUtil.centerWindow(frame);
		frame.setVisible(true);
		//
		
		
		try {
			
			QueueingConsumer orderreceiver = getOrderReceiver();
			System.out.println(" [x] receive transaction from matching-engine");
			while (true) {
				//receive the deal info from transaction queue 
				QueueingConsumer.Delivery delivery = orderreceiver.nextDelivery();
				byte[] message = delivery.getBody();
				String transactionStr = new String(message);
				System.out.println(transactionStr);
				Transaction deal = SingleTransactionGenerator.convertMsgToTransaction(transactionStr);
				deal.printTransaction();
				dealPrice.addChartValue(deal.getDealPrice());
				@SuppressWarnings("deprecation")
				int hour = deal.getDealTime().getHours();
				@SuppressWarnings("deprecation")
				int minute = deal.getDealTime().getMinutes();
				dealTime.add(LocalTime.of(hour, minute).toString());
				dealQuantity.addChartValue(deal.getDealQuantity());
				
			}
		} catch(IOException | InterruptedException | ParseException ex) {
			ex.printStackTrace();
		}
		
	}
}
