package com.ll.framework.ioc;

import com.ll.domain.testPost.repository.TestPostRepository;
import com.ll.domain.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {
        beans.put("testPostService", new TestPostService(new TestPostRepository()));
        beans.put("testPostRepository", new TestPostRepository());
    }

    public <T> T genBean(String beanName) {
        return (T) beans.get(beanName);
    }
}
