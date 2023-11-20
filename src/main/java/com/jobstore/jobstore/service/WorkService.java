package com.jobstore.jobstore.service;

import com.jobstore.jobstore.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkService {
    @Autowired
    private WorkRepository workRepository;
}
