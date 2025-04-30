package ru.alexeev.etflux.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alexeev.etflux.entity.UserRole;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    UserRole findByRole(String name);
}
