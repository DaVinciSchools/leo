package org.davincischools.leo.database.utils.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Instant;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminXRepository extends JpaRepository<AdminX, Integer> {

  default UserX upsert(UserX userX) {
    checkNotNull(userX);

    if (!UserXRepository.isAdminX(userX)) {
      userX.setAdminX(saveAndFlush(new AdminX().setCreationTime(Instant.now())));
    }
    return userX;
  }
}
