package com.sharebooks.repositories;


import com.sharebooks.models.UsuarisModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Interface per comunicar l'aplicació amb la bdD Postgre
 * @author Sergio Prieto Rufián
 * 
 */

@Repository
public interface UsuarisRepository extends CrudRepository<UsuarisModel, Long> {
}