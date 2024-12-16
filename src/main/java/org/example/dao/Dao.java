package org.example.dao;

import org.hibernate.Session;

import java.util.Optional;

public interface Dao<Entity> {

        Entity save(Entity entity, Session session);

//        Optional<Entity> getByPlayerName(String name, Session session);
}
