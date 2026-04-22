package com.ll.framework.ioc;

import com.ll.domain.testPost.service.TestPostService;

public class ApplicationContext {
    public ApplicationContext() {

    }

    public <T> T genBean(String beanName) {
        if(beanName.equals("testPostService")) {
            TestPostService testPostService = new TestPostService(genBean("testPostRepository"));
            return (T) testPostService;
        }
        return (T) null;
    }
}
