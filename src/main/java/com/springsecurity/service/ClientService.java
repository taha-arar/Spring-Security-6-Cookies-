package com.springsecurity.service;

import com.springsecurity.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Long addClient(Client client);
    Client updateClient(Long id, Client updatedClient);
    void deleteClient(Long id);
    List<Client> getAllClients();
    Page<Client> getAllClientsPaginated(Pageable pageable);
    Optional<Client> getClientById(Long id);

}
