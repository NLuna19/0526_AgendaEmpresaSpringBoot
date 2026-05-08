package com.nluna.agenda_java_spring.repository;

import com.nluna.agenda_java_spring.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

}
