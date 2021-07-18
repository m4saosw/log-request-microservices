package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Log Request Resources Controller
 */
@Slf4j
@RestController
@RequestMapping("v1/log-requests")
public class LogRequestResource {

    /**
     * List Logs
     * @param page
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<LogRequest>> list(@PageableDefault(size = 3, sort = "id") Pageable page) {

        log.info("list pageable=<{}>", page);
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequest log1 = new LogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        LogRequest log2 = new LogRequest(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");
        List<LogRequest> list = Arrays.asList(log1, log2);


        Page<LogRequest> listResult = new PageImpl(list);

        return ResponseEntity.ok(listResult);
    }


    /**
     * Find a Log by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<LogRequest> findById(@PathVariable("id") Long id) {
        log.info("findById id={}", id);

        LogRequestModel model = new LogRequestModel(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");
        return ResponseEntity.ok(new LogRequest(model));
    }


    /**
     * Create a Log
     * @param form
     * @return
     */
    @PostMapping
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
     * @param id
     * @param form
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<LogRequest> update(@PathVariable("id") Long id, @Valid @RequestBody LogRequestForm form) {
        log.info("modify id={} form={}", id, form);

        LogRequestModel model = new LogRequestModel(1L, form.getDate(), form.getIp(), form.getRequest(), form.getStatus(), form.getUserAgent());
        return ResponseEntity.ok(new LogRequest(model));
    }

}
