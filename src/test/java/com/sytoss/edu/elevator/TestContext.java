package com.sytoss.edu.elevator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class TestContext {

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    private ResponseEntity<String> response;

    private HashMap<Integer, Long> houseIds = new HashMap<>();

    private HashMap<Integer, List<Long>> shaftIds = new HashMap<>();

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void dropInstance() {
        testContext.set(null);
    }
}