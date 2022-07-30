package com.export.pdf.thymeleaf.repo;

import com.export.pdf.thymeleaf.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
}
