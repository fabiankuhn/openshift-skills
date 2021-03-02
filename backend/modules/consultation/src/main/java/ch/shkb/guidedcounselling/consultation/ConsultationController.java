package ch.shkb.guidedcounselling.consultation;

import ch.shkb.guidedcounselling.consultation.entity.ConsultationRow;
import ch.shkb.guidedcounselling.consultation.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api-prefix}/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationRepository consultationRepository;

    @GetMapping
    private ResponseEntity<List<ConsultationRow>> findAll(){
        return ResponseEntity.ok(consultationRepository.findAll());
    }
}
