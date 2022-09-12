package hellojpa;

import javax.persistence.*;

@Entity
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    private String name;

    //양방향
    @OneToOne
    @JoinColumn(name = "locker")
    private MemberV2 member;
}
