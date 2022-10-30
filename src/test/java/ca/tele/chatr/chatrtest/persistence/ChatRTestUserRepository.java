package ca.tele.chatr.chatrtest.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRTestUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
}
