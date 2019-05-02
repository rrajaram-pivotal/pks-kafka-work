package io.pivotal.workshops.pkskafka.analytics.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.workshops.pkskafka.analytics.bindings.AnalyticsBindings;

@RestController
public class ProductCountController {
	
	private final QueryableStoreRegistry queryableStoreRegistry;
	
	public ProductCountController (QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry=queryableStoreRegistry;
	}

	
	@GetMapping("/counts")
	Map<String, Long> counts() {
			Map<String, Long> counts = new HashMap<String, Long>();
			ReadOnlyKeyValueStore<String, Long> queryableStoreType =
				this.queryableStoreRegistry.getQueryableStoreType(AnalyticsBindings.PRODUCT_COUNT_MV, QueryableStoreTypes.keyValueStore());
			KeyValueIterator<String, Long> all = queryableStoreType.all();
			while (all.hasNext()) {
					KeyValue<String, Long> value = all.next();
					counts.put(value.key, value.value);
			}
			return counts;
	}
}
