package ch.shkb.guidedcounselling.customer.entity;

import ch.shkb.guidedcounselling.customer.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ClientRowTest {

    private final String USER_NAME = "Test-User-1";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void loadData(){
        clientRepository.save(new ClientRow(USER_NAME));
    }

    @AfterEach
    void destroyData(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
                "CUSTOMER.ACCOUNT",
                "CUSTOMER.CLIENT_ACCOUNT",
                "CUSTOMER.CLIENT");
    }

    /**
     * Before loadClient is executed the dataLoader
     * is also used to fill the first three clients.
     * Therefor the first persisted entity has the id 3.
     */
    @Test
    void loadClient() {
        // arrange
        List<ClientRow> clients = clientRepository.findAll();
        //act
        //assert
        assertThat(clients).isNotNull();
        assertThat(clients.size()).isEqualTo(4);
        assertThat(clients.get(3).getName()).isEqualTo(USER_NAME);
    }

    /**
     * When the loadConsultation method is executed again
     * the previously loaded elements are deleted. Therefor
     * the size of the list is 1.
     */
    @Test
    void loadClient_repeat() {
        // arrange
        List<ClientRow> clients = clientRepository.findAll();
        //act
        //assert
        assertThat(clients).isNotNull();
        assertThat(clients.size()).isEqualTo(1);
        assertThat(clients.get(0).getName()).isEqualTo(USER_NAME);
    }
}
