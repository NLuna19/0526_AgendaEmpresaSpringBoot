package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Direccion;
import com.nluna.agenda_java_spring.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionService implements IDireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public List<Direccion> addressList() {
        return direccionRepository.findAll();
    }

    @Override
    public Direccion findAddressById(Integer idDireccion) {
        return direccionRepository.findById(idDireccion).orElse(null);
    }

    @Override
    public void saveAddress(Direccion address) {
        direccionRepository.save(address);
    }

    @Override
    public void deleteAddress(Direccion address) {
        direccionRepository.delete(address);
    }
}
