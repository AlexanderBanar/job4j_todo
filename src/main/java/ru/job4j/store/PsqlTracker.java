package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.models.Item;

import java.sql.Timestamp;
import java.util.List;

public class PsqlTracker implements AutoCloseable {
    private final StandardServiceRegistry registry;
    private final SessionFactory sf;

    private PsqlTracker() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final PsqlTracker INST = new PsqlTracker();
    }

    public static PsqlTracker instOf() {
        return Lazy.INST;
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from ru.job4j.models.Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> findAllOpen() {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Item> query = session.createQuery(
                "from ru.job4j.models.Item where done = :parameter");
        query.setParameter("parameter", false);
        List<Item> result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Item> query = session.createQuery(
                "from ru.job4j.models.Item where id = :parameter");
        query.setParameter("parameter", id);
        List<Item> result = query.list();
        session.getTransaction().commit();
        session.close();
        return (result.size() != 0) ? result.get(0) : null;
    }

    public void closeTask(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    public void save(String description) {
        Item item = new Item(description, new Timestamp(System.currentTimeMillis()), false);
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
