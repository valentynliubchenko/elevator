package com.sytoss.edu.elevator.controller;

import com.sytoss.edu.elevator.services.FloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FloorController {

    private final FloorService floorService;

    @Operation(description = "Call cabin to floor with UP direction")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"), @ApiResponse(responseCode = "400",
                    description = "Bad request")})
    @PostMapping("/house/{houseId}/floorButton/{floorNumber}/up")
    public void goUpCabinRequest(
            @PathVariable(value = "houseId")
            long houseId,
            @PathVariable(value = "floorNumber")
            int floorNumber) {
        floorService.goUpCabinRequest(houseId, floorNumber);
    }
}
