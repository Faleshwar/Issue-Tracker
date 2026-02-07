package com.issueflow.repository;

import com.issueflow.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    @Query("SELECT u FROM User u JOIN u.projectMembers pm JOIN pm.project p WHERE p.id = :projectId")
    Set<User> findProjectMembers(@Param("projectId") Long projectId);

    @Query("SELECT u FROM User u WHERE u.userRole != 'SYSTEM_ADMIN'")
    List<User> findUsersExceptSysAdmin();

}
