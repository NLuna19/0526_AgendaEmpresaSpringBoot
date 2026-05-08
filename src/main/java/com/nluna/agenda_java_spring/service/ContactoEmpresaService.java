package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.ContactoEmpresa;
import com.nluna.agenda_java_spring.model.ContactoEmpresaId;
import com.nluna.agenda_java_spring.repository.ContactoEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class ContactoEmpresaService implements IContactoEmpresaService {

    @Autowired
    private ContactoEmpresaRepository contactoEmpresaRepository;

    @Override
    public List<ContactoEmpresa> companyContactList() {
        log.info("ContactoEmpresaService.companyContactList");
        List<ContactoEmpresa> contactos = contactoEmpresaRepository.findAll();
        log.info("ContactoEmpresaService.companyContactList resultCount={}", contactos.size());
        return contactos;
    }

    @Override
    public ContactoEmpresa findCompanyContactById(ContactoEmpresaId id) {
        log.info("ContactoEmpresaService.findCompanyContactById idEmpresa={} idPersona={}", id.getIdEmpresa(), id.getIdPersona());
        return contactoEmpresaRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCompanyContact(ContactoEmpresa companyContact) {
        log.info("ContactoEmpresaService.saveCompanyContact idEmpresa={} idPersona={} cargo={}",
                companyContact.getEmpresa() != null ? companyContact.getEmpresa().getIdEmpresa() : null,
                companyContact.getPersona() != null ? companyContact.getPersona().getIdPersona() : null,
                companyContact.getCargo());
        contactoEmpresaRepository.save(companyContact);
    }

    @Override
    public void deleteCompanyContact(ContactoEmpresa companyContact) {
        log.info("ContactoEmpresaService.deleteCompanyContact idEmpresa={} idPersona={}",
                companyContact.getEmpresa() != null ? companyContact.getEmpresa().getIdEmpresa() : null,
                companyContact.getPersona() != null ? companyContact.getPersona().getIdPersona() : null);
        contactoEmpresaRepository.delete(companyContact);
    }
}
