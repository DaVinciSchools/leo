package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {}
