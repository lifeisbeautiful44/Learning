
package citytech.global.usecase.create;

import citytech.global.platform.exception.MoneyRequestException;
import citytech.global.platform.usecase.UseCase;
import citytech.global.repository.User;
import citytech.global.repository.UserRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MicronautTest
class CreateUserUseCaseTest {

    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.createUserUseCase = new CreateUserUseCase(userRepository);
    }

    @Test
    void shouldCreateUserIfAllFieldArePresent() throws IOException, URISyntaxException, InterruptedException {
       // given
        when(System.getenv("REGEX_PATTERN")).thenReturn(".+@.+\\..+");
        CreateUserRequest createUserDto = new CreateUserRequest(
                "srijansil",
                "bohara",
                "srijan@gmail.com",
                "lender"
        );

        User savedUser = new User();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
       // when
        Optional<CreateUserResponse> response = createUserUseCase.execute(createUserDto);
       // then
        Assertions.assertNotNull(response);
        assertEquals(200, response.get().data());
    }

    @Test
    void testEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "johndoe123", "existing@gmail.com");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(MoneyRequestException.class, () -> createUserUseCase.execute(request));

    }
}

