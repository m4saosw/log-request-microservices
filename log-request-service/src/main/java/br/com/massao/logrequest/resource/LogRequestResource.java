package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Page<LogRequest> list = new Page<LogRequest>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super LogRequest, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<LogRequest> getContent() {
                LogRequest log1 = new LogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");
                LogRequest log2 = new LogRequest(2L, LocalDateTime.now(), "ip2", "request", (short) 200, "userAgent");
                List<LogRequest> list = new ArrayList<>();
                list.add(log1);
                list.add(log2);
                return list;

            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<LogRequest> iterator() {
                return null;
            }
        };

        return ResponseEntity.ok(list);
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
