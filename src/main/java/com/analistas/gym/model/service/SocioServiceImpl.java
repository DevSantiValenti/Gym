package com.analistas.gym.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analistas.gym.model.domain.Socio;
import com.analistas.gym.model.repository.ISocioRepository;

@Service
public class SocioServiceImpl implements ISocioService {

    @Autowired
    ISocioRepository socioRepository;

    @Override
    public List<Socio> listarSocios() {
        return (List<Socio>)socioRepository.findAll();
    }

    @Override
    public void guardar(Socio socio) {
        socioRepository.save(socio);
    }

    @Override
    public Socio buscarPorDNI(String dni) {
        return (Socio) socioRepository.findByDni(dni);
    }

}
