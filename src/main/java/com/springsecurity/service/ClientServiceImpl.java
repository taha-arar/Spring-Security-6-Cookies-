package com.springsecurity.service;

import com.springsecurity.exception.ClientNotFoundException;
import com.springsecurity.model.Client;
import com.springsecurity.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Long addClient(Client client) {
        return clientRepository.save(client).getId();
    }

    @Override
    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setEmail(updatedClient.getEmail());
                    existing.setPhone(updatedClient.getPhone());
                    existing.setAddress(updatedClient.getAddress());
                    return clientRepository.save(existing);
                })
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Page<Client> getAllClientsPaginated(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

}
