package de.dontletyoudie.backend.persistence.friend;

import javax.persistence.*;

@Entity(name = "Friend")
@Table(name = "friend")
public class Friend {

    @Id
    @SequenceGenerator(name = "friend_sequence", sequenceName = "friend_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    // TODO

    public Friend() {
    }
}
