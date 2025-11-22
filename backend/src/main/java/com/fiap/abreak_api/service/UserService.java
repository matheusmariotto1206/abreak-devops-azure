package com.fiap.abreak_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fiap.abreak_api.dto.RequestUserDTO;
import com.fiap.abreak_api.dto.UserDTO;
import com.fiap.abreak_api.model.User;
import com.fiap.abreak_api.repository.BreakRepo;
import com.fiap.abreak_api.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final BreakRepo breakRepo;
    private final PasswordEncoder encoder;

    public UserDTO create(RequestUserDTO dto) {
        if (repo.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já existe no sistema");
        }

        User user = User.builder()
                .name(dto.name().trim())
                .email(dto.email().toLowerCase().trim())
                .password(encoder.encode(dto.password()))
                .position(dto.position())
                .breaksQuota(dto.breaksQuota())
                .build();

        return toDto(repo.save(user));
    }

    public UserDTO getById(Long id) {
        var user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return toDto(user);
    }

    public Page<UserDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable)
                .map(this::toDto);
    }

    public UserDTO update(Long id, RequestUserDTO dto) {
        var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (dto.email() != null && !existing.getEmail().equalsIgnoreCase(dto.email())
                && repo.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        existing.setName(dto.name() != null ? dto.name() : existing.getName());
        existing.setEmail(dto.email() != null ? dto.email() : existing.getEmail());
        existing.setPassword(
                dto.password() != null ? encoder.encode(dto.password()) : encoder.encode(existing.getPassword()));
        existing.setPosition(dto.position() != null ? dto.position() : existing.getPosition());
        existing.setBreaksQuota(dto.breaksQuota() != null ? dto.breaksQuota() : existing.getBreaksQuota());

        return toDto(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        repo.deleteById(id);
    }

    public User findEntityById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    protected UserDTO toDto(User user) {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        var breaksToday = breakRepo.countByUserIdAndDateTimeBetween(user.getId(), startOfDay, endOfDay);
        var quotaStatus = calculateQuotaStatus(breaksToday.intValue(), user.getBreaksQuota());

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPosition(),
                user.getBreaksQuota(),
                breaksToday.intValue(),
                quotaStatus);
    }

    private String calculateQuotaStatus(int breaksToday, int quota) {
        if (quota == 0)
            return "SEM_META";

        var percentual = (double) breaksToday / quota * 100;

        if (breaksToday >= quota) {
            return "META_ATINGIDA";
        } else if (percentual >= 75) {
            return "QUASE_LA";
        } else if (percentual >= 50) {
            return "NO_CAMINHO";
        } else {
            return "ABAIXO_ESPERADO";
        }
    }

}
