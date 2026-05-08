package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Empresa;
import com.nluna.agenda_java_spring.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService implements IEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> companyList() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa findCompanyById(Integer idEmpresa) {
        return empresaRepository.findById(idEmpresa).orElse(null);
    }

    @Override
    public void saveCompany(Empresa company) {
        empresaRepository.save(company);
    }

    @Override
    public void deleteCompany(Empresa company) {
        empresaRepository.delete(company);
    }
}
