package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.Mapper.PatronMapper;
import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.payload.PatronDTO;
import com.librarymanagementsystem.repository.PatronRepository;
import com.librarymanagementsystem.service.PatronService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.librarymanagementsystem.util.ApplicationConstants.*;

@AllArgsConstructor
@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Cacheable("patrons")
    @Override
    public List<PatronDTO> getAllPatrons() {
        return patronRepository.findAll().stream()
                .map(PatronMapper.MAPPER::mapToPatronDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "patrons", key = "#id")
    @Override
    public PatronDTO getPatronById(Long id) {
        Patron patron = patronRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(PATRON, ID, id)

        );
        return PatronMapper.MAPPER.mapToPatronDTO(patron);
    }

    @CachePut(value = "patrons", key = "#patronDTO.email")
    @Override
    public PatronDTO addPatron(PatronDTO patronDTO) {
        patronRepository.findByEmail(patronDTO.getEmail()).ifPresent(patron -> {
            throw new AlreadyExistsException(PATRON, EMAIL, patron.getEmail());
        });
        Patron patron = PatronMapper.MAPPER.mapToPatron(patronDTO);
        Patron savedPatron = patronRepository.save(patron);
        return PatronMapper.MAPPER.mapToPatronDTO(savedPatron);
    }

    @CachePut(value = "patrons", key = "#id")
    @Override
    public PatronDTO updatePatron(Long id, PatronDTO patronDTO) {
        Patron existingPatron = patronRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(PATRON, ID, id)
        );
        existingPatron.setFirstName(patronDTO.getFirstName());
        existingPatron.setLastName(patronDTO.getLastName());
        existingPatron.setPhoneNumber(patronDTO.getPhoneNumber());
        existingPatron.setEmail(patronDTO.getEmail());

        Patron updatedPatron = patronRepository.save(existingPatron);
        return PatronMapper.MAPPER.mapToPatronDTO(updatedPatron);
    }

    @CacheEvict(value = "patrons", key = "#id")
    @Override
    public void deletePatron(Long id) {
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(PATRON, ID, id);
        }
    }
}
