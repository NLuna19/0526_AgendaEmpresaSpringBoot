package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Ciudad;
import com.nluna.agenda_java_spring.model.Direccion;
import com.nluna.agenda_java_spring.model.Persona;
import com.nluna.agenda_java_spring.repository.CiudadRepository;
import com.nluna.agenda_java_spring.repository.DireccionRepository;
import com.nluna.agenda_java_spring.repository.PersonaRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonaService implements IPersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Override
    public List<Persona> personList() {
        log.info("PersonaService.personList");
        List<Persona> personas = personaRepository.findAll();
        log.info("PersonaService.personList resultCount={}", personas.size());
        return personas;
    }

    @Override
    public Persona findPersonById(Integer idPersona) {
        log.info("PersonaService.findPersonById id={}", idPersona);
        return personaRepository.findById(idPersona).orElse(null);
    }

    @Override
    public List<Persona> findPeopleByPersonalData(String name, String lastName, String phone, String email) {
        log.info("PersonaService.findPeopleByPersonalData nombre={} apellido={} telefono={} email={}", name, lastName, phone, email);
        List<Persona> personas = personaRepository.findAll(buildPersonalDataSpecification(name, lastName, phone, email));
        log.info("PersonaService.findPeopleByPersonalData resultCount={}", personas.size());
        return personas;
    }

    @Override
    public List<Persona> findPeopleByCityData(List<String> cities, String province, List<String> countries) {
        log.info("PersonaService.findPeopleByCityData ciudades={} provincia={} paises={}", cities, province, countries);
        List<Persona> personas = personaRepository.findAll(buildCityDataSpecification(cities, province, countries));
        log.info("PersonaService.findPeopleByCityData resultCount={}", personas.size());
        return personas;
    }

    @Override
    public List<Persona> findPeopleByNameLastNameAndCities(String name, String lastName, List<String> cities) {
        log.info("PersonaService.findPeopleByNameLastNameAndCities nombre={} apellido={} ciudades={}", name, lastName, cities);
        List<Persona> personas = personaRepository.findAll(buildNameLastNameAndCitiesSpecification(name, lastName, cities));
        log.info("PersonaService.findPeopleByNameLastNameAndCities resultCount={}", personas.size());
        return personas;
    }

    @Override
    @Transactional
    public void savePerson(Persona person) {
        log.info("PersonaService.savePerson id={} nombre={} apellido={}", person.getIdPersona(), person.getNombre(), person.getApellido());
        Direccion incomingDireccion = person.getDireccion();
        person.setDireccion(null);
        normalizarDireccion(person, incomingDireccion);
        personaRepository.save(person);
    }

    private void normalizarDireccion(Persona person, Direccion incomingDireccion) {
        Direccion baseDireccion = findExistingDireccion(person.getIdPersona());
        Direccion direccion = mergeDireccion(incomingDireccion, baseDireccion);

        if (direccion == null) {
            return;
        }

        Ciudad ciudad = mergeCiudad(direccion.getCiudad(), baseDireccion != null ? baseDireccion.getCiudad() : null);
        if (ciudad != null) {
            direccion.setCiudad(normalizarOCrearCiudad(ciudad));
        } else if (person.getIdPersona() == null) {
            throw new IllegalArgumentException("La ciudad es obligatoria para guardar una persona");
        }

        direccion = normalizarOCrearDireccion(direccion);
        person.setDireccion(direccion);
    }

    private Persona findExistingPerson(Integer idPersona) {
        if (idPersona == null) {
            return null;
        }

        return personaRepository.findById(idPersona).orElse(null);
    }

    private Direccion findExistingDireccion(Integer idPersona) {
        Persona existing = findExistingPerson(idPersona);
        return existing != null ? existing.getDireccion() : null;
    }

    private Ciudad normalizarOCrearCiudad(Ciudad ciudad) {
        if (ciudad == null) {
            return null;
        }

        ciudad.setNombre(normalizarTexto(ciudad.getNombre()));
        ciudad.setProvincia(normalizarTexto(ciudad.getProvincia()));
        ciudad.setPais(normalizarTexto(ciudad.getPais()));

        if (ciudad.getNombre() == null || ciudad.getProvincia() == null || ciudad.getPais() == null) {
            throw new IllegalArgumentException("La ciudad debe incluir nombre, provincia y pais");
        }

        Ciudad existente = ciudadRepository.findAll().stream()
                .filter(actual -> equalsIgnoreCase(actual.getNombre(), ciudad.getNombre()))
                .filter(actual -> equalsIgnoreCase(actual.getProvincia(), ciudad.getProvincia()))
                .filter(actual -> equalsIgnoreCase(actual.getPais(), ciudad.getPais()))
                .findFirst()
                .orElse(null);

        return existente != null ? existente : ciudadRepository.save(ciudad);
    }

    private Direccion normalizarOCrearDireccion(Direccion direccion) {
        direccion.setCalle(normalizarTexto(direccion.getCalle()));
        direccion.setPiso(normalizarTexto(direccion.getPiso()));
        direccion.setDepto(normalizarTexto(direccion.getDepto()));
        direccion.setCp(normalizarTexto(direccion.getCp()));

        if (direccion.getCalle() == null || direccion.getNumero() == null || direccion.getCiudad() == null) {
            throw new IllegalArgumentException("La direccion debe incluir calle, numero y ciudad");
        }

        Direccion existente = direccionRepository.findAll().stream()
                .filter(actual -> equalsIgnoreCase(actual.getCalle(), direccion.getCalle()))
                .filter(actual -> java.util.Objects.equals(actual.getNumero(), direccion.getNumero()))
                .filter(actual -> sameOptionalText(actual.getPiso(), direccion.getPiso()))
                .filter(actual -> sameOptionalText(actual.getDepto(), direccion.getDepto()))
                .filter(actual -> sameOptionalText(actual.getCp(), direccion.getCp()))
                .filter(actual -> sameCity(actual.getCiudad(), direccion.getCiudad()))
                .findFirst()
                .orElse(null);

        return existente != null ? existente : direccionRepository.save(direccion);
    }

    private Direccion mergeDireccion(Direccion incoming, Direccion base) {
        if (incoming == null) {
            return base;
        }

        if (base == null) {
            return incoming;
        }

        if (incoming.getCalle() == null) {
            incoming.setCalle(base.getCalle());
        }
        if (incoming.getNumero() == null) {
            incoming.setNumero(base.getNumero());
        }
        if (incoming.getPiso() == null) {
            incoming.setPiso(base.getPiso());
        }
        if (incoming.getDepto() == null) {
            incoming.setDepto(base.getDepto());
        }
        if (incoming.getCp() == null) {
            incoming.setCp(base.getCp());
        }
        if (incoming.getCiudad() == null) {
            incoming.setCiudad(base.getCiudad());
        }

        return incoming;
    }

    private Ciudad mergeCiudad(Ciudad incoming, Ciudad base) {
        if (incoming == null) {
            return base;
        }

        if (base == null) {
            return incoming;
        }

        if (incoming.getNombre() == null) {
            incoming.setNombre(base.getNombre());
        }
        if (incoming.getProvincia() == null) {
            incoming.setProvincia(base.getProvincia());
        }
        if (incoming.getPais() == null) {
            incoming.setPais(base.getPais());
        }

        return incoming;
    }

    private boolean sameCity(Ciudad left, Ciudad right) {
        if (left == null || right == null) {
            return false;
        }

        if (left.getIdCiudad() != null && right.getIdCiudad() != null) {
            return java.util.Objects.equals(left.getIdCiudad(), right.getIdCiudad());
        }

        return equalsIgnoreCase(left.getNombre(), right.getNombre())
                && equalsIgnoreCase(left.getProvincia(), right.getProvincia())
                && equalsIgnoreCase(left.getPais(), right.getPais());
    }

    private String normalizarTexto(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private boolean equalsIgnoreCase(String left, String right) {
        if (left == null || right == null) {
            return false;
        }

        return left.equalsIgnoreCase(right);
    }

    private boolean sameOptionalText(String left, String right) {
        String normalizedLeft = normalizarTexto(left);
        String normalizedRight = normalizarTexto(right);

        if (normalizedLeft == null && normalizedRight == null) {
            return true;
        }

        return normalizedLeft != null && normalizedLeft.equalsIgnoreCase(normalizedRight);
    }

    @Override
    public void deletePerson(Persona person) {
        log.info("PersonaService.deletePerson id={} nombre={} apellido={}", person.getIdPersona(), person.getNombre(), person.getApellido());
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
