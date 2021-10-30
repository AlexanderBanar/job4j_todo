package ru.job4j.auto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Engine engine1 = Engine.of("4-cylinder", "NK-3");
            Engine engine2 = Engine.of("6-cylinder", "GR-15");

            Car car1 = Car.of("Ford", "Fiesta", engine1);
            Car car2 = Car.of("KIA", "Sportage", engine2);
            Car car3 = Car.of("LADA", "Vesta", engine2);

            Driver driver1 = Driver.of("Ivan");
            driver1.getCars().add(car1);

            Driver driver2 = Driver.of("Sergey");
            driver2.getCars().add(car2);
            driver2.getCars().add(car3);

            session.persist(driver1);
            session.persist(driver2);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
