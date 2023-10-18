package citytech.global;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Property(name = "spec.name", value = "ChatWebSocketTest")
@MicronautTest
class ChatServerWebSocketTest {

    @Inject
    BeanContext beanContext;

    @Inject
    EmbeddedServer embeddedServer;

    @Requires(property = "spec.name", value = "ChatWebSocketTest")
    @ClientWebSocket
    static abstract class TestWebSocketClient implements AutoCloseable {

        private final Deque<String> messageHistory = new ConcurrentLinkedDeque<>();

        public String getLatestMessage() {
            return messageHistory.peekLast();
        }

        public List<String> getMessagesChronologically() {
            return new ArrayList<>(messageHistory);
        }

        @OnMessage
        void onMessage(String message) {
            messageHistory.add(message);
        }

        abstract void send(@NonNull @NotBlank String message);
    }

    private TestWebSocketClient createWebSocketClient(int port, String username, String topic) {
        WebSocketClient webSocketClient = beanContext.getBean(WebSocketClient.class);
        URI uri = UriBuilder.of("ws://localhost")
                .port(port)
                .path("ws")
                .path("chat")
                .path("{topic}")
                .path("{username}")
                .expand(CollectionUtils.mapOf("topic", topic, "username", username));
        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient.class,  uri);
        return Flux.from(client).blockFirst();
    }

    @Test
    void testWebsockerServer() throws Exception {
        TestWebSocketClient adam = createWebSocketClient(embeddedServer.getPort(), "adam", "Cats & Recreation");
        await().until(() ->
                Collections.singletonList("[adam] Joined Cats & Recreation!")
                        .equals(adam.getMessagesChronologically()));

        TestWebSocketClient anna = createWebSocketClient(embeddedServer.getPort(), "anna", "Cats & Recreation");
        await().until(() ->
                Collections.singletonList("[anna] Joined Cats & Recreation!")
                        .equals(anna.getMessagesChronologically()));
        await().until(() ->
                Arrays.asList("[adam] Joined Cats & Recreation!", "[anna] Joined Cats & Recreation!")
                        .equals(adam.getMessagesChronologically()));

        TestWebSocketClient ben = createWebSocketClient(embeddedServer.getPort(), "ben", "Fortran Tips & Tricks");
        await().until(() ->
                Collections.singletonList("[ben] Joined Fortran Tips & Tricks!")
                        .equals(ben.getMessagesChronologically()));
        TestWebSocketClient zach = createWebSocketClient(embeddedServer.getPort(), "zach", "all");
        await().until(() ->
                Collections.singletonList("[zach] Now making announcements!")
                        .equals(zach.getMessagesChronologically()));
        TestWebSocketClient cienna = createWebSocketClient(embeddedServer.getPort(), "cienna", "Fortran Tips & Tricks");
        await().until(() ->
                Collections.singletonList("[cienna] Joined Fortran Tips & Tricks!")
                        .equals(cienna.getMessagesChronologically()));
        await().until(() ->
                Arrays.asList("[ben] Joined Fortran Tips & Tricks!", "[zach] Now making announcements!", "[cienna] Joined Fortran Tips & Tricks!")
                        .equals(ben.getMessagesChronologically()));

        final String adamsGreeting = "Hello, everyone. It's another purrrfect day :-)";
        final String expectedGreeting = "[adam] " + adamsGreeting;
        adam.send(adamsGreeting);

        await().until(() ->
                expectedGreeting.equals(adam.getLatestMessage()));

        await().until(() ->
                expectedGreeting.equals(anna.getLatestMessage()));

        assertNotEquals(expectedGreeting, ben.getLatestMessage());

        await().until(() ->
                expectedGreeting.equals(zach.getLatestMessage()));

        assertNotEquals(expectedGreeting, cienna.getLatestMessage());


        anna.close();

        String annaLeaving = "[anna] Leaving Cats & Recreation!";
        await().until(() ->
                annaLeaving.equals(adam.getLatestMessage()));

        assertEquals(annaLeaving, adam.getLatestMessage());

        assertNotEquals(annaLeaving, anna.getLatestMessage());

        assertNotEquals(annaLeaving, ben.getLatestMessage());

        assertEquals(annaLeaving, zach.getLatestMessage());

        assertNotEquals(annaLeaving, cienna.getLatestMessage());

        adam.close();
        ben.close();
        zach.close();
        cienna.close();
    }
}