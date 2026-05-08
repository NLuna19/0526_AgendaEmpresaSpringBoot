package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Persona;

import java.util.List;

public interface IPersonaService {

    public List<Persona> personList();

    public Persona findPersonById(Integer idPersona);

    public List<Persona> findPeopleByPersonalData(String name, String lastName, String phone, String email);

    public List<Persona> findPeopleByCityData(List<String> cities, String province, List<String> countries);

    public List<Persona> findPeopleByNameLastNameAndCities(String name, String lastName, List<String> cities);

    public void savePerson(Persona person);

    public void deletePerson(Persona person);
}
