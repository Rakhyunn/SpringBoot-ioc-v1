package com.ll.domain.testPost.service;

import com.ll.domain.testPost.repository.TestPostRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPostService {
    private final TestPostRepository testPostRepository;
}
