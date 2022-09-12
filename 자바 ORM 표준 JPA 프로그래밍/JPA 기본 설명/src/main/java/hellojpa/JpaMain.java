package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //Transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            MemberV1 memberV1 = new MemberV1();
            memberV1.setUsername("member1");

            em.persist(memberV1);

            TeamV1 teamV1 = new TeamV1();
            teamV1.setName("teamA");
            teamV1.getMembers().add(memberV1);

            em.persist(teamV1);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
