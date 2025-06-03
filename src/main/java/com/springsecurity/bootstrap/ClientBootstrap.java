package com.springsecurity.bootstrap;

import com.github.javafaker.Faker;
import com.springsecurity.model.Client;
import com.springsecurity.model.enums.Gender;
import com.springsecurity.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientBootstrap {

    private final ClientRepository clientRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @PostConstruct
    public void seedClients() {
        if (clientRepository.count() == 0) {
            IntStream.range(0, 100).forEach(i -> {
                Client client = new Client();
                client.setFirstName(faker.name().firstName());
                client.setLastName(faker.name().lastName());
                client.setDateOfBirth(LocalDate.of(1980 + random.nextInt(20), 1 + random.nextInt(12), 1 + random.nextInt(28)));
                client.setGender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE);
                client.setEmail(faker.internet().emailAddress());
                client.setPhone(String.format("%010d", random.nextInt(1_000_000_000)));
                client.setAddress(faker.address().fullAddress());

                clientRepository.save(client);
            });
            log.info("✅ Inserted 100 random clients");
        }
    }
}
