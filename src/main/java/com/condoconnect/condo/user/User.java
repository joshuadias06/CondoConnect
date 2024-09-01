package com.condoconnect.condo.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @NotBlank(message =  "O email é obrigatório!")
    @Column(nullable = false)
    @Email(message = "Email deve ser válido!")
    private String email;

    @NotBlank(message = "A senha é obrigatória! ")
    @Column(nullable = false)
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    private String cpf;
    private String phone;
    private String CondominiumId;
}


