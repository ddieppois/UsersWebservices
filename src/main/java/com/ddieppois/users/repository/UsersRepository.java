package com.ddieppois.users.repository;

import com.ddieppois.users.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
    List<Users> findAllByActive(boolean active);

}
