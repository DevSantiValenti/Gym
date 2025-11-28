package com.analistas.gym.model.service;

import java.util.List;
import java.util.Optional;

import com.analistas.gym.model.domain.Socio;

public interface ISocioService  {

    public List<Socio> listarSocios();

    public void guardar(Socio socio);

    // public Socio buscarPorDNI(String dni);

    public Optional<Socio> actualizarVecesIngresado(String dni);

}
