package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.ContactoEmpresa;
import com.nluna.agenda_java_spring.model.ContactoEmpresaId;

import java.util.List;

public interface IContactoEmpresaService {

    public List<ContactoEmpresa> companyContactList();

    public ContactoEmpresa findCompanyContactById(ContactoEmpresaId id);

    public void saveCompanyContact(ContactoEmpresa companyContact);

    public void deleteCompanyContact(ContactoEmpresa companyContact);
}
