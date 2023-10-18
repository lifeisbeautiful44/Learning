package citytech.global.usecase;

import citytech.global.platform.email.EmailConfiguration;
import citytech.global.platform.exception.CustomResponseException;
import citytech.global.repository.EmailRepository;
import freemarker.template.TemplateException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@MicronautTest
public class EmailUseCaseTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private EmailConfiguration emailConfiguration;

    @InjectMocks
    private EmailUseCase emailUseCase;

    @BeforeEach
    void setUp() {
        emailRepository = mock(EmailRepository.class);
        emailConfiguration = mock(EmailConfiguration.class);
        emailUseCase = new EmailUseCase(emailRepository);
    }

    @Test
    void givenValidRequest_whenExecute_thenSuccess() throws SQLException, TemplateException, IOException {
        EmailUseCaseRequest request = new EmailUseCaseRequest("srijansil@gmail.com", "password123");
        when(emailRepository.findEmail()).thenReturn(Optional.of("bohara@gmail.com"));

        EmailUseCaseResponse response = emailUseCase.execute(request).orElseThrow();

        assertEquals("Success", response.data());
        verify(emailConfiguration, times(1)).sendMail(any());
    }

    @Test
    void givenInvalidRequest_whenExecute_thenExceptionThrown() throws SQLException, TemplateException, IOException {
        EmailUseCaseRequest request = new EmailUseCaseRequest("bohara@gmail.com", "password123");
        when(emailRepository.findEmail()).thenReturn(Optional.empty());


        assertThrows(CustomResponseException.class, () -> emailUseCase.execute(request));
        verify(emailConfiguration, never()).sendMail(any());
    }
}