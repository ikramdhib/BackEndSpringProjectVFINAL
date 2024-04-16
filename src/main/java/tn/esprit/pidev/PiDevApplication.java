package tn.esprit.pidev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class , SecurityAutoConfiguration.class })
public class PiDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiDevApplication.class, args);
    }

}
