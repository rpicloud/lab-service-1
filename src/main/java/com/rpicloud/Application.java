package com.rpicloud;

import com.rpicloud.models.Resource1;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping(value = "/resources")
	public ResponseEntity<List<Resource1>> resources() {
		List<Resource1> resources = new ArrayList<>();
		resources.add(new Resource1("Dummy data 1"));
		resources.add(new Resource1("Dummy data 2"));
		resources.add(new Resource1("Dummy data 3"));
		resources.add(new Resource1("Dummy data 4"));
		resources.add(new Resource1("Dummy data 5"));


		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
}
