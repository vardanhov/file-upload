package com.example.uploadfile.repo;

import com.example.uploadfile.domain.WhiteListUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListUserRepository extends JpaRepository<WhiteListUser,Integer> {
}
