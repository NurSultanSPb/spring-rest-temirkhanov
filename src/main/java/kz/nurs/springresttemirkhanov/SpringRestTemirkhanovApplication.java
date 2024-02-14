package kz.nurs.springresttemirkhanov;

import kz.nurs.springresttemirkhanov.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class SpringRestTemirkhanovApplication {
    private static final String API_USERS = "http://94.198.50.185:7081/api/users";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(SpringRestTemirkhanovApplication.class, args);
        SpringRestTemirkhanovApplication application = new SpringRestTemirkhanovApplication();
        String employeesCookie = application.getUsersCookie();
        String first = application.createUser(employeesCookie);
        String second = application.updateUser(employeesCookie);
        String third = application.deleteUser(employeesCookie, 3L);
        System.out.println(first + second + third);
    }
    private String getUsersCookie() {
        String cookie = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> result = restTemplate.exchange(API_USERS, HttpMethod.GET, entity,
                String.class);
        if (result.getHeaders().containsKey("Set-Cookie")) {
            cookie = result.getHeaders().get("Set-Cookie").get(0);
        }

        return cookie;
    }

    private String createUser(String s) {

        User user = new User(3L, "James", "Brown", (byte) 30);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("cookie", s);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(API_USERS, HttpMethod.POST, entity, String.class);
        return exchange.getBody();
    }

    private String updateUser(String s) {

        User user = new User(3L, "Thomas", "Shelby", (byte) 30);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("cookie", s);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(API_USERS, HttpMethod.PUT, entity, String.class);
        return exchange.getBody();
    }

    private String deleteUser(String s, Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("cookie", s);
        HttpEntity<User> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(API_USERS + "/" + id, HttpMethod.DELETE, entity, String.class);
        return exchange.getBody();
    }
}
