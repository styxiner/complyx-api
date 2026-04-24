package io.github.styxiner.complyx_api.users;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    	RoleEntity role = roleRepo.findById(id).orElseThrow(new Supplier<RuntimeException>() {
    		@Override
    		public RuntimeException get() {
    			return new RuntimeException("role does not exist");
    		}
    	});
    	
    	return mapper.toDTO(role);
    }
    
    public RoleDTO create(RoleCreateDTO dto) {

        if (roleRepo.existsByRoleName(dto.getName())) {
        	throw new RuntimeException("role does not exists by that rolename");
        }
            
        RoleEntity role = mapper.toEntity(dto);

        return mapper.toDTO(roleRepo.save(role));
    }
    
    public RoleDTO update(RoleUpdateDTO dto) {
    	RoleEntity role = roleRepo.findById(dto.getId()).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("role does not exist");
			}
    		
    	});
    	
    	return mapper.toDTO(roleRepo.save(role));
    }
    
    public void delete(UUID id) {
    	if (!roleRepo.existsById(id)) {
    		throw new RuntimeException("role does not exist");
    	}
    	
    	roleRepo.deleteById(id);
    }
}
