package ru.job4j.vehicles;

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
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = Model.of("Optima");
            Model two = Model.of("Rio");
            Model three = Model.of("Quoris");
            Model four = Model.of("Soul");
            Model five = Model.of("Stringer");

            Brand kia = Brand.of("KIA");
            kia.addModel(one);
            kia.addModel(two);
            kia.addModel(three);
            kia.addModel(four);
            kia.addModel(five);

            session.save(kia);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
