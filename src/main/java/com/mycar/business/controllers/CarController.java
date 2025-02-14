package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.car.CarAddKm;
import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.CarService;
import com.mycar.business.services.impl.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cars")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class CarController {
    private final CarService carService;
    private final AuthService authService;

    public CarController(CarService carService, AuthService authService){
        this.carService = carService;
        this.authService = authService;
    }

    @PostMapping()
    public ResponseEntity<Object> create(HttpServletRequest request, @Valid @RequestBody CarCreateDTO carCreateDTO){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.createCar(user, carCreateDTO);

        if (car!=null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(car.getId())
                    .toUri();
            return ResponseEntity.created(location).body(car);
        } else {
            return ResponseEntity.badRequest().body("Ha habido un error al guardar el vehículo en la base de datos");
        }
    }

    @Transactional
    @PatchMapping("/deactivate/{carId}")
    public ResponseEntity<Object> deactivateCar(HttpServletRequest request, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        Map<String, String> results = carService.deactivateCar(user, carId);

        if(results.containsKey("OK")){
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(results);
        }
    }

    @Transactional
    @PatchMapping("/activate/{carId}")
    public ResponseEntity<Object> activateCar(HttpServletRequest request, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        Map<String, String> results = carService.activateCar(user, carId);

        if(results.containsKey("OK")){
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(results);
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getCars(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        List<CarQueryDTO> cars = carService.getCars(user.getId());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<Object> getCarById(HttpServletRequest request, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.getCarById(user.getId(), carId);
        if (car==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el Issue con ID: " + carId + ". Es probable que pertenezca a otro usuario.");
        }
        return ResponseEntity.ok(car);
    }

    @PatchMapping("/addKm")
    public ResponseEntity<Object> addKm(HttpServletRequest request, @Valid @RequestBody CarAddKm carAddKm){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.addKm(user, carAddKm);

        if (car!=null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(car.getId())
                    .toUri();
            return ResponseEntity.created(location).body(car);
        } else {
            return ResponseEntity.badRequest().body("Ha habido un error al actualizar los km del vehículo");
        }
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Object> deleteCar(HttpServletRequest request, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.deleteCarById(user.getId(), carId);
        if (car==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se ha podido eliminar el vehículo con ID: " + carId + ". Es probable que pertenezca a otro usuario.");
        }
        return ResponseEntity.ok(car);
    }

    @PutMapping("/{carId}")
    public ResponseEntity<Object> updateCar(HttpServletRequest request, @Valid @RequestBody CarCreateDTO carCreateDTO, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.updateCar(user, carCreateDTO, carId);

        if (car!=null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(car.getId())
                    .toUri();
            return ResponseEntity.created(location).body(car);
        } else {
            return ResponseEntity.badRequest().body("Ha habido un error al actualizar el vehículo en la base de datos");
        }
    }
}
