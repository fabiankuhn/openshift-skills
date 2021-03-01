package ch.shkb.guidedcounselling.consultation.entity;

import ch.shkb.guidedcounselling.consultation.repository.ConsultationRepository;
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
public class ConsultationRowTest {

    private final String GREETING = "Howdy";

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void loadData(){
        consultationRepository.save(new ConsultationRow(GREETING));
    }

    @AfterEach
    void destroyData(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "CONSULTATION.CONSULTATION_ROW");
    }

    /**
     * Before loadConsultation is executed the dataLoader
     * is also used to fill the first two greetings.
     * Therefor the first persisted entity has the id 2.
     */
    @Test
    void loadConsultations() {
        // arrange
        List<ConsultationRow> clients = consultationRepository.findAll();
        //act
        //assert
        assertThat(clients).isNotNull();
        assertThat(clients.size()).isEqualTo(3);
        assertThat(clients.get(2).getGreeting()).isEqualTo(GREETING);
    }

    /**
     * When the loadConsultation method is executed again
     * the previously loaded elements are deleted. Therefor
     * the size of the list is 1.
     */
    @Test
    void loadConsultations_repeat() {
        // arrange
        List<ConsultationRow> clients = consultationRepository.findAll();
        //act
        //assert
        assertThat(clients).isNotNull();
        assertThat(clients.size()).isEqualTo(1);
        assertThat(clients.get(0).getGreeting()).isEqualTo(GREETING);
    }
}
