package com.nluna.agenda_java_spring.service;

import com.nluna.agenda_java_spring.model.Empresa;
import com.nluna.agenda_java_spring.model.Direccion;
import com.nluna.agenda_java_spring.model.Ciudad;
import com.nluna.agenda_java_spring.repository.CiudadRepository;
import com.nluna.agenda_java_spring.repository.DireccionRepository;
import com.nluna.agenda_java_spring.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EmpresaService implements IEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Override
    public List<Empresa> companyList() {
        log.info("EmpresaService.companyList");
        List<Empresa> empresas = empresaRepository.findAll();
        log.info("EmpresaService.companyList resultCount={}", empresas.size());
        return empresas;
    }

    @Override
    public Empresa findCompanyById(Integer idEmpresa) {
        log.info("EmpresaService.findCompanyById id={}", idEmpresa);
        return empresaRepository.findById(idEmpresa).orElse(null);
    }

    @Override
    @Transactional
    public void saveCompany(Empresa company) {
        log.info("EmpresaService.saveCompany id={} razonSocial={}", company.getIdEmpresa(), company.getRazonSocial());
        Direccion incomingDireccion = company.getDireccion();
        company.setDireccion(null);
        normalizarDireccion(company, incomingDireccion);
        empresaRepository.save(company);
    }

    private void normalizarDireccion(Empresa company, Direccion incomingDireccion) {
        Direccion baseDireccion = findExistingDireccion(company.getIdEmpresa());
        Direccion direccion = mergeDireccion(incomingDireccion, baseDireccion);

        if (direccion == null) {
            return;
        }

        Ciudad ciudad = mergeCiudad(direccion.getCiudad(), baseDireccion != null ? baseDireccion.getCiudad() : null);
        if (ciudad != null) {
            direccion.setCiudad(normalizarOCrearCiudad(ciudad));
        } else if (company.getIdEmpresa() == null) {
            throw new IllegalArgumentException("La ciudad es obligatoria para guardar una empresa");
        }

        direccion = normalizarOCrearDireccion(direccion);
        company.setDireccion(direccion);
    }

    private Empresa findExistingEmpresa(Integer idEmpresa) {
        if (idEmpresa == null) {
            return null;
        }

        return empresaRepository.findById(idEmpresa).orElse(null);
    }

    private Direccion findExistingDireccion(Integer idEmpresa) {
        Empresa existing = findExistingEmpresa(idEmpresa);
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
                .filter(actual -> Objects.equals(actual.getNumero(), direccion.getNumero()))
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
            return Objects.equals(left.getIdCiudad(), right.getIdCiudad());
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
    public void deleteCompany(Empresa company) {
        log.info("EmpresaService.deleteCompany id={} razonSocial={}", company.getIdEmpresa(), company.getRazonSocial());
        empresaRepository.delete(company);
    }
}
