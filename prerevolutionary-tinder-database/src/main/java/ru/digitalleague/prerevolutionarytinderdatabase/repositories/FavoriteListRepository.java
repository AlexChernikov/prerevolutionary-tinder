package ru.digitalleague.prerevolutionarytinderdatabase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.FavoriteList;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {
    public Optional<FavoriteList> findByPersonIdAndFavoritePersonId(Long personId, Long favoritePersonId);

    @Query(value = "SELECT EXISTS (SELECT * FROM tinder.favoritelist WHERE person_id = ?1 AND favorite_person_id = ?2)", nativeQuery = true)
    public boolean containsByPersonIdAndFavoritePersonId(Long personId, Long favoritePersonId);
}
