package br.com.massao.logrequest.application.resource;

import br.com.massao.logrequest.application.dto.LogRequest;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.application.exception.ApiError;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.service.LogRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Log Request Resources Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/log-requests")
public class LogRequestResource {
    @Autowired
    private LogRequestService service;


    /**
     * List Logs
     *
     * @return
     */
    @GetMapping
    @ApiOperation(value = "List all logs")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<List<LogRequest>> list() {

        log.info("list>");

        List<LogRequest> resultList = new LogRequest().listLogRequestFrom(service.list());
        return ResponseEntity.ok(resultList);
    }


    /**
     * Find a Log by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Find a log request by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<LogRequest> findById(@PathVariable("id") Long id) throws NotFoundException {
        log.info("findById id={}", id);

        DomainLogRequest domain = service.findById(id);
        return ResponseEntity.ok(new LogRequest(domain));
    }


    /**
     * Create a Log
     *
     * @param form
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Create a log request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED) // This removes http 200 on Swagger
    public ResponseEntity<Void> create(@Valid @RequestBody LogRequestForm form) {
        log.info("create form={}", form);

        DomainLogRequest entity = service.save(form.toDomain());

        // nota: ResponseEntity retornando link do novo recurso no cabecalho da requisicao
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    /**
     * Update a Log
     *
     * @param id
     * @param form
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update a log request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Updated"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<LogRequest> update(@PathVariable("id") Long id, @Valid @RequestBody LogRequestForm form) throws NotFoundException {
        log.info("modify id={} form={}", id, form);

        DomainLogRequest modified = service.update(id, form.toDomain());

        return ResponseEntity.ok().body(new LogRequest(modified));
    }
}
