package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


@Slf4j
@RestController
@RequestMapping("v1/log-requests")
public class LogRequestResource {


    @GetMapping
    public ResponseEntity<Page<LogRequest>> list(@PageableDefault(size = 3, sort = "id") Pageable page) {

        log.info("list pageable=<{}>", page);


        LogRequest log1 = new LogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");
        LogRequest log2 = new LogRequest(2L, LocalDateTime.now(), "ip2", "request", (short) 200, "userAgent");
        List<LogRequest> list = Arrays.asList(log1, log2);


        Page<LogRequest> listResult = new PageImpl(list);

        return ResponseEntity.ok(listResult);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LogRequest> findById(@PathVariable("id") Long id) {
        log.info("findById id={}", id);

        LogRequestModel model = new LogRequestModel(1L, LocalDateTime.now(), "ip", "request", (short) 200, "userAgent");
        return ResponseEntity.ok(new LogRequest(model));
    }


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody LogRequestForm form) {
        log.info("create form={}", form);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<LogRequest> update(@PathVariable("id") Long id, @Valid @RequestBody LogRequestForm form) {
        log.info("modify id={} form={}", id, form);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
