package com.sytoss.edu.elevator.cucumber;

import com.sytoss.edu.elevator.IntegrationTest;
import com.sytoss.edu.elevator.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CucumberHooks extends IntegrationTest {

    @After
    public void tearDown() {
        TestContext.dropInstance();
        log.info("tearDown completed...");
    }

    @ParameterType(".*")
    public List<Integer> intList(String value) {
        return Arrays.stream(value.split(",")).map(s -> Integer.valueOf(s.trim())).collect(Collectors.toList());
    }

    @ParameterType(".*")
    public List<String> stringList(String value) {
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
