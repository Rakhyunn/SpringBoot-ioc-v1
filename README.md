# Spring Ioc-V1

> 스프링 부트의 IoC 컨테이너를 직접 구현해보는 학습 프로젝트

## 📌 프로젝트 개요 / Overview

스프링 부트를 배운 적 없는 상태에서, TDD 방식으로 IoC 컨테이너의 핵심 동작을 단계적으로 구현했습니다.  
**하드코딩으로 테스트를 통과**시키는 그린(Green) 단계에 집중했습니다.


## 🔴🟢 TDD 사이클 / TDD Cycle

V1은 **레드 → 그린** 단계로 진행했으며 테스트 통과를 목적으로 진행하였습니다.

### t2 - Bean 가져오기

**레드** : `genBean()`이 null을 반환해서 실패  
**그린** : Map에 Bean을 하드코딩으로 등록

```java
public ApplicationContext() {
    beans.put("testPostService", new TestPostService(new TestPostRepository()));
}
```

### t3 - 싱글톤

**그린** : Map에서 꺼내는 방식으로 싱글톤 보장

```java
// Map에서 꺼내면 항상 같은 객체
public <T> T genBean(String beanName) {
    return (T) beans.get(beanName);
}
```

### t4, t5 - 의존성 동일 객체 보장

**레드** : `TestPostService` 안의 Repository와 `genBean("testPostRepository")`가 다른 객체  
**그린** : 변수 하나로 동일한 인스턴스를 두 곳에 전달

```java
// ❌ 실패 케이스 - 서로 다른 객체
beans.put("testPostService", new TestPostService(new TestPostRepository())); // A
beans.put("testPostRepository", new TestPostRepository());                   // B (A ≠ B)

// ✅ 통과 케이스 - 같은 객체 공유
TestPostRepository testPostRepository = new TestPostRepository();            // 하나만 생성
beans.put("testPostService", new TestPostService(testPostRepository));       // 같은 객체 주입
beans.put("testPostRepository", testPostRepository);
```

### t6 - 복합 의존성 주입

**레드** : `TestFacadePostService`가 `TestPostService`와 `TestPostRepository` 둘 다 필요  
**그린** : 의존성 순서를 지켜서 차례대로 생성

```java
public ApplicationContext() {
    TestPostRepository testPostRepository = new TestPostRepository();           // 1. 의존성 없음 → 먼저 생성
    TestPostService testPostService = new TestPostService(testPostRepository);  // 2. Repository 필요 → 두번째
    TestFacadePostService testFacadePostService =
        new TestFacadePostService(testPostService, testPostRepository);         // 3. 둘 다 필요 → 마지막

    beans.put("testPostRepository", testPostRepository);
    beans.put("testPostService", testPostService);
    beans.put("testFacadePostService", testFacadePostService);
}
```

## ✅ 테스트 결과 / Test Results

| 테스트 | 내용 | 결과 |
|--------|------|------|
| t1 | ApplicationContext 객체 생성 | ✅ |
| t2 | testPostService Bean 가져오기 | ✅ |
| t3 | 싱글톤 보장 | ✅ |
| t4 | testPostRepository Bean 가져오기 | ✅ |
| t5 | testPostService가 testPostRepository를 가지고 있는지 | ✅ |
| t6 | testFacadePostService가 testPostService, testPostRepository를 가지고 있는지 | ✅ |


## 💡 배운 점 & 회고 / Lessons Learned

**t4, t5에서 얻은 인사이트**

단순히 Repository를 두 번 `new` 했을 때 싱글톤이 깨진다는 것을 레드 케이스로 발견하였습니다.
"같은 객체를 써야 한다"는 의존성 주입을 알게 되어 하나를 생성하여 똑같이 넣어 해결하였습니다.
