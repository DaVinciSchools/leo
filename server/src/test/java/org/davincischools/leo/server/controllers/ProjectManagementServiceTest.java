package org.davincischools.leo.server.controllers;

import com.google.common.truth.Truth;
import com.google.common.truth.extensions.proto.ProtoTruth;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.protos.pl_types.Project;
import org.davincischools.leo.protos.project_management.GenerateProjectsResponse;
import org.davincischools.leo.server.utils.http_executor.HttpExecutorLog;
import org.junit.Test;
import org.mockito.Mockito;

public class ProjectManagementServiceTest {
  HttpExecutorLog mockLog = Mockito.mock(HttpExecutorLog.class);

  @Test
  public void testProjectNumberColon() {
    Truth.assertThat(ProjectManagementService.normalizeAndCheckString("Project 1: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testProjectNumberColonQuoted() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString("Project 1: \"Title's Title\""))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testProjectNumberColonLabelColon() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString(
                "Project 1: Title: Primary Title: Subtitle"))
        .isEqualTo("Primary Title: Subtitle");
  }

  @Test
  public void testProjectNumberColonLabelColonQuoted() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString("Project 1: Title: \"Title's Title\""))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testShortDescrNumberColon() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString("Short Description: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testShortDescrNumberColonQuoted() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString("Short Description: “Title's Title”"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testDescrNumberColon() {
    Truth.assertThat(ProjectManagementService.normalizeAndCheckString("Description: Title's Title"))
        .isEqualTo("Title's Title");
  }

  @Test
  public void testNumberDotLabelColon() {
    Truth.assertThat(ProjectManagementService.normalizeAndCheckString("1. Title: This: Title"))
        .isEqualTo("This: Title");
  }

  @Test
  public void testBulletedFullDescription() {
    Truth.assertThat(
            ProjectManagementService.normalizeAndCheckString(
                """
Full Description:

- Full description 1a.


- Full description 1b.


- Full description 1c.


            """))
        .isEqualTo("""
- Full description 1a.
- Full description 1b.
- Full description 1c.""");
  }

  @Test
  public void textExtractProject() {
    GenerateProjectsResponse.Builder response = GenerateProjectsResponse.newBuilder();
    ProjectManagementService.extractProjects(
        mockLog,
        response,
        new ProjectInput(),
        String.format(
            """
Project 1:

Title: Title 1

%s

Short description: Short 1

%s

Full description:
Full 1

%s


Project 2:

Title: Title 2

%s

Short description: Short 2

%s

Full description:
Full 2

%s""",
            ProjectManagementService.END_OF_TITLE,
            ProjectManagementService.END_OF_SHORT,
            ProjectManagementService.START_OF_PROJECT,
            ProjectManagementService.END_OF_TITLE,
            ProjectManagementService.END_OF_SHORT,
            ProjectManagementService.START_OF_PROJECT));
    ProtoTruth.assertThat(response.build())
        .comparingExpectedFieldsOnly()
        .isEqualTo(
            GenerateProjectsResponse.newBuilder()
                .addProjects(
                    Project.newBuilder()
                        .setName("Title 1")
                        .setShortDescr("Short 1")
                        .setLongDescr("Full 1"))
                .addProjects(
                    Project.newBuilder()
                        .setName("Title 2")
                        .setShortDescr("Short 2")
                        .setLongDescr("Full 2"))
                .build());
  }
}
