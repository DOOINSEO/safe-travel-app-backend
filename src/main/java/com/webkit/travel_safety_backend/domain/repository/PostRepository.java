package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long>, JpaSpecificationExecutor<Posts> {

    @Query(
            value = """
            select p
            from Posts p
            left join PostLike pl on pl.post = p
            where (:categoryId is null or p.category.id = :categoryId)
              and (:locationId is null or p.location.id = :locationId)
              and (:q is null or p.content like concat('%', :q, '%'))
            group by p
            order by count(pl.id) desc, p.id desc
        """,
            countQuery = """
            select count(p)
            from Posts p
            where (:categoryId is null or p.category.id = :categoryId)
              and (:locationId is null or p.location.id = :locationId)
              and (:q is null or p.content like concat('%', :q, '%'))
        """
    )
    Page<Posts> findAllOrderByLikeCountDesc(
            @Param("categoryId") Long categoryId,
            @Param("locationId") Long locationId,
            @Param("q") String q,
            Pageable pageable
    );
}
