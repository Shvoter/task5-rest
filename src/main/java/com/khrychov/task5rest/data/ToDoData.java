package com.khrychov.task5rest.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "todos")
@Setter
@Getter
@NoArgsConstructor
public class ToDoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String title;

    @OneToMany(mappedBy = "toDoData", cascade = CascadeType.DETACH)
    List<TaskData> tasksData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ToDoData toDoData = (ToDoData) o;
        return id != null && Objects.equals(id, toDoData.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
