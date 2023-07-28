package spring.httpparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import spring.httpparking.manager.GestoreParcheggio;

@SpringBootApplication
public class HttpParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpParkingApplication.class, args);
		System.out.println("funziona così https://localhost:3000");
	}


}
