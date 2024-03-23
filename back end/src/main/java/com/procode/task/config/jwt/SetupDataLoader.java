package com.procode.task.config.jwt;

import com.procode.task.dao.RoleDao;
import com.procode.task.dao.UserDao;
import com.procode.task.model.entity.RoleEntity;
import com.procode.task.model.entity.UserEntity;
import com.procode.task.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final String PASSWORD = "$2a$10$TE1nfIH8gst.D84C3JFbpuTrhGb1Xs5n4jniyMbnQKMH1TfDxS8nu";

    private boolean alreadySetup = false;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDAO;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // Create initial users
        initializeEntities();
        alreadySetup = true;
    }

    /**
     * Create roles and
     */
    @Transactional
    public void initializeEntities() {

        for (UserRole role : UserRole.values()) {
            final Optional<RoleEntity> optionalEmployeeRoleEntity = roleDAO.findByRoleName(role);
            if (!optionalEmployeeRoleEntity.isPresent()) {
                final RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRole(role);
                roleDAO.save(roleEntity);
            }
        }

        if (!userDao.findByUsername("manager").isPresent()) {
            UserEntity userEntity = new UserEntity();
            //password "test" encrypted in SHA-1
            userEntity.setPassword(PASSWORD);
            userEntity.setUsername("manager");
            roleDAO.findByRoleName(UserRole.MANAGER).ifPresent(userEntity::setRoleEntity);
            userDao.save(userEntity);
        }

        for (int x = 1; x < 4; x++) {
            final String username = "employee" + x;
            if (!userDao.findByUsername(username).isPresent()) {
                UserEntity userEntity = new UserEntity();
                //password "test" encrypted in SHA-1
                userEntity.setPassword(PASSWORD);
                userEntity.setUsername(username);
                roleDAO.findByRoleName(UserRole.EMPLOYEE).ifPresent(userEntity::setRoleEntity);
                userDao.save(userEntity);
            }
        }
    }
}
