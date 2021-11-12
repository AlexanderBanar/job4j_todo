package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.models.Category;
import ru.job4j.models.Item;
import ru.job4j.models.User;

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

    public User getUser(String name) {
        return this.tx(
                session -> {
                    Query<User> query = session.createQuery(
                            "from ru.job4j.models.User where name = :parameter");
                    query.setParameter("parameter", name);
                    return query.uniqueResult();
                }
        );
    }

    public void saveUser(User user) {
        this.tx(
                session -> {
                    session.persist(user);
                    return user;
                }
        );
    }

    public List<Item> findAll(User user) {
        return this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "select distinct t from ru.job4j.models.Item t join fetch t.categories where t.user = :parameter");
                    query.setParameter("parameter", user);
                    return query.list();
                }
        );
    }

    public List<Item> findAllOpen(User user) {
        return this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "select distinct t from ru.job4j.models.Item t join fetch t.categories where t.done = false and t.user = :parameter");
                    query.setParameter("parameter", user);
                    return query.list();
                }
        );
    }

    public void closeTask(User user, int id) {
        if (belongsToUser(user, id)) {
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
    }

    private boolean belongsToUser(User user, int id) {
        User userOfId = this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "from ru.job4j.models.Item where id = :parameter");
                    query.setParameter("parameter", id);
                    return query.list().get(0).getUser();
                }
        );
        return user.equals(userOfId);
    }

    public void save(User user, String description, String[] ctIds) {
         this.tx(
                session -> {
                    Item item = new Item(description, user);
                    for (String id : ctIds) {
                        Category category = session.find(Category.class, Integer.parseInt(id));
                        item.getCategories().add(category);
                    }
                    session.persist(item);
                    return description;
                }
        );
    }

    public List<Category> getAllCategories() {
        return this.tx(
                session -> session.createQuery("select c from Category c", Category.class).list()
        );
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
