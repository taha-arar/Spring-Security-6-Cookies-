package com.springsecurity.bootstrap;

import com.springsecurity.model.Client;
import com.springsecurity.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientBootstrap implements CommandLineRunner {

    private final ClientRepository clientRepository;

    public ClientBootstrap(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 100; i++) {
            Client client = new Client();
            client.setEmail("client" + i + "@gmail.com");
            client.setPhone("123-456-789" + (i % 10));
            client.setAddress("Address #" + i);
            clientRepository.save(client);
        }
        log.info("100 clients have been saved!");
    }
}