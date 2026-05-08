package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Empresa;

import java.util.List;

public interface IEmpresaService {

    public List<Empresa> companyList();

    public Empresa findCompanyById(Integer idEmpresa);

    public void saveCompany(Empresa company);

    public void deleteCompany(Empresa company);
}
