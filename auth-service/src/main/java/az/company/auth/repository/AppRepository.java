package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import az.company.auth.entity.App;



public interface AppRepository extends JpaRepository<App, Long> {
}
