package br.com.massao.logrequest.dto;

import br.com.massao.logrequest.resource.LogRequestImportFromFileResource;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class JobExecutionResource extends RepresentationModel<JobExecutionResource> {
    private JobExecutionOutput jobExecution;

    // For Jackson
    private JobExecutionResource() {
    }

    public JobExecutionResource(final JobExecutionOutput job) {
        this.jobExecution = job;
        try {
            final Link statusLink = linkTo(methodOn(LogRequestImportFromFileResource.class).status(job.getJobId())).withRel("status");
            add(statusLink);
        } catch (NoSuchJobException e) {
            e.printStackTrace();
        }
    }

    public JobExecutionOutput getJobExecution() {
        return jobExecution;
    }
}
