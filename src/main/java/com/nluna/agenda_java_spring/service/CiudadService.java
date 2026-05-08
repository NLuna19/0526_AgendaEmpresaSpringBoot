package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Ciudad;
import com.nluna.agenda_java_spring.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiudadService implements ICiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Override
    public List<Ciudad> CityList() {
        return ciudadRepository.findAll();
    }

    @Override
    public Ciudad findCityById(Integer id_ciudad) {
        return ciudadRepository.findById(id_ciudad).orElse(null);
    }

    @Override
    public void saveCity(Ciudad city) {
        ciudadRepository.save(city);
    }

    @Override
    public void deleteCity(Ciudad city) {
        ciudadRepository.delete((city));
    }
}
