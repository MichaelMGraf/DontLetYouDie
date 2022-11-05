package de.dontletyoudie.backend.persistence.account;

import javax.persistence.*;

@Entity(name = "Account")
@Table(name = "account", uniqueConstraints = {/*@UniqueConstraint(name = "account_email_unique", columnNames = "email"),*/
        @UniqueConstraint(name = "account_user_name_unique", columnNames = "username")})
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

    public Account() {
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
