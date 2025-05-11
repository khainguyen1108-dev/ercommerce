package storemanagement.example.group_15.infrastructure.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import storemanagement.example.group_15.app.aop.logger;
import storemanagement.example.group_15.domain.permissions.repository.PermissionRepository;
import storemanagement.example.group_15.domain.rules.entity.RuleEntity;
import storemanagement.example.group_15.domain.rules.repository.RuleRepository;
import storemanagement.example.group_15.domain.users.entity.AuthEntity;
import storemanagement.example.group_15.domain.users.repository.AuthRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;
import storemanagement.example.group_15.infrastructure.helper.PasswordHelper;
import storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    public AuthRepository authRepository;
    @Autowired
    public RuleRepository ruleRepository;
    @Autowired
    public PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ruleRepository.count() == 0) {
            RuleEntity ruleEntity = new RuleEntity();
            ruleEntity.setName("admin");
            this.ruleRepository.save(ruleEntity);
            RuleEntity userEntity = new RuleEntity();
            userEntity.setName("user");
            this.ruleRepository.save(userEntity);
        }
        if (authRepository.count() == 0) {
            AuthEntity admin = new AuthEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(PasswordHelper.hashPassword("123456"));
            admin.setName("admin");
            Optional<RuleEntity> ruleAdmin = this.ruleRepository.findByName("admin");
            if (!ruleAdmin.isEmpty()) {
                System.out.println("seed admin failed");
            }
            admin.setRole(ruleAdmin.get());
            authRepository.save(admin);
            System.out.println("Seeded auth data.");
        }
        if (permissionRepository.count() == 0) {
            String[] permissions = { "users", "products", "events", "carts", "vouchers", "rating", "collection",
                    "favorites", "orders", "statistic", "rules", "permissions" };
            for (String permission : permissions) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setUrlPattern(permission);
                permissionRepository.save(permissionEntity);
            }
            System.out.println("Seeded permissions data.");
        }
    }
}
