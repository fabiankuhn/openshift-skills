package ch.shkb.guidedcounselling.consultation;

import ch.shkb.guidedcounselling.consultation.entity.ConsultationRow;
import ch.shkb.guidedcounselling.consultation.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationDataLoader implements ApplicationRunner {

    private final ConsultationRepository consultationRepository;

    @Override
    public void run(ApplicationArguments args) {
        consultationRepository.save(new ConsultationRow("Hello Madam"));
        consultationRepository.save(new ConsultationRow("Hello Sir"));
    }
}
