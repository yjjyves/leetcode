package com.yves.spring.spring.aop.advisor;

import java.util.List;

public interface AdvisorRegistry {
    void registAdvisor(Advisor ad);

    List<Advisor> getAdvisors();
}
