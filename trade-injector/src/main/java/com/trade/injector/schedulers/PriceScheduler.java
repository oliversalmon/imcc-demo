package com.trade.injector.schedulers;

import com.example.mu.domain.Instrument;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.trade.injector.business.service.GeneratePriceData;
import com.trade.injector.sinks.KafkaSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PriceScheduler {

	final Logger LOG = LoggerFactory.getLogger(PriceScheduler.class);
	private static final String REPORT_NAME = "MASTER_REPORT";

	@Autowired
	private KafkaSink sender;

	@Autowired
	HazelcastInstance hazelcastInstance;

	@Value("${kafka.topic.marketData}")
	private String marketDataTopic;

	

	@Autowired
	private MongoTemplate coreTemplate;

	/**
	 * This scheduler will generate prices every 5 minute for instruments that
	 * currently exist within the cache
	 *
	 */

	@Scheduled(fixedDelay = 400000)
	public void generateFrequentPriceData() {

		LOG.info("Starting to generate prices ");
		// first get all the prices from the cache
		IMap<String, Instrument> mapInstruments = hazelcastInstance
				.getMap("instrument");

		// no data do not generate
		if (mapInstruments.size() == 0) {
			LOG.warn("No instruments found to generate prices");
			return;
		}

		// generate and sink to Kafka
		//Predicate predicate = new SqlPredicate(String.format("instrumentId like %s", "ELECU"));
		//Collection<Instrument> ins = mapInstruments.values(predicate);
		Collection<Instrument> ins = mapInstruments.values();
		LOG.info("Number of Instruments " + ins.size());
		ins.stream().forEach(
				a -> sender.send(marketDataTopic, a.getInstrumentId(), GeneratePriceData
						.generateRandomDataOnInstruments(a).toJSON()));

		LOG.info("Price generation done");
	}
	
	
	

}
