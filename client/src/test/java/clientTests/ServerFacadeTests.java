package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    // Clear the database before each test
    public void reset() throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.connect();

        int responseCode = http.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Couldn't clear database");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void positiveRegisterTest() throws URISyntaxException, IOException {
        String response = facade.register("testUser", "password", "email.com");

        Assertions.assertEquals("Success! Registered with username: testUser", response);
    }

    @Test
    public void negativeRegisterTest() throws URISyntaxException, IOException {
        // Attempt to add a user with missing information
        String response = facade.register("testUser", null, "hi@mail.com");

        Assertions.assertEquals("Error: missing registration information", response);

        // Attempt to add a user with a username that already exists in the database
        facade.register("twinUsername", "password", "email.com");
        response = facade.register("twinUsername", "differentpassword", "different@mail.com");

        Assertions.assertEquals("Error: username already taken", response);
    }
}
