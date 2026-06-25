package com.duoc.sgf.ms_auth.service;

import java.util.Set;

public interface UsuarioService {

    void upsertUsuario(String username, String password, Set<String> roles);

}