package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Candidate> allCandidates = session.createQuery("from Candidate").list();
            for (Candidate can : allCandidates) {
                System.out.println(can);
            }

            Query query2 = session.createQuery("from Candidate where id = :param")
                    .setParameter("param", 2);
            System.out.println(query2.uniqueResult());

            List<Candidate> searchForIvans = session.createQuery("from Candidate where name = :param")
                    .setParameter("param", "Ivan")
                    .list();
            for (Candidate can : searchForIvans) {
                System.out.println(can);
            }

            session.createQuery("update Candidate c set c.name = :param1 where c.id = :param2")
                    .setParameter("param1", "Sergey")
                    .setParameter("param2", 2)
                    .executeUpdate();

            session.createQuery("delete from Candidate where id = :param")
                    .setParameter("param", 1)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
