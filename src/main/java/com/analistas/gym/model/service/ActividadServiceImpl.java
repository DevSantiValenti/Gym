package com.analistas.gym.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analistas.gym.model.domain.Actividad;
import com.analistas.gym.model.repository.IActividadRepository;

@Service
public class ActividadServiceImpl implements IActividadService{

    @Autowired
    IActividadRepository actividadRepository;

    @Override
    public List<Actividad> listarActividades() {
        return (List<Actividad>)actividadRepository.findAll();
    }

    @Override
    public void guardar(Actividad actividad) {
        actividadRepository.save(actividad);
    }

    @Override
    public Actividad buscarPorId(Long id) {
       return actividadRepository.findById(id).orElse(null);
    }

}
