package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Empresa;
import com.nluna.agenda_java_spring.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class EmpresaService implements IEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> companyList() {
        log.info("EmpresaService.companyList");
        List<Empresa> empresas = empresaRepository.findAll();
        log.info("EmpresaService.companyList resultCount={}", empresas.size());
        return empresas;
    }

    @Override
    public Empresa findCompanyById(Integer idEmpresa) {
        log.info("EmpresaService.findCompanyById id={}", idEmpresa);
        return empresaRepository.findById(idEmpresa).orElse(null);
    }

    @Override
    public void saveCompany(Empresa company) {
        log.info("EmpresaService.saveCompany id={} razonSocial={}", company.getIdEmpresa(), company.getRazonSocial());
        empresaRepository.save(company);
    }

    @Override
    public void deleteCompany(Empresa company) {
        log.info("EmpresaService.deleteCompany id={} razonSocial={}", company.getIdEmpresa(), company.getRazonSocial());
        empresaRepository.delete(company);
    }
}
