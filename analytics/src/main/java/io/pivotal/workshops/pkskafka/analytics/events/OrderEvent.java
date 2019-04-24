package io.pivotal.workshops.pkskafka.analytics.events;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an order event 
 * @author rrajaram
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
	
	private Integer orderId;
	private String userId;
	private String productId;
	private Number amount;
	
	
}
