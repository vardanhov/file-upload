package com.example.uploadfile.repo;




import com.example.uploadfile.domain.Authorities;
import com.example.uploadfile.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    List<Authorities> findAllByUser(User user);
}
