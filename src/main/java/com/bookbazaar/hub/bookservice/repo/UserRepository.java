package com.bookbazaar.hub.bookservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookbazaar.hub.bookservice.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long>{

}
