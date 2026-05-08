package com.nluna.agenda_java_spring.repository;

import com.nluna.agenda_java_spring.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

}
