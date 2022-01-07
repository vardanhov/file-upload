package com.example.uploadfile.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;


@Entity
@Table(name = "logging_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicUpdate
@DynamicInsert
public class LoggingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long eventId;

    @Column(name = "timestmp")
    private Long timestmp;

    @Column(name = "formatted_message")
    private String formattedMessage;

    @Column(name = "logger_name")
    private String loggerName;

    @Column(name = "level_string")
    private String levelString;

    @Column(name = "thread_name")
    private String threadName;

    @Column(name = "reference_flag")
    private Short referenceFlag;

    @Column(name = "arg0")
    private String arg0;

    @Column(name = "arg1")
    private String arg1;

    @Column(name = "arg2")
    private String arg2;

    @Column(name = "arg3")
    private String arg3;

    @Column(name = "caller_filename")
    private String callerFilename;

    @Column(name = "caller_class")
    private String callerClass;

    @Column(name = "caller_method")
    private String callerMethod;

    @Column(name = "caller_line")
    private String callerLine;

}
