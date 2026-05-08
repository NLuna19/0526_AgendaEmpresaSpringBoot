package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.Direccion;
import com.nluna.agenda_java_spring.service.IDireccionService;
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
@RequestMapping("/api/direcciones")
@Tag(name = "Direccion",
        description = "Direccion Controller")
@Slf4j
public class DireccionController {

    private final IDireccionService direccionService;

    public DireccionController(IDireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping
    public ResponseEntity<List<Direccion>> listarDirecciones() {
        log.info("GET /api/direcciones");
        return ResponseEntity.ok(direccionService.addressList());
    }

    @GetMapping("/{idDireccion}")
    public ResponseEntity<Direccion> buscarDireccionPorId(@PathVariable Integer idDireccion) {
        log.info("GET /api/direcciones/{}", idDireccion);
        Direccion direccion = direccionService.findAddressById(idDireccion);

        if (direccion == null) {
            log.warn("Direccion not found id={}", idDireccion);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(direccion);
    }

    @PostMapping
    public ResponseEntity<Direccion> crearDireccion(@RequestBody Direccion direccion) {
        log.info("POST /api/direcciones calle={} numero={} cp={}", direccion.getCalle(), direccion.getNumero(), direccion.getCp());
        direccion.setIdDireccion(null);
        direccionService.saveAddress(direccion);

        return ResponseEntity.status(HttpStatus.CREATED).body(direccion);
    }

    @PutMapping("/{idDireccion}")
    public ResponseEntity<Direccion> actualizarDireccion(
            @PathVariable Integer idDireccion,
            @RequestBody Direccion direccionActualizada
    ) {
        log.info("PUT /api/direcciones/{}", idDireccion);
        Direccion direccion = direccionService.findAddressById(idDireccion);

        if (direccion == null) {
            log.warn("Direccion not found for update id={}", idDireccion);
            return ResponseEntity.notFound().build();
        }

        direccion.setCalle(direccionActualizada.getCalle());
        direccion.setNumero(direccionActualizada.getNumero());
        direccion.setPiso(direccionActualizada.getPiso());
        direccion.setDepto(direccionActualizada.getDepto());
        direccion.setCp(direccionActualizada.getCp());
        direccion.setCiudad(direccionActualizada.getCiudad());
        direccionService.saveAddress(direccion);

        return ResponseEntity.ok(direccion);
    }

    @DeleteMapping("/{idDireccion}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Integer idDireccion) {
        log.info("DELETE /api/direcciones/{}", idDireccion);
        Direccion direccion = direccionService.findAddressById(idDireccion);

        if (direccion == null) {
            log.warn("Direccion not found for delete id={}", idDireccion);
            return ResponseEntity.notFound().build();
        }

        direccionService.deleteAddress(direccion);

        return ResponseEntity.noContent().build();
    }
}
