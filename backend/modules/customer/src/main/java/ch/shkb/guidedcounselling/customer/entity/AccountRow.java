package ch.shkb.guidedcounselling.customer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ACCOUNT", schema = "CUSTOMER")
public class AccountRow {

    public AccountRow() { }

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "accountRow")
    @Getter @Setter
    private List<ClientRow> clientRow;

}
