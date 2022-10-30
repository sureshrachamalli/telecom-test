package ca.tele.chatr.chatrtest.persistence;

import ca.tele.chatr.chatrtest.ChatrTestApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatrTestApplication.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserName_success_scenario() {
        UserEntity userEntity = new UserEntity(null, "suresh");
        userRepository.save(userEntity);
        UserEntity returnUserEntity = userRepository.findByUserName("suresh");
        assertEquals("suresh", returnUserEntity.getUserName());
    }

    @Test
    void findByUserName_failure_scenario() {
       assertEquals(null, userRepository.findByUserName("naresh"));
    }
}