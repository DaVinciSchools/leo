package org.davincischools.leo.server.utils.http_executor;

import org.davincischools.leo.database.daos.Log.StatusType;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;

public interface HttpExecutorLog {

  HttpExecutorLog addNote(String pattern, Object... args);

  HttpExecutorLog addProjectInput(ProjectInput projectInput);

  HttpExecutorLog addProject(Project project);

  HttpExecutorLog setIssueLink(String issueLink);

  HttpExecutorLog setStatus(StatusType status);
}
