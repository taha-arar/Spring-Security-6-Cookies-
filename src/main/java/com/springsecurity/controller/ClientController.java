package com.springsecurity.controller;

import com.springsecurity.model.Client;
import com.springsecurity.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Add new client", description = "Creates a new client and returns the generated client ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid client data")
    })
    public ResponseEntity<Long> addClient(
            @Valid @RequestBody
            @Parameter(description = "Client object to be added", required = true)
            Client client) {
        log.info("Adding new client: {}", client);
        Long savedClientId = clientService.addClient(client);
        return ResponseEntity.ok(savedClientId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client by ID", description = "Updates the client identified by the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Client> updateClient(
            @Parameter(description = "ID of the client to update", required = true)
            @PathVariable Long id,
            @RequestBody
            @Parameter(description = "Updated client object", required = true)
            Client client) {
        log.info("Updating client with ID: {}", id);
        Client updated = clientService.updateClient(id, client);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client by ID", description = "Deletes the client identified by the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID of the client to delete", required = true)
            @PathVariable Long id) {
        log.info("Deleting client with ID: {}", id);
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Returns a list of all clients.")
    @ApiResponse(responseCode = "200", description = "List of clients returned")
    public ResponseEntity<List<Client>> getAllClients() {
        log.info("Fetching all clients");
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get clients with pagination", description = "Returns paginated clients based on Pageable parameters.")
    @ApiResponse(responseCode = "200", description = "Paginated list of clients returned")
    public ResponseEntity<Page<Client>> getClientsPaginated(
            @Parameter(description = "Pagination information (page, size, sort)")
            Pageable pageable) {
        log.info("Fetching paginated clients");
        return ResponseEntity.ok(clientService.getAllClientsPaginated(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Returns a client by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found and returned"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Client> getClientById(
            @Parameter(description = "ID of the client to retrieve", required = true)
            @PathVariable Long id) {
        log.info("Fetching client with ID: {}", id);
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
