package com.assignment.repositories;

import com.assignment.entity.users.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateRepository  extends JpaRepository<Affiliate, Integer> {
}
