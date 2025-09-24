package org.example.socks.util.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.model.Socks;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SocksSpecificationUtil {
    public static Specification<Socks> build(SocksFilterDto dto) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.color() != null && !dto.color().isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("color")), dto.color().toLowerCase()));
            }
            if (dto.lessThanCotton() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cottonPercentage"), dto.lessThanCotton()));
            }
            if (dto.moreThanCotton() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cottonPercentage"), dto.moreThanCotton()));
            }
            if (dto.lessThanCount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("count"), dto.lessThanCount()));
            }
            if (dto.moreThanCount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("count"), dto.moreThanCount()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
