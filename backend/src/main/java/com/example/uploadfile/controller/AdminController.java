package com.example.uploadfile.controller;

import com.example.uploadfile.service.iface.LoggingEventService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private final LoggingEventService eventService;

    public AdminController(LoggingEventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation(value = "Полчить историю событий пользователей")
    @GetMapping("/get-all")
    public ResponseEntity<?> getUserHistory(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("timestmp").descending());
        return ResponseEntity.ok(eventService.getAllEvent(paging));
    }


    @ApiOperation(value = "Получить историю событий пользователя")
    @GetMapping("/{userName}")
    public ResponseEntity<?> getUsrHistory(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @PathVariable String userName) {

        Pageable paging = PageRequest.of(page, size, Sort.by("timestmp").descending());

        try {
            return ResponseEntity.ok(eventService.getUserActionByUserName(userName, paging));
        } catch (RuntimeException exception) {
            //TODO Добавить ответ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @ApiOperation(value = "Получить историю событий пользователя")
    @GetMapping("/{user}/by-type/{eventType}")
    public ResponseEntity<?> getUsrHistory(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @PathVariable String user,
                                           @PathVariable String eventType) {

        Pageable paging = PageRequest.of(page, size, Sort.by("timestmp").descending());

        try {
            return ResponseEntity.ok(eventService.getUserActionByUserNameAndActionType(user, eventType, paging));
        } catch (RuntimeException exception) {
            //TODO Добавить ответ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }
}
