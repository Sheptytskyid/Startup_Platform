package net.greatstart.controllers;

import net.greatstart.model.Project;
import net.greatstart.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public boolean createProject(Project project) {
        return projectService.createProject(project);
    }

    public boolean updateProject(long id) {
        return projectService.updateProject(id);
    }

    public boolean deleteProject(long id) {
        return projectService.deleteProject(id);
    }

    public Project getProjectById(long id) {
        return projectService.getProjectById(id);
    }

    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}