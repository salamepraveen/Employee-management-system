package com.ems.employee.config;

import org.springframework.context.annotation.Import;

import com.ems.common.exception.GlobalExceptionHandler;

@Import(GlobalExceptionHandler.class)
public class CommonConfig {

}
