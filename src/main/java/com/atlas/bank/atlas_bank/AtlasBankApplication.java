package com.atlas.bank.atlas_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
// Activa la caché declarativa de Spring. En una llamada a un método anotado con
// @Cacheable, Spring comprueba primero la caché: si encuentra el resultado, lo
// devuelve; si no, ejecuta el método y guarda el resultado para la próxima llamada.
// CachedAccountService usa una caché manual con ConcurrentHashMap, independiente de esta.
@EnableCaching
public class AtlasBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtlasBankApplication.class, args);
	}

}
