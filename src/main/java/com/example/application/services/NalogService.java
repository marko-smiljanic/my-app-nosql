package com.example.application.services;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.NalogDTO;
import com.example.application.DTO.UserDTO;
import com.example.application.entity.Nalog;
import com.example.application.repository.NalogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NalogService {
    private final NalogRepository repository;

    @Autowired
    public NalogService(NalogRepository repository) {
        this.repository = repository;
    }

    public Iterable<Nalog> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<NalogDTO> lazyFindAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(nalog -> Konverzija.konvertujUDto(nalog, NalogDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<Nalog> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<NalogDTO> findByUserId(Long id){
        return repository.findByUserId(id).stream()
                .map(nalog -> Konverzija.konvertujUDto(nalog, NalogDTO.class))
                .collect(Collectors.toList());
    }

    public Nalog create(Nalog n) {
        return repository.save(n);
    }

    public Nalog update(Nalog n) {
        if(n != null &&  this.findById(n.getId()) != null) {
            return this.repository.save(n);
        }
        return null;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
