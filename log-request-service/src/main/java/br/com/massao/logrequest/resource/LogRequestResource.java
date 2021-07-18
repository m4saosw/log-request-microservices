package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.exception.ApiError;
import br.com.massao.logrequest.exception.NotFoundException;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.service.LogRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

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
     * @param page
     * @return
     */
    @GetMapping
    @ApiOperation(value = "List all logs")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<Page<LogRequest>> list(@PageableDefault(size = 3, sort = "id") Pageable page) {

        log.info("list pageable=<{}>", page);

        Page<LogRequest> resultList = new LogRequest().listLogRequestFrom(service.list(page));
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

        LogRequestModel model = service.findById(id);
        return ResponseEntity.ok(new LogRequest(model));
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
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Void> create(@Valid @RequestBody LogRequestForm form) {
        log.info("create form={}", form);

        LogRequestModel log1 = new LogRequestModel(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // nota: ResponseEntity retornando link do novo recurso no cabecalho da requisicao
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(log1.getId()).toUri();

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
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<LogRequest> update(@PathVariable("id") Long id, @Valid @RequestBody LogRequestForm form) {
        log.info("modify id={} form={}", id, form);

        LogRequestModel model = new LogRequestModel(1L, form.getDate(), form.getIp(), form.getRequest(), form.getStatus(), form.getUserAgent());
        return ResponseEntity.ok(new LogRequest(model));
    }

}
