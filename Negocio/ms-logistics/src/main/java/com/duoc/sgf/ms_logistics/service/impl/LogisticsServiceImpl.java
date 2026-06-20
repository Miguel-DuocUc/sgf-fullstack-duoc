package com.duoc.sgf.ms_logistics.service.impl;
import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository repository;
    private final PuestoMapper mapper;

    @Override
    public PuestoResponseDto crearPuesto(PuestoRequestDto requestDto) {
        PuestoFronterizo nuevopuesto = mapper.toEntity(requestDto);
        PuestoFronterizo puestoGuardado = repository.save(nuevopuesto);
        return mapper.toDto(nuevopuesto);

    }

    @Override
    public List<PuestoResponseDto> listarTodo() {
        List<PuestoFronterizo> puestoFronterizos = repository.findAll();
        ArrayList <PuestoResponseDto> listaDtos = new ArrayList<PuestoResponseDto>();
            for (PuestoFronterizo puestos:puestoFronterizos){
                PuestoResponseDto dto = mapper.toDto(puestos);
                listaDtos.add(dto);
            }
        return  listaDtos;
    }

    @Override
    public PuestoResponseDto buscarporId(Long id) {
        PuestoFronterizo puestoFronterizo  =  repository.findById(id).orElse(null);

        if (puestoFronterizo != null){
           return mapper.toDto(puestoFronterizo);
        }
        return null;
    }

    @Override
    public PuestoResponseDto actualizarPuesto(Long id, PuestoRequestDto requestDto) {
        PuestoFronterizo puestoFronterizo =  repository.findById(id).orElse(null);
        if (puestoFronterizo != null){
            puestoFronterizo.setName(requestDto.getName());
            puestoFronterizo.setDireccion(requestDto.getDireccion());
            puestoFronterizo.setCantPersonMax(requestDto.getCantPersonMax());
            puestoFronterizo.setGuardPerson(requestDto.getGuardPerson());
            puestoFronterizo.setEstadoOperativo(requestDto.getEstadoOperativo());
            repository.save(puestoFronterizo);
            return mapper.toDto(puestoFronterizo);
        }else {
            return null ;
        }
    }


//

    @Override
    public boolean eliminarPuesto(Long id) {
        PuestoFronterizo puestoFronterizo =  repository.findById(id).orElse(null);
        if (puestoFronterizo != null){
            repository.delete(puestoFronterizo);
            return true;
        } else {
            return false ;
        }

    }
}