package com.duoc.sgf.ms_auth.service;

import com.duoc.sgf.ms_auth.model.dto.LoginRequestDto;
import com.duoc.sgf.ms_auth.model.dto.TokenResponseDto;

public interface AuthService {


    TokenResponseDto login(LoginRequestDto request);

}