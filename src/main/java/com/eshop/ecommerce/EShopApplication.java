package com.eshop.ecommerce;

import com.eshop.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EShopApplication implements CommandLineRunner {
	@Autowired
	public ProductService productService;

	public static void main(String[] args) {
		SpringApplication.run(EShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!dataAlreadyImported()) {
			productService.importDataFromCsv("src/main/resources/csv_files/Products.csv");
		} else {
			System.out.println("Data has already been imported. Skipping import.");
		}
	}
	private boolean dataAlreadyImported() {
		return !productService.getAllProducts().isEmpty();
	}
}
