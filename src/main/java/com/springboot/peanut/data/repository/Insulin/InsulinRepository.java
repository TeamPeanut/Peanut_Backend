package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsulinRepository extends JpaRepository<Insulin,Long>,InsulinRepositoryCustom{
}
