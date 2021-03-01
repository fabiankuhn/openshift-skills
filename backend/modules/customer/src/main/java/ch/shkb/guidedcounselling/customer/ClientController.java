package ch.shkb.guidedcounselling.customer;

import ch.shkb.guidedcounselling.customer.entity.ClientRow;
import ch.shkb.guidedcounselling.customer.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api-prefix}/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @GetMapping
    private ResponseEntity<List<ClientRow>> findAll(){
        return ResponseEntity.ok(clientRepository.findAll());
    }
}
