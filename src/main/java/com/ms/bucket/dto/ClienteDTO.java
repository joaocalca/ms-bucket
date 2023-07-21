package com.ms.bucket.dto;

import com.ms.bucket.enums.Genero;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO implements Serializable {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer idade;

    @NotNull
    private Genero genero;

    private String idImagemCliente;
}
