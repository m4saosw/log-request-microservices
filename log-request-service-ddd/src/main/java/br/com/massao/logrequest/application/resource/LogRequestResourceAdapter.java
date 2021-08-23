package br.com.massao.logrequest.application.resource;

import br.com.massao.logrequest.application.dto.LogRequest;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.application.exception.ApiError;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.DomainLogRequestParams;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.service.LogRequestServicePort;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Log Request Resources Controller
 * Controller is the Adapter of an external Rest interface
 */
@Slf4j
@RestController
@RequestMapping("v1/log-requests")
public class LogRequestResourceAdapter {
    @Autowired
    private LogRequestServicePort service;


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
    public ResponseEntity<Page<LogRequest>> list(@PageableDefault(size = 10, sort = "id") Pageable page) {

        log.info("list pageable=<{}>", page);

        Page<LogRequest> resultList = new LogRequest().listLogRequestFromDomain(service.list(page));
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

        DomainLogRequest found = service.findById(id);
        return ResponseEntity.ok(new LogRequest(found));
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

        DomainLogRequest newDomain = form.toDomain();
        DomainLogRequest saved = service.save(newDomain);

        // nota: ResponseEntity retornando link do newDomain recurso no cabecalho da requisicao
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();

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

        DomainLogRequest newDomain = form.toDomain();
        DomainLogRequest updated = service.update(id, newDomain);

        return ResponseEntity.ok().body(new LogRequest(updated));
    }


    /**
     * Search logs by filters
     *
     * @param startDate
     * @param endDate
     * @param ip
     * @param request
     * @param status
     * @param userAgent
     * @param page
     * @return
     */
    @ApiOperation(value = "Search logs by filters")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @GetMapping("/search")
    public ResponseEntity<Page<LogRequest>> searchWithFilters(@RequestParam(value = "startDate", required = false) String startDate,
                                                              @RequestParam(value = "endDate", required = false) String endDate,
                                                              @RequestParam(value = "ip", required = false) String ip,
                                                              @RequestParam(value = "request", required = false) String request,
                                                              @RequestParam(value = "status", required = false) String status,
                                                              @RequestParam(value = "userAgent", required = false) String userAgent,
                                                              @PageableDefault(size = 10, sort = "id") Pageable page) {

        log.info("list startDate=<{}> endDate=<{}> ip=<{}> request=<{}> status=<{}> userAgent=<{}> pageable=<{}>", startDate, endDate, ip, request, status, userAgent, page);

        final DomainLogRequestParams params = new DomainLogRequestParams(startDate, endDate, ip, request, status, userAgent);

        boolean anyParam = params.haveStartDate() || params.haveEndDate() || params.haveIp() || params.haveRequests() || params.haveStatus() || params.haveUserAgent();
        if (!anyParam) return ResponseEntity.badRequest().build();

        Page<LogRequest> resultList = new LogRequest().listLogRequestFromDomain(service.searchByFilters(params, page));
        return ResponseEntity.ok(resultList);
    }

}