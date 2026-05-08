package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Direccion;

import java.util.List;

public interface IDireccionService {

    public List<Direccion> addressList();

    public Direccion findAddressById(Integer idDireccion);

    public void saveAddress(Direccion address);

    public void deleteAddress(Direccion address);
}
