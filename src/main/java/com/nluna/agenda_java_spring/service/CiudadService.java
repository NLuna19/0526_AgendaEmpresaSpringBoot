package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Ciudad;
import com.nluna.agenda_java_spring.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class CiudadService implements ICiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Override
    public List<Ciudad> CityList() {
        log.info("CiudadService.CityList");
        List<Ciudad> ciudades = ciudadRepository.findAll();
        log.info("CiudadService.CityList resultCount={}", ciudades.size());
        return ciudades;
    }

    @Override
    public Ciudad findCityById(Integer id_ciudad) {
        log.info("CiudadService.findCityById id={}", id_ciudad);
        return ciudadRepository.findById(id_ciudad).orElse(null);
    }

    @Override
    public void saveCity(Ciudad city) {
        log.info("CiudadService.saveCity id={} nombre={} provincia={} pais={}", city.getIdCiudad(), city.getNombre(), city.getProvincia(), city.getPais());
        ciudadRepository.save(city);
    }

    @Override
    public void deleteCity(Ciudad city) {
        log.info("CiudadService.deleteCity id={} nombre={}", city.getIdCiudad(), city.getNombre());
        ciudadRepository.delete((city));
    }
}
