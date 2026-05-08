package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Persona;
import com.nluna.agenda_java_spring.repository.PersonaRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaService implements IPersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Persona> personList() {
        return personaRepository.findAll();
    }

    @Override
    public Persona findPersonById(Integer idPersona) {
        return personaRepository.findById(idPersona).orElse(null);
    }

    @Override
    public List<Persona> findPeopleByPersonalData(String name, String lastName, String phone, String email) {
        return personaRepository.findAll(buildPersonalDataSpecification(name, lastName, phone, email));
    }

    @Override
    public List<Persona> findPeopleByCityData(List<String> cities, String province, List<String> countries) {
        return personaRepository.findAll(buildCityDataSpecification(cities, province, countries));
    }

    @Override
    public List<Persona> findPeopleByNameLastNameAndCities(String name, String lastName, List<String> cities) {
        return personaRepository.findAll(buildNameLastNameAndCitiesSpecification(name, lastName, cities));
    }

    @Override
    public void savePerson(Persona person) {
        personaRepository.save(person);
    }

    @Override
    public void deletePerson(Persona person) {
        personaRepository.delete(person);
    }

    private Specification<Persona> buildPersonalDataSpecification(
            String name,
            String lastName,
            String phone,
            String email
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(name)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")),
                        likeValue(name)
                ));
            }

            if (hasText(lastName)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("apellido")),
                        likeValue(lastName)
                ));
            }

            if (hasText(phone)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("telefono")),
                        likeValue(phone)
                ));
            }

            if (hasText(email)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        likeValue(email)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<Persona> buildCityDataSpecification(
            List<String> cities,
            String province,
            List<String> countries
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            var direccion = root.join("direccion", JoinType.LEFT);
            var ciudad = direccion.join("ciudad", JoinType.LEFT);

            if (cities != null && !cities.isEmpty()) {
                List<Predicate> cityPredicates = cities.stream()
                        .filter(this::hasText)
                        .map(city -> criteriaBuilder.like(
                                criteriaBuilder.lower(ciudad.get("nombre")),
                                likeValue(city)
                        ))
                        .toList();

                if (!cityPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(cityPredicates.toArray(Predicate[]::new)));
                }
            }

            if (hasText(province)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(ciudad.get("provincia")),
                        likeValue(province)
                ));
            }

            if (countries != null && !countries.isEmpty()) {
                List<String> normalizedCountries = countries.stream()
                        .filter(this::hasText)
                        .map(String::toLowerCase)
                        .toList();

                if (!normalizedCountries.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(ciudad.get("pais")).in(normalizedCountries));
                }
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<Persona> buildNameLastNameAndCitiesSpecification(
            String name,
            String lastName,
            List<String> cities
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            var direccion = root.join("direccion", JoinType.LEFT);
            var ciudad = direccion.join("ciudad", JoinType.LEFT);

            if (hasText(name)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")),
                        likeValue(name)
                ));
            }

            if (hasText(lastName)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("apellido")),
                        likeValue(lastName)
                ));
            }

            if (cities != null && !cities.isEmpty()) {
                List<Predicate> cityPredicates = cities.stream()
                        .filter(this::hasText)
                        .map(city -> criteriaBuilder.like(
                                criteriaBuilder.lower(ciudad.get("nombre")),
                                likeValue(city)
                        ))
                        .toList();

                if (!cityPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(cityPredicates.toArray(Predicate[]::new)));
                }
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String likeValue(String value) {
        return "%" + value.toLowerCase() + "%";
    }
}
