package storemanagement.example.group_15.domain.rules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import storemanagement.example.group_15.app.dto.request.rule.RuleRequestDTO;
import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;
import storemanagement.example.group_15.domain.permissions.repository.PermissionRepository;
import storemanagement.example.group_15.domain.rules.entity.RuleEntity;
import storemanagement.example.group_15.domain.rules.entity.RulePermissionEntity;
import storemanagement.example.group_15.domain.rules.repository.RulePermissionRepository;
import storemanagement.example.group_15.domain.rules.repository.RuleRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.*;

@Service
public class RuleService {
    @Autowired
    private RulePermissionRepository rulePermissionRepository;
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
   public Map<String, Object> create(RuleRequestDTO input){
       Set<String> permissions = input.getPermissions();
       String name = input.getName();
       Optional<RuleEntity> existed = this.ruleRepository.findByName(input.getName());
       if (existed.isPresent()){
           throw new AppException(HttpStatus.BAD_REQUEST, "name.existed");
       }
       RuleEntity rule = new RuleEntity();
       rule.setName(name);
       this.ruleRepository.save(rule);
       Set<String> pNotFound = new HashSet<>();
       for (String item : permissions){
           String[] permission_split = item.split("/");
           Optional<PermissionEntity> permissionEntity = this.permissionRepository.findById(Long.parseLong(permission_split[0]));
           if (permissionEntity.isEmpty()){
               pNotFound.add(permission_split[0]);
               continue;
           }
           RulePermissionEntity rulePermissionEntity = new RulePermissionEntity();
           rulePermissionEntity.setRule(rule);
           rulePermissionEntity.setPermission(permissionEntity.get());
           rulePermissionEntity.setAccess(permission_split[1]);
           this.rulePermissionRepository.save(rulePermissionEntity);
       }
       Map<String, Object> response = new HashMap<>();
       response.put("rule_id", rule.getId());
       response.put("not_found",pNotFound);
       return response;
   }
}
