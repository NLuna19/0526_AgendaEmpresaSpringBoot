package com.nluna.agenda_java_spring.repository;

import com.nluna.agenda_java_spring.model.ContactoEmpresa;
import com.nluna.agenda_java_spring.model.ContactoEmpresaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoEmpresaRepository extends JpaRepository<ContactoEmpresa, ContactoEmpresaId> {

}
