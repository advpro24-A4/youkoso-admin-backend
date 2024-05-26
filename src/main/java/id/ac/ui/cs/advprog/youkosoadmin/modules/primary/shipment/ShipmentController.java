package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.exceptions.UnauthorizedException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment.dto.ArriveShipmentRequest;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment.dto.CreateShipmentRequest;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthService;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponseBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
    private final IShipmentService shipmentService;
    private final AuthService authService;

    public ShipmentController(IShipmentService shipmentService, AuthService authService) {
        this.shipmentService = shipmentService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<DefaultResponse<Shipment>> createShipment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CreateShipmentRequest createShipmentRequest
    ){

        if(createShipmentRequest.getOrderId() == null || createShipmentRequest.getShipmentMethod() == null){
            throw new BadRequestException("Order ID and Shipment Method must be provided");
        }

        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }


        String shipmentMethod = createShipmentRequest.getShipmentMethod();

        Shipment shipment = shipmentService.createShipment(createShipmentRequest.getOrderId(), shipmentMethod);

        DefaultResponse<Shipment> response = new DefaultResponseBuilder<Shipment>()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Shipment created")
                .data(shipment)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/arrive")
    public ResponseEntity<DefaultResponse<Shipment>> arriveShipment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ArriveShipmentRequest request
            ){
        if(request.getOrderId() == null){
            throw new BadRequestException("Shipment ID must be provided");
        }

        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Shipment shipment = shipmentService.arriveShipment(request.getOrderId());

        DefaultResponse<Shipment> response = new DefaultResponseBuilder<Shipment>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Shipment arrived")
                .data(shipment)
                .build();

        return ResponseEntity.ok(response);
    }

}
