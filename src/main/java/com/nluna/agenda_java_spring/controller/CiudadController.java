package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.Ciudad;
import com.nluna.agenda_java_spring.service.ICiudadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
@Tag(name = "Ciudad",
        description = "Ciudad Controller")
@Slf4j
public class CiudadController {

    private final ICiudadService ciudadService;

    public CiudadController(ICiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public ResponseEntity<List<Ciudad>> listarCiudades() {
        log.info("GET /api/ciudades");
        return ResponseEntity.ok(ciudadService.CityList());
    }

    @GetMapping("/{idCiudad}")
    public ResponseEntity<Ciudad> buscarCiudadPorId(@PathVariable Integer idCiudad) {
        log.info("GET /api/ciudades/{}", idCiudad);
        Ciudad ciudad = ciudadService.findCityById(idCiudad);

        if (ciudad == null) {
            log.warn("Ciudad not found id={}", idCiudad);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ciudad);
    }

    @PostMapping
    public ResponseEntity<Ciudad> crearCiudad(@RequestBody Ciudad ciudad) {
        log.info("POST /api/ciudades nombre={} provincia={} pais={}", ciudad.getNombre(), ciudad.getProvincia(), ciudad.getPais());
        ciudad.setIdCiudad(null);
        ciudadService.saveCity(ciudad);

        return ResponseEntity.status(HttpStatus.CREATED).body(ciudad);
    }

    @PutMapping("/{idCiudad}")
    public ResponseEntity<Ciudad> actualizarCiudad(
            @PathVariable Integer idCiudad,
            @RequestBody Ciudad ciudadActualizada
    ) {
        log.info("PUT /api/ciudades/{}", idCiudad);
        Ciudad ciudad = ciudadService.findCityById(idCiudad);

        if (ciudad == null) {
            log.warn("Ciudad not found for update id={}", idCiudad);
            return ResponseEntity.notFound().build();
        }

        ciudad.setNombre(ciudadActualizada.getNombre());
        ciudad.setProvincia(ciudadActualizada.getProvincia());
        ciudad.setPais(ciudadActualizada.getPais());
        ciudadService.saveCity(ciudad);

        return ResponseEntity.ok(ciudad);
    }

    @DeleteMapping("/{idCiudad}")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Integer idCiudad) {
        log.info("DELETE /api/ciudades/{}", idCiudad);
        Ciudad ciudad = ciudadService.findCityById(idCiudad);

        if (ciudad == null) {
            log.warn("Ciudad not found for delete id={}", idCiudad);
            return ResponseEntity.notFound().build();
        }

        ciudadService.deleteCity(ciudad);

        return ResponseEntity.noContent().build();
    }
}
