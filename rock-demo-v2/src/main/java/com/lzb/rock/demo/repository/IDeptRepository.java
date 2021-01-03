package com.lzb.rock.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lzb.rock.demo.entity.Dept;

@Repository
public interface IDeptRepository extends JpaRepository<Dept, Long> {

}
