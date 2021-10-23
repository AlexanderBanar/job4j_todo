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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                            "from ru.job4j.models.Item where user = :parameter");
                    query.setParameter("parameter", user);
                    return query.list();
                }
        );
    }

    public List<Item> findAllOpen(User user) {
        return this.tx(
                session -> {
                    Query<Item> query = session.createQuery(
                            "from ru.job4j.models.Item where done = false and user = :parameter");
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

    public void save(User user, String description, String[] cIds) {
         this.tx(
                session -> {
                    Item item = new Item(description, new Timestamp(System.currentTimeMillis()), false, user);
                    for (String id : cIds) {
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

    public static void main(String[] args) {
        User alexUser = User.of("AlexBanar", "password");
        PsqlTracker.instOf().saveUser(alexUser);
        User savedUser = PsqlTracker.instOf().getUser("AlexBanar");

        String[] forOne = {"1", "3"};
        String[] forTwo = {"4"};
        String[] forThree = {"2", "5"};
        String[] forFour = {"3"};
        String[] forFive = {"4"};
        String[] forSix = {"5"};

        PsqlTracker.instOf().save(savedUser, "to track all new activities", forOne);
        PsqlTracker.instOf().save(savedUser, "to find all Hibernate courses", forTwo);
        PsqlTracker.instOf().save(savedUser, "to find all Hibernate books", forThree);
        PsqlTracker.instOf().save(savedUser, "to fix bug on back-end", forFour);
        PsqlTracker.instOf().save(savedUser, "to develop learning plan", forFive);
        PsqlTracker.instOf().save(savedUser, "to fix the bug on front-side", forSix);

        List<Item> itemList = PsqlTracker.instOf().findAll(savedUser);

        for (Item m : itemList) {
            List<Category> categories = m.getCategories();
            for (Category category : categories) {
                System.out.println(m.getDescription() + ": " + category.getId());
            }
        }


    }
}
