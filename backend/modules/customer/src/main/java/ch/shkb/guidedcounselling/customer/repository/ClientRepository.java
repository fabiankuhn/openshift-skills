package ch.shkb.guidedcounselling.customer.repository;

import ch.shkb.guidedcounselling.customer.entity.ClientRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientRow, Long> {
}
