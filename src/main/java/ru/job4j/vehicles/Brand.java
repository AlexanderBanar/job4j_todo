package ru.job4j.vehicles;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models = new ArrayList<>();

    public static Brand of(String name) {
        Brand brand = new Brand();
        brand.name = name;
        return brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addModel(Model model) {
        this.models.add(model);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Brand brand = (Brand) o;
        return id == brand.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
