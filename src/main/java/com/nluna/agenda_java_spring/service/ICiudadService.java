package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Ciudad;

import java.util.List;

public interface ICiudadService {
    public List<Ciudad> CityList();

    public Ciudad findCityById(Integer id_ciudad);

    public void saveCity(Ciudad city);

    public void deleteCity(Ciudad city);
}
