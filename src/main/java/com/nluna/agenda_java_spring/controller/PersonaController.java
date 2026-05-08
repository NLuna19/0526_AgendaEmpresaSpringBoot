package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.Persona;
import com.nluna.agenda_java_spring.service.IPersonaService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/personas")
@Tag(name = "Persona",
        description = "")
@Slf4j
public class PersonaController {

    private final IPersonaService personaService;

    public PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listarPersonas() {
        log.info("GET /api/personas");
        return ResponseEntity.ok(personaService.personList());
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<Persona> buscarPersonaPorId(@PathVariable Integer idPersona) {
        log.info("GET /api/personas/{}", idPersona);
        Persona persona = personaService.findPersonById(idPersona);

        if (persona == null) {
            log.warn("Persona not found id={}", idPersona);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(persona);
    }

    @PostMapping
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        log.info("POST /api/personas nombre={} apellido={}", persona.getNombre(), persona.getApellido());
        persona.setIdPersona(null);
        personaService.savePerson(persona);

        return ResponseEntity.status(HttpStatus.CREATED).body(persona);
    }

    @PutMapping("/{idPersona}")
    public ResponseEntity<Persona> actualizarPersona(
            @PathVariable Integer idPersona,
            @RequestBody Persona personaActualizada
    ) {
        log.info("PUT /api/personas/{}", idPersona);
        Persona persona = personaService.findPersonById(idPersona);

        if (persona == null) {
            log.warn("Persona not found for update id={}", idPersona);
            return ResponseEntity.notFound().build();
        }

        persona.setNombre(personaActualizada.getNombre());
        persona.setApellido(personaActualizada.getApellido());
        persona.setTelefono(personaActualizada.getTelefono());
        persona.setEmail(personaActualizada.getEmail());
        persona.setDireccion(personaActualizada.getDireccion());
        personaService.savePerson(persona);

        return ResponseEntity.ok(persona);
    }

    @DeleteMapping("/{idPersona}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Integer idPersona) {
        log.info("DELETE /api/personas/{}", idPersona);
        Persona persona = personaService.findPersonById(idPersona);

        if (persona == null) {
            log.warn("Persona not found for delete id={}", idPersona);
            return ResponseEntity.notFound().build();
        }

        personaService.deletePerson(persona);

        return ResponseEntity.noContent().build();
    }
}
