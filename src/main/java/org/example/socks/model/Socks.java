package org.example.socks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String color;
    int cottonPercentage;
    @Min(value = 0, message = "Количество носков не может быть меньше нуля")
    int count;
}
