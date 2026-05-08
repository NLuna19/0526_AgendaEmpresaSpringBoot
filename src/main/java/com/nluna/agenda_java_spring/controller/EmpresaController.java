package com.nluna.agenda_java_spring.controller;

import com.nluna.agenda_java_spring.model.Empresa;
import com.nluna.agenda_java_spring.service.IEmpresaService;
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
@RequestMapping("/api/empresas")
@Tag(name = "Empresa",
        description = "")
@Slf4j
public class EmpresaController {

    private final IEmpresaService empresaService;

    public EmpresaController(IEmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        log.info("GET /api/empresas");
        return ResponseEntity.ok(empresaService.companyList());
    }

    @GetMapping("/{idEmpresa}")
    public ResponseEntity<Empresa> buscarEmpresaPorId(@PathVariable Integer idEmpresa) {
        log.info("GET /api/empresas/{}", idEmpresa);
        Empresa empresa = empresaService.findCompanyById(idEmpresa);

        if (empresa == null) {
            log.warn("Empresa not found id={}", idEmpresa);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
        log.info("POST /api/empresas razonSocial={}", empresa.getRazonSocial());
        empresa.setIdEmpresa(null);
        empresaService.saveCompany(empresa);

        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @PutMapping("/{idEmpresa}")
    public ResponseEntity<Empresa> actualizarEmpresa(
            @PathVariable Integer idEmpresa,
            @RequestBody Empresa empresaActualizada
    ) {
        log.info("PUT /api/empresas/{}", idEmpresa);
        Empresa empresa = empresaService.findCompanyById(idEmpresa);

        if (empresa == null) {
            log.warn("Empresa not found for update id={}", idEmpresa);
            return ResponseEntity.notFound().build();
        }

        empresa.setRazonSocial(empresaActualizada.getRazonSocial());
        empresa.setTelefono(empresaActualizada.getTelefono());
        empresa.setDireccion(empresaActualizada.getDireccion());
        empresaService.saveCompany(empresa);

        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping("/{idEmpresa}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Integer idEmpresa) {
        log.info("DELETE /api/empresas/{}", idEmpresa);
        Empresa empresa = empresaService.findCompanyById(idEmpresa);

        if (empresa == null) {
            log.warn("Empresa not found for delete id={}", idEmpresa);
            return ResponseEntity.notFound().build();
        }

        empresaService.deleteCompany(empresa);

        return ResponseEntity.noContent().build();
    }
}
