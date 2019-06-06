package io.pivotal.workshops.pkskafka.analytics.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.workshops.pkskafka.analytics.AnalyticsBindings;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;

@RestController
public class ProductCountController {
	
	private final InteractiveQueryService queryProductCountStore;
	
	public ProductCountController (InteractiveQueryService queryProductCountStore)
	{
		this.queryProductCountStore=queryProductCountStore;
	}

	
	@GetMapping("/counts")
	public List<ProductDTO> counts() {
		KeyValue<String, Long> productCount = null;
  		ProductDTO productDTO = null;
  	
		Map<String, Long> counts = new HashMap<String, Long>();
		KeyValueIterator<String, Long> productIterator = null;
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		try {
	  		final ReadOnlyKeyValueStore<String, Long> productCountStore =
	  				queryProductCountStore.getQueryableStore(AnalyticsBindings.PRODUCT_COUNT_MV, 
							QueryableStoreTypes.<String, Long>keyValueStore());
	  		productIterator = productCountStore.all();
	  		while (productIterator.hasNext())
	  		{
	  			productDTO = new ProductDTO();
	  			productCount = productIterator.next();
	  			productDTO.setOrderCount(productCount.value);
	  			productDTO.setProductSku(productCount.key);
	  			productList.add(productDTO);
	  			
	  		}
		  		
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  		}
	  		
	  		return productList;
	
	}
}