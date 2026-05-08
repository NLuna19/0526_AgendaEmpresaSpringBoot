package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Direccion;
import com.nluna.agenda_java_spring.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class DireccionService implements IDireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public List<Direccion> addressList() {
        log.info("DireccionService.addressList");
        List<Direccion> direcciones = direccionRepository.findAll();
        log.info("DireccionService.addressList resultCount={}", direcciones.size());
        return direcciones;
    }

    @Override
    public Direccion findAddressById(Integer idDireccion) {
        log.info("DireccionService.findAddressById id={}", idDireccion);
        return direccionRepository.findById(idDireccion).orElse(null);
    }

    @Override
    public void saveAddress(Direccion address) {
        log.info("DireccionService.saveAddress id={} calle={} numero={} cp={}", address.getIdDireccion(), address.getCalle(), address.getNumero(), address.getCp());
        direccionRepository.save(address);
    }

    @Override
    public void deleteAddress(Direccion address) {
        log.info("DireccionService.deleteAddress id={} calle={}", address.getIdDireccion(), address.getCalle());
        direccionRepository.delete(address);
    }
}
