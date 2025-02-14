package com.mycar.business.controllers.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ControllerHelper {

    public Pageable getPageable(HttpServletRequest request){
        int page = Integer.parseInt(request.getParameter("page"));
        int size = Integer.parseInt(request.getParameter("size"));
        Sort sort = getSortBy(request.getParameter("sort"));

        return PageRequest.of(page, size, sort);
    }

    public static Sort getSortBy(String sort) {
        return sort.startsWith("-") ? Sort.by(sort.substring(1)).descending() : Sort.by(sort).ascending();
    }
}

