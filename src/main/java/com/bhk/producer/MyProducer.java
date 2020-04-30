package com.bhk.producer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProducer {
	
	Properties prop = new Properties();
	Properties kafkaProp = new Properties();
	KafkaProducer<String, String> kafkaProducer = null;
	
	public MyProducer() throws IOException{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		prop.load(cl.getResourceAsStream("application.properties"));
		kafkaProp.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProperty("kafka.bootstrapServer"));
		kafkaProp.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProp.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	}
	
	public void createProducer() {
		kafkaProducer = new KafkaProducer<String, String>(kafkaProp);		
	}
	
	public int createRecordAndSendData(String strMessage, String key) throws ExecutionException, InterruptedException {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(prop.getProperty("kafka.topicName").toString(), key, strMessage);  
		Logger logger=LoggerFactory.getLogger(MyProducer.class);   
		logger.info("strMessage :: "+strMessage);
		kafkaProducer.send(record, (recordMetadata, exception) -> { 		         
		        if (exception== null) {  
		        	logger.info("Successfully received the details as: \n" +  
		                    "Topic:" + recordMetadata.topic() + "\n" +  
		                    "Partition:" + recordMetadata.partition() + "\n" +  
		                    "Offset" + recordMetadata.offset() + "\n" +  
		                    "Timestamp" + recordMetadata.timestamp());  
		                      }  
		  
		         else {  
		            logger.error("Can't produce,getting error",exception);  
		  
		        } 
		}).get(); 
		
		kafkaProducer.flush();
		
		return 0;
	}
	
	public void closeProduser() {
		kafkaProducer.close();
	}
}
