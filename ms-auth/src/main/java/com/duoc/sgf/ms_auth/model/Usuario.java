package com.duoc.sgf.ms_auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "USUARIOS") // <- Cambiar a mayúsculas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private boolean activo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "USUARIO_ROLES", joinColumns = @JoinColumn(name = "USUARIO_ID")) // <- En Mayúsculas
    @Column(name = "ROL") // <- En Mayúsculas
    private Set<String> roles;
}