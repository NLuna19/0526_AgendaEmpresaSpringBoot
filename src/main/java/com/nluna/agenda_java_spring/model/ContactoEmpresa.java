package com.nluna.agenda_java_spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "contacto_empresa")
public class ContactoEmpresa {

    @EmbeddedId
    private ContactoEmpresaId id;

    @ManyToOne
    @MapsId("idEmpresa")
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @MapsId("idPersona")
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(length = 50)
    private String cargo;
}
