package ch.shkb.guidedcounselling.consultation.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Table(name = "CONSULTATION_ROW", schema = "CONSULTATION")
public class ConsultationRow {

    public ConsultationRow() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter @Setter @NonNull
    private String greeting;
}
