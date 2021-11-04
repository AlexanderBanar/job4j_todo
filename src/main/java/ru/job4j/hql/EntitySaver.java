package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class EntitySaver {
    public static void saveTestingEntities() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy vacancyBank1 = new Vacancy();
            vacancyBank1.setName("Bank operator");

            Vacancy vacancyBank2 = new Vacancy();
            vacancyBank2.setName("Bank inspector");

            Vacancy vacancyBank3 = new Vacancy();
            vacancyBank3.setName("Bank director");

            VacancyBase bankBase = new VacancyBase();
            bankBase.setName("Bank base");
            bankBase.addVacancy(vacancyBank1);
            bankBase.addVacancy(vacancyBank2);
            bankBase.addVacancy(vacancyBank3);

            Candidate ivan = new Candidate();
            ivan.setName("Ivan");
            ivan.setVacancyBase(bankBase);

            session.save(vacancyBank1);
            session.save(vacancyBank2);
            session.save(vacancyBank3);

            session.save(bankBase);
            session.save(ivan);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
