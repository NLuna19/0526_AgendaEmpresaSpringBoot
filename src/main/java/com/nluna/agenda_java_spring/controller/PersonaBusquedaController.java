package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.Persona;
import com.nluna.agenda_java_spring.service.IPersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@Tag(name = "Busquedas de persona", description = "")
@Slf4j
public class PersonaBusquedaController {

    private final IPersonaService personaService;

    public PersonaBusquedaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/busqueda/persona")
    @Operation(
            summary = "Buscar personas por datos personales",
            description = "Busca personas aplicando filtros opcionales sobre campos propios de persona."
    )
    public ResponseEntity<List<Persona>> buscarPersonasPorDatosPersonales(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String email
    ) {
        log.info("GET /api/personas/busqueda/persona nombre={} apellido={} telefono={} email={}", nombre, apellido, telefono, email);
        return ResponseEntity.ok(personaService.findPeopleByPersonalData(nombre, apellido, telefono, email));
    }

    @GetMapping("/busqueda/ciudad")
    @Operation(
            summary = "Buscar personas por datos de ciudad",
            description = "Busca personas aplicando filtros opcionales sobre la ciudad asociada a su direccion."
    )
    public ResponseEntity<List<Persona>> buscarPersonasPorDatosCiudad(
            @RequestParam(required = false) List<String> ciudad,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) List<String> pais
    ) {
        log.info("GET /api/personas/busqueda/ciudad ciudades={} provincia={} paises={}", ciudad, provincia, pais);
        return ResponseEntity.ok(personaService.findPeopleByCityData(ciudad, provincia, pais));
    }

    @GetMapping("/busqueda/nombre-apellido-ciudades")
    @Operation(
            summary = "Buscar personas por nombre, apellido y ciudades",
            description = "Busca personas filtrando por nombre, apellido y una lista de ciudades."
    )
    public ResponseEntity<List<Persona>> buscarPersonasPorNombreApellidoYCiudades(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) List<String> ciudades
    ) {
        log.info("GET /api/personas/busqueda/nombre-apellido-ciudades nombre={} apellido={} ciudades={}", nombre, apellido, ciudades);
        return ResponseEntity.ok(
                personaService.findPeopleByNameLastNameAndCities(
                        nombre,
                        apellido,
                        ciudades
                )
        );
    }
}
