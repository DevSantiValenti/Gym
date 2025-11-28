package com.analistas.gym.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.analistas.gym.model.domain.Socio;
import com.analistas.gym.model.repository.ISocioRepository;

@Service
public class SocioServiceImpl implements ISocioService {

    @Autowired
    ISocioRepository socioRepository;

    @Override
    public List<Socio> listarSocios() {
        return (List<Socio>) socioRepository.findAll();
    }

    @Override
    public void guardar(Socio socio) {
        socioRepository.save(socio);
    }

    // @Override
    // public Socio buscarPorDNI(String dni) {
    // return socioRepository.findByDni(dni);
    // }

    @Transactional
    @Override
    public Optional<Socio> actualizarVecesIngresado(String dni) {
        Optional<Socio> socioOpt = socioRepository.findByDni(dni);
        if (socioOpt.isPresent()) {

            Socio socio = socioOpt.get();

            // Guardar el valor del último ingreso ANTES de modificar
            LocalDateTime ultimoIngresoAnterior = socio.getUltIngreso() != null ? socio.getUltIngreso() : LocalDateTime.now();

            // Actualizar datos en BD
            socio.setVecesIngresado(socio.getVecesIngresado() == null ? 1 : socio.getVecesIngresado() + 1);
            socio.setUltIngreso(LocalDateTime.now());
            socioRepository.save(socio);

            // Copia para el frontend 
            Socio copia = new Socio();
            copia.setId(socio.getId());
            copia.setNombreCompleto(socio.getNombreCompleto());
            copia.setDni(socio.getDni());
            copia.setActividad(socio.getActividad());
            copia.setSaldoPendiente(socio.getSaldoPendiente());
            copia.setCuotaPaga(socio.getCuotaPaga());
            copia.setFechaVencimiento(socio.getFechaVencimiento());
            copia.setVecesIngresado(socio.getVecesIngresado());
            // Mandamos al front la ult vez ingresada (no actual)
            copia.setUltIngreso(ultimoIngresoAnterior);
            return Optional.of(copia);
        }
        return Optional.empty();
    }

}
