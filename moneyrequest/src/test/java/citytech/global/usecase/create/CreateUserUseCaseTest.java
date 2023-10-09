package citytech.global.usecase.create;

import citytech.global.platform.exception.CustomResponseException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MicronautTest
class CreateUserUseCaseTest {

    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;


    @BeforeEach
    void setUp()
    {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.createUserUseCase = new CreateUserUseCase(userRepository);
    }

    @Test
    void testCreateUserSuccess() throws IOException, URISyntaxException, InterruptedException {
        //given
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "srijansil",
                "bohara",
                "srijansilbohara@gmail.com",
                "LENDER"
        );
        User savedUser = new User();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);
        // When
        Optional<CreateUserResponse> response = createUserUseCase.execute(createUserRequest);
        // Then
        Assertions.assertNotNull(response);
        assertEquals("200", response.get().code());
    }

    @Test
    void shouldThrowExceptionWhenFieldsAreEmpty() {
        // Given
        CreateUserRequest createUserRequest = new CreateUserRequest(

                "",
                "",
                "",
                ""
        );

        // When
        Executable executable = () -> createUserUseCase.execute(createUserRequest);

        // Then
        CustomResponseException exception = assertThrows(CustomResponseException.class, executable);
        assertEquals("failed", exception.getMessage());
    }
    }






