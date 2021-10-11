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
import java.util.function.Function;

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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            T rsl = command.apply(session);
            session.getTransaction().commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.models.Item").list()
        );
    }

    public List<Item> findAllOpen() {
        return this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "from ru.job4j.models.Item where done = :parameter");
                    query.setParameter("parameter", false);
                    return query.list();
                }
        );
    }

    public void closeTask(int id) {
        this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "update ru.job4j.models.Item set done = true where id = :parameterId");
                    query.setParameter("parameterId", id);
                    query.executeUpdate();
                    return id;
                }
        );
    }

    public void save(String description) {
         this.tx(
                session -> {
                    Item item = new Item(description, new Timestamp(System.currentTimeMillis()), false);
                    session.save(item);
                    return description;
                }
        );
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
