package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.ContactoEmpresa;
import com.nluna.agenda_java_spring.model.ContactoEmpresaId;
import com.nluna.agenda_java_spring.repository.ContactoEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoEmpresaService implements IContactoEmpresaService {

    @Autowired
    private ContactoEmpresaRepository contactoEmpresaRepository;

    @Override
    public List<ContactoEmpresa> companyContactList() {
        return contactoEmpresaRepository.findAll();
    }

    @Override
    public ContactoEmpresa findCompanyContactById(ContactoEmpresaId id) {
        return contactoEmpresaRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCompanyContact(ContactoEmpresa companyContact) {
        contactoEmpresaRepository.save(companyContact);
    }

    @Override
    public void deleteCompanyContact(ContactoEmpresa companyContact) {
        contactoEmpresaRepository.delete(companyContact);
    }
}
