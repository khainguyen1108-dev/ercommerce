package storemanagement.example.group_15.domain.permissions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.domain.permissions.dto.PermissionDTO;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;
import storemanagement.example.group_15.domain.permissions.repository.PermissionRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.*;

@Service
public class PermissionsService {
    @Autowired
    private PermissionRepository permissionRepository;
    public  Map<String, Set<String>> create(Map<String, Set<String>> input){
        Set<String> inputCreate = input.get("name");
       Set<String> errors = new HashSet<>();
        Set<String> success = new HashSet<>();
        for (String in : inputCreate){
            Optional<PermissionEntity> existed = this.permissionRepository.findByUrlPattern(in);
            if (existed.isPresent()){
                errors.add(in);
                continue;
            }
            PermissionEntity permission = new PermissionEntity();
            permission.setUrlPattern(in);
            this.permissionRepository.save(permission);
            success.add(in);
        }
        Map<String, Set<String>> output = new HashMap<>();
        output.put("existed", errors);
        output.put("success",success);
        return output;
    }
    public List<PermissionDTO> getAll(){
        List<PermissionEntity> output =  this.permissionRepository.findAll();
        List<PermissionDTO> response = new ArrayList<>();
        for (PermissionEntity in : output){
            PermissionDTO res = new PermissionDTO();
            res.setPermissionId(in.getPermissionId());
            res.setUrlPattern(in.getUrlPattern());
            response.add(res);
        }
        return response;
    }
    public Long update(Long id, Map<String, String> input ){
        Optional<PermissionEntity> permission = this.permissionRepository.findById(id);
        if (permission.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "permission_id.not_found");
        }
        permission.get().setUrlPattern(input.get("name"));
        this.permissionRepository.save(permission.get());
        return id;
    }
    public Long delete(Long id){
        Optional<PermissionEntity> permission = this.permissionRepository.findById(id);
        if (permission.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "permission_id.not_found");
        }
        this.permissionRepository.delete(permission.get());
        return id;
    }
}
