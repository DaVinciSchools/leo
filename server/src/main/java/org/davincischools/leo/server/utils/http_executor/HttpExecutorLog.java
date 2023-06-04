package org.davincischools.leo.server.utils.http_executor;

import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.utils.repos.LogRepository;

public interface HttpExecutorLog {

  HttpExecutorLog addNote(String pattern, Object... args);

  HttpExecutorLog addProjectInput(ProjectInput projectInput);

  HttpExecutorLog addProject(Project project);

  HttpExecutorLog setStatus(LogRepository.Status status);
}
