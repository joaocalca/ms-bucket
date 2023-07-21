package com.ms.bucket.model;

import com.ms.bucket.enums.Genero;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CLIENTE", uniqueConstraints = { @UniqueConstraint(name = "EMAIL_UNIQUE", columnNames = "EMAIL"),
        @UniqueConstraint(name = "ID_IMAGEM_CLIENTE_UNIQUE", columnNames = "ID_IMAGEM_CLIENTE")})
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "IDADE", nullable = false)
    private Integer idade;

    @Column(name = "GENERO", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "ID_IMAGEM_CLIENTE", unique = true)
    private String idImagemCliente;
}
