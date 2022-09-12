package hellojpa;

import javax.persistence.*;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //Transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("homeCity", "street", "address");
            MemberV5 member = new MemberV5();
            member.setUsername("member1");
            member.setHomeAddress(address);

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity(new Address("old1", "street", "address")));
            member.getAddressHistory().add(new AddressEntity(new Address("old2", "street", "address")));
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("==============START===============");
            MemberV5 findMember = em.find(MemberV5.class, member.getId());

            //homecity -> newcity
            Address newAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", newAddress.getStreet(), newAddress.getZipcode()));

            //치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }

        emf.close();
    }

    private static void printMember(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team.getName() = " + team.getName());
    }
}
