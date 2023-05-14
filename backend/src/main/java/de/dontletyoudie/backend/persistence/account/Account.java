package de.dontletyoudie.backend.persistence.account;

import de.dontletyoudie.backend.persistence.minime.MiniMe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Transactional
@Entity(name = "Account")
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(name = "account_email_unique", columnNames = "email"),
        @UniqueConstraint(name = "account_username_unique", columnNames = "username")}
)
public class Account {

    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mini_me_id", referencedColumnName = "id")
    private MiniMe miniMe;

    public Account(String username, String password, String email, Role role, MiniMe miniMe) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.miniMe = miniMe;
    }

    public List<Role> getRoleAsList() {
        List<Role> list = new ArrayList<>();
        list.add(role);
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
