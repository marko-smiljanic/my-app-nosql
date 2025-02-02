package com.example.application.services;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.RobaDTO;
import com.example.application.entity.Roba;
import com.example.application.repository.RobaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RobaService {
    private final RobaRepository repository;

    @Autowired
    public RobaService(RobaRepository repository) {
        this.repository = repository;
    }

    public Iterable<Roba> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<RobaDTO> lazyFindAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(roba -> Konverzija.konvertujUDto(roba, RobaDTO.class))
                .collect(Collectors.toList());
    }

    public Roba findBySifra(String sifra){
        return repository.findBySifra(sifra);
    }

    @Transactional
    public Optional<Roba> get(Long id) {
        return repository.findById(id);
    }

    public Roba create(Roba n) {
        return repository.save(n);
    }

    public Roba update(Roba n) {
        return this.repository.save(n);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
