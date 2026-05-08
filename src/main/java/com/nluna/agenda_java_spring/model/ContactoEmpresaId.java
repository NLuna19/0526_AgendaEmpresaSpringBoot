package com.nluna.agenda_java_spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ContactoEmpresaId implements Serializable {

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "id_persona")
    private Integer idPersona;
}
