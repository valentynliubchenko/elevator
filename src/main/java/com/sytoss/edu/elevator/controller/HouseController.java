package com.sytoss.edu.elevator.controller;

import com.sytoss.edu.elevator.params.HouseParams;
import com.sytoss.edu.elevator.services.HouseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/house")
public class HouseController {

    private final HouseService houseService;

    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"), @ApiResponse(responseCode = "400",
                    description = "Bad request")})
    @PostMapping("/register")
    public ResponseEntity<String> saveRequest(
            @RequestBody HouseParams houseParams) {
        long houseId = houseService.saveRequest(houseParams);
        return new ResponseEntity<>(String.valueOf(houseId), HttpStatusCode.valueOf(200));
    }
}
