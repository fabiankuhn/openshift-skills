package ch.shkb.guidedcounselling.customer.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Table(name = "CLIENT", schema = "CUSTOMER")
public class ClientRow {

    public ClientRow() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter @Setter @NonNull
    private String name;

    @ManyToMany
    @JoinTable(name = "CLIENT_ACCOUNT", schema = "CUSTOMER")
    @Getter @Setter
    private List<AccountRow> accountRow;

}
