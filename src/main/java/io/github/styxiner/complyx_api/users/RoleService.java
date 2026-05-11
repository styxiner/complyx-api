package io.github.styxiner.complyx_api.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {

    private final RoleRepository roleRepo;
    private final RoleMapper mapper;

    public RoleService(RoleRepository roleRepo, RoleMapper mapper) {
        this.roleRepo = roleRepo;
        this.mapper = mapper;
    }

    public Page<RoleDTO> getAllRoles(RoleFilter filter, Pageable pageable) {
        Page<RoleEntity> page = roleRepo.findAll(RoleSpecifications.build(filter), pageable);
        List<RoleDTO> list = new java.util.ArrayList<>();
        for (RoleEntity role : page.getContent()) {
            list.add(mapper.toDTO(role));
        }
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    public RoleDTO getRolesById(UUID id) {
        Optional<RoleEntity> opt = roleRepo.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado: " + id);
        }
        return mapper.toDTO(opt.get());
    }

    public RoleDTO create(RoleCreateDTO dto) {
        if (roleRepo.existsByRoleName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un rol con ese nombre: " + dto.getName());
        }
        RoleEntity role = mapper.toEntity(dto);
        return mapper.toDTO(roleRepo.save(role));
    }

    public RoleDTO update(RoleUpdateDTO dto) {
        Optional<RoleEntity> opt = roleRepo.findById(dto.getId());
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado: " + dto.getId());
        }
        RoleEntity role = opt.get();
        // Aquí aplica los campos del dto que necesites actualizar
        return mapper.toDTO(roleRepo.save(role));
    }

    public void delete(UUID id) {
        if (!roleRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado: " + id);
        }
        roleRepo.deleteById(id);
    }
}