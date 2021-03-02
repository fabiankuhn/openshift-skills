package ch.shkb.guidedcounselling.customer;

import ch.shkb.guidedcounselling.customer.entity.ClientRow;
import ch.shkb.guidedcounselling.customer.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDataLoader implements ApplicationRunner {

    private final ClientRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(new ClientRow("Alexander"));
        userRepository.save(new ClientRow("Peter"));
        userRepository.save(new ClientRow("Rafael"));
    }
}
