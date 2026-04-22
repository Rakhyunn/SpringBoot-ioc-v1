package com.ll.framework.ioc;

import com.ll.domain.testPost.repository.TestPostRepository;
import com.ll.domain.testPost.service.TestFacadePostService;
import com.ll.domain.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {
        TestPostRepository testPostRepository = new TestPostRepository();
        TestPostService testPostService = new TestPostService(testPostRepository);
        TestFacadePostService testFacadePostService = new TestFacadePostService(testPostService, testPostRepository);
        beans.put("testPostService", testPostService);
        beans.put("testPostRepository", testPostRepository);
        beans.put("testFacadePostService", testFacadePostService);
    }

    public <T> T genBean(String beanName) {
        return (T) beans.get(beanName);
    }
}
