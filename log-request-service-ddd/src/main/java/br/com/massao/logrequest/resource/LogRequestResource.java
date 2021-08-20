package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.application.converter.LogRequestModelConverter;
import br.com.massao.logrequest.application.dto.LogRequest;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.application.exception.ApiError;
import br.com.massao.logrequest.application.exception.NotFoundException;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.repository.query.LogRequestSpecifications;
import br.com.massao.logrequest.service.LogRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Log Request Resources Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/log-requests")
public class LogRequestResource {
    @Autowired
    private LogRequestService service;

    @Autowired
    private LogRequestModelConverter converter;

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
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED) // This removes http 200 on Swagger
    public ResponseEntity<Void> create(@Valid @RequestBody LogRequestForm form) {
        log.info("create form={}", form);

        LogRequestModel model = converter.modelFrom(form);
        LogRequestModel entity = service.save(model);

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

        LogRequestModel model = converter.modelFrom(form);
        LogRequestModel modified = service.update(id, model);

        return ResponseEntity.ok().body(new LogRequest(modified));
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

        final LogRequestParams params = new LogRequestParams(startDate, endDate, ip, request, status, userAgent);

        boolean anyParam = params.haveStartDate() || params.haveEndDate() || params.haveIp() || params.haveRequests() || params.haveStatus() || params.haveUserAgent();
        if (!anyParam) return ResponseEntity.badRequest().build();

        final Specification<LogRequestModel> specification = new LogRequestSpecifications().fromParams(params);
        Page<LogRequest> resultList = new LogRequest().listLogRequestFrom(service.searchByFilters(specification, page));
        return ResponseEntity.ok(resultList);
    }

}
