package com.fiap.abreak_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.abreak_api.dto.BreakDTO;
import com.fiap.abreak_api.dto.RequestBreakDTO;
import com.fiap.abreak_api.service.BreakService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/breaks")
@RequiredArgsConstructor
@Slf4j
public class BreakController {

    private final BreakService service;

    @PostMapping
    public ResponseEntity<BreakDTO> create(@Valid @RequestBody RequestBreakDTO dto) {

        log.info("criando pausa: {}", dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<BreakDTO> findById(@PathVariable Long id) {
        log.info("recuperando pausa pelo id: {}", id);
        return ResponseEntity
                .ok(service.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<BreakDTO> update(@PathVariable Long id, @RequestBody RequestBreakDTO dto) {
        log.info("atualizando pausa '{}' com: {}", id, dto);
        return ResponseEntity
                .ok(
                        service.update(id, dto));
    }

    @GetMapping("today/{userId}")
    public ResponseEntity<List<BreakDTO>> listBreaksToday(@PathVariable Long userId) {
        log.info("recuperando pausas hoje do usuario: {}", userId);
        return ResponseEntity
                .ok(service.getBreaksToday(userId));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<BreakDTO>> listUserBreaks(
            @PathVariable Long userId,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("recuperando pausas do usuario pelo id: {}", userId);
        return ResponseEntity
                .ok(service.getUserBreaks(userId, pageable));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("deletando pausa pelo id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
