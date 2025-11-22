package com.fiap.abreak_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiap.abreak_api.dto.BreakDTO;
import com.fiap.abreak_api.dto.BreakType;
import com.fiap.abreak_api.dto.RequestBreakDTO;
import com.fiap.abreak_api.model.Break;
import com.fiap.abreak_api.repository.BreakRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BreakService {

    private final BreakRepo repo;
    private final UserService userService;

    public BreakDTO create(RequestBreakDTO dto) {
        var usuario = userService.findEntityById(dto.userId());

        var pausa = Break.builder()
                .user(usuario)
                .breakType(BreakType.valueOf(dto.breakType()))
                .durationSeconds(dto.durationSeconds())
                .dateTime(LocalDateTime.now())
                .build();

        pausa = repo.save(pausa);

        return toDto(pausa);
    }

    public BreakDTO findById(Long id) {
        var pausa = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pausa não encontrada"));

        return toDto(pausa);
    }

    public List<BreakDTO> getBreaksToday(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return repo.findByUserIdAndDateTimeBetween(userId, startOfDay, endOfDay)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Page<BreakDTO> getUserBreaks(Long usuarioId, Pageable pageable) {
        return repo.findByUserIdOrderByDateTimeDesc(usuarioId, pageable)
                .map(this::toDto);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Pausa não encontrada");
        }
        repo.deleteById(id);
    }

    protected BreakDTO toDto(Break pause) {
        return new BreakDTO(
                pause.getId(),
                userService.toDto(pause.getUser()),
                pause.getBreakType(),
                formatDuration(pause.getDurationSeconds()));
    }

    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%dmin %ds", minutes, secs);
    }

    public BreakDTO update(Long id, RequestBreakDTO dto) {
        var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Pausa não encontrada"));

        existing.setBreakType(dto.breakType() != null ? BreakType.valueOf(dto.breakType()) : existing.getBreakType());

        return toDto(repo.save(existing));
    }
}