package ru.job4j.reading;

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

            Book book1 = Book.of("History of Region");
            Book book2 = Book.of("History of City");

            Author author1 = Author.of("John Smith");
            author1.getBooks().add(book1);
            author1.getBooks().add(book2);

            Author author2 = Author.of("Robert Brown");
            author2.getBooks().add(book1);

            session.persist(author1);
            session.persist(author2);

            Author authorToRemove = session.get(Author.class, 1);
            session.remove(authorToRemove);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
