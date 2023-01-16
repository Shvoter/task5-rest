package com.khrychov.task5rest.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tasks")
@Setter
@Getter
@NoArgsConstructor
public class TaskData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String title;

    @Column(nullable = false, length = 40)
    private String state;

    @Column(nullable = false, length = 40)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private ToDoData toDoData;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskData taskData = (TaskData) o;
        return id != null && Objects.equals(id, taskData.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
