package org.example.socks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String color;
    int cottonPercentage;
    @Min(value = 0, message = "Количество носков не может быть меньше нуля")
    int count;
}
