package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.ContactoEmpresa;
import com.nluna.agenda_java_spring.model.ContactoEmpresaId;
import com.nluna.agenda_java_spring.service.IContactoEmpresaService;
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
@RequestMapping("/api/contactos-empresa")
@Tag(name = "ContactoEmpresa",
        description = "")
@Slf4j
public class ContactoEmpresaController {

    private final IContactoEmpresaService contactoEmpresaService;

    public ContactoEmpresaController(IContactoEmpresaService contactoEmpresaService) {
        this.contactoEmpresaService = contactoEmpresaService;
    }

    @GetMapping
    public ResponseEntity<List<ContactoEmpresa>> listarContactosEmpresa() {
        log.info("GET /api/contactos-empresa");
        return ResponseEntity.ok(contactoEmpresaService.companyContactList());
    }

    @GetMapping("/{idEmpresa}/{idPersona}")
    public ResponseEntity<ContactoEmpresa> buscarContactoEmpresaPorId(
            @PathVariable Integer idEmpresa,
            @PathVariable Integer idPersona
    ) {
        log.info("GET /api/contactos-empresa/{}/{}", idEmpresa, idPersona);
        ContactoEmpresa contactoEmpresa = contactoEmpresaService.findCompanyContactById(
                new ContactoEmpresaId(idEmpresa, idPersona)
        );

        if (contactoEmpresa == null) {
            log.warn("ContactoEmpresa not found idEmpresa={} idPersona={}", idEmpresa, idPersona);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(contactoEmpresa);
    }

    @PostMapping
    public ResponseEntity<ContactoEmpresa> crearContactoEmpresa(@RequestBody ContactoEmpresa contactoEmpresa) {
        log.info("POST /api/contactos-empresa idEmpresa={} idPersona={} cargo={}",
                contactoEmpresa.getEmpresa() != null ? contactoEmpresa.getEmpresa().getIdEmpresa() : null,
                contactoEmpresa.getPersona() != null ? contactoEmpresa.getPersona().getIdPersona() : null,
                contactoEmpresa.getCargo());
        contactoEmpresa.setId(buildId(contactoEmpresa));
        contactoEmpresaService.saveCompanyContact(contactoEmpresa);

        return ResponseEntity.status(HttpStatus.CREATED).body(contactoEmpresa);
    }

    @PutMapping("/{idEmpresa}/{idPersona}")
    public ResponseEntity<ContactoEmpresa> actualizarContactoEmpresa(
            @PathVariable Integer idEmpresa,
            @PathVariable Integer idPersona,
            @RequestBody ContactoEmpresa contactoEmpresaActualizado
    ) {
        log.info("PUT /api/contactos-empresa/{}/{}", idEmpresa, idPersona);
        ContactoEmpresa contactoEmpresa = contactoEmpresaService.findCompanyContactById(
                new ContactoEmpresaId(idEmpresa, idPersona)
        );

        if (contactoEmpresa == null) {
            log.warn("ContactoEmpresa not found for update idEmpresa={} idPersona={}", idEmpresa, idPersona);
            return ResponseEntity.notFound().build();
        }

        contactoEmpresa.setCargo(contactoEmpresaActualizado.getCargo());
        contactoEmpresaService.saveCompanyContact(contactoEmpresa);

        return ResponseEntity.ok(contactoEmpresa);
    }

    @DeleteMapping("/{idEmpresa}/{idPersona}")
    public ResponseEntity<Void> eliminarContactoEmpresa(
            @PathVariable Integer idEmpresa,
            @PathVariable Integer idPersona
    ) {
        log.info("DELETE /api/contactos-empresa/{}/{}", idEmpresa, idPersona);
        ContactoEmpresa contactoEmpresa = contactoEmpresaService.findCompanyContactById(
                new ContactoEmpresaId(idEmpresa, idPersona)
        );

        if (contactoEmpresa == null) {
            log.warn("ContactoEmpresa not found for delete idEmpresa={} idPersona={}", idEmpresa, idPersona);
            return ResponseEntity.notFound().build();
        }

        contactoEmpresaService.deleteCompanyContact(contactoEmpresa);

        return ResponseEntity.noContent().build();
    }

    private ContactoEmpresaId buildId(ContactoEmpresa contactoEmpresa) {
        return new ContactoEmpresaId(
                contactoEmpresa.getEmpresa().getIdEmpresa(),
                contactoEmpresa.getPersona().getIdPersona()
        );
    }
}
