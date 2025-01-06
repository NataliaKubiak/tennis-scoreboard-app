package org.example.dao;

import org.example.entity.Player;
import org.hibernate.Session;

import java.util.Optional;

public interface Dao<Entity> {

        Entity save(Entity entity);
}
