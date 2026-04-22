package com.thehecklers.sburrestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import javax.persistence.*;
import java.util.Optional;

@SpringBootApplication
public class SburRestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SburRestDemoApplication.class, args);
	}
}

@CrossOrigin(origins = {"http://localhost:8080","http://127.0.0.1:5500"})
@RestController
@RequestMapping("/coffees")
class RestApiDemoController {

	private final CoffeeRepository coffeeRepository;

	public RestApiDemoController(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffeeRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable Long id) {
		return coffeeRepository.findById(id);
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable Long id, @RequestBody Coffee coffee) {

		return coffeeRepository.findById(id)
				.map(existingCoffee -> {
					existingCoffee.setName(coffee.getName());
					coffeeRepository.save(existingCoffee);
					return ResponseEntity.ok(existingCoffee);
				})
				.orElseGet(() -> {
					Coffee newCoffee = coffeeRepository.save(coffee);
					return ResponseEntity.ok(newCoffee);
				});
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable Long id) {
		coffeeRepository.deleteById(id);
	}
}

@Entity
class Coffee {

	@Id
	@GeneratedValue
	public Long id;

	public String name;

	public Coffee() {}

	public Coffee(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}