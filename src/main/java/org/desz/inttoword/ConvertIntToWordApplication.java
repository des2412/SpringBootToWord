package org.desz.inttoword;

import java.util.function.Function;

import org.desz.inttoword.conversion.functions.ConversionFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConvertIntToWordApplication {

	@Autowired
	private ConversionFunction conversionDelegate;

	public static void main(String[] args) {
		SpringApplication.run(ConvertIntToWordApplication.class, args);
	}

	@Bean
	public Function<IntToWordRequest, String> myFunction() {
		return req -> conversionDelegate.apply(req);
	}

}
