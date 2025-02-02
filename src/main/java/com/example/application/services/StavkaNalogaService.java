package com.example.application.services;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.NalogDTO;
import com.example.application.DTO.StavkaNalogaDTO;
import com.example.application.entity.StavkaNaloga;
import com.example.application.repository.StavkaNalogaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StavkaNalogaService {
    private final StavkaNalogaRepository repository;

    @Autowired
    public StavkaNalogaService(StavkaNalogaRepository repository) {
        this.repository = repository;
    }

    public List<StavkaNaloga> findAll() {
        return repository.findAll();
    }


    @Transactional
    public List<StavkaNalogaDTO> lazyFindAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(stavkaNaloga -> Konverzija.konvertujUDto(stavkaNaloga, StavkaNalogaDTO.class))
                .collect(Collectors.toList());
    }


    public Optional<StavkaNaloga> findById(Long id) {
        return repository.findById(id);
    }

    public StavkaNaloga create(StavkaNaloga n) {
        return repository.save(n);
    }

    public StavkaNaloga update(StavkaNaloga n) {
        if(n != null &&  this.findById(n.getId()) != null) {
            return this.repository.save(n);
        }
        return null;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
