package ch.shkb.guidedcounselling.consultation.repository;

import ch.shkb.guidedcounselling.consultation.entity.ConsultationRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<ConsultationRow, Long> {
}
