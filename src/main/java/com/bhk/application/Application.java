package com.bhk.application;

import java.io.IOException;

import com.bhk.producer.MyProducer;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		try {
			app.executeKafkaProducer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void executeKafkaProducer() throws Exception{
		MyProducer prod = new MyProducer();
		prod.createProducer();
		for(int count = 0 ; count < 10 ; count++) {
			prod.createRecordAndSendData("Hello from Bhaskar - "+count, String.valueOf(count));
		}
		
		prod.closeProduser();
	}
}
