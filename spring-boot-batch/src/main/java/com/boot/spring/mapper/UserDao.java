package com.boot.spring.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.spring.po.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

}
