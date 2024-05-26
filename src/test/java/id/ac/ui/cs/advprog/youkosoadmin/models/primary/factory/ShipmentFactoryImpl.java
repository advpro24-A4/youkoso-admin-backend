package id.ac.ui.cs.advprog.youkosoadmin.models.primary.factory;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShipmentFactoryImplTest {

    private ShipmentFactoryImpl shipmentFactory;

    @BeforeEach
    void setUp() {
        shipmentFactory = new ShipmentFactoryImpl();
    }

    @Test
    void createShipment_ValidJTETrackingNumber_ReturnsShipment() {
        Long orderId = 1L;
        String shipmentMethod = "JTE";
        String trackingNumber = "JTE-123456789012";
        String shipmentStatus = "PENDING";

        Shipment shipment = shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus);

        assertNotNull(shipment);
        assertEquals(orderId, shipment.getOrderId());
        assertEquals(shipmentMethod, shipment.getShipmentMethod());
        assertEquals(trackingNumber, shipment.getTrackingNumber());
        assertEquals(shipmentStatus, shipment.getShipmentStatus());
    }

    @Test
    void createShipment_InvalidJTETrackingNumber_ThrowsException() {
        Long orderId = 1L;
        String shipmentMethod = "JTE";
        String trackingNumber = "JTE-INVALID";
        String shipmentStatus = "PENDING";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus));

        assertEquals("Invalid JTE tracking number", exception.getMessage());
    }

    @Test
    void createShipment_ValidGoBekTrackingNumber_ReturnsShipment() {
        Long orderId = 1L;
        String shipmentMethod = "Go-bek";
        String trackingNumber = "GBK-AB1234CD5678";
        String shipmentStatus = "PENDING";

        Shipment shipment = shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus);

        assertNotNull(shipment);
        assertEquals(orderId, shipment.getOrderId());
        assertEquals(shipmentMethod, shipment.getShipmentMethod());
        assertEquals(trackingNumber, shipment.getTrackingNumber());
        assertEquals(shipmentStatus, shipment.getShipmentStatus());
    }

    @Test
    void createShipment_InvalidGoBekTrackingNumber_ThrowsException() {
        Long orderId = 1L;
        String shipmentMethod = "Go-bek";
        String trackingNumber = "GBK-INVALID";
        String shipmentStatus = "PENDING";

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus));

        assertEquals("Invalid Go-bek tracking number", exception.getMessage());
    }

    @Test
    void createShipment_ValidSiWuzzTrackingNumber_ReturnsShipment() {
        Long orderId = 1L;
        String shipmentMethod = "SiWuzz";
        String trackingNumber = "SWZ-ABCDEFGHJKLM";
        String shipmentStatus = "PENDING";

        Shipment shipment = shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus);

        assertNotNull(shipment);
        assertEquals(orderId, shipment.getOrderId());
        assertEquals(shipmentMethod, shipment.getShipmentMethod());
        assertEquals(trackingNumber, shipment.getTrackingNumber());
        assertEquals(shipmentStatus, shipment.getShipmentStatus());
    }

    @Test
    void createShipment_InvalidSiWuzzTrackingNumber_ThrowsException() {
        Long orderId = 1L;
        String shipmentMethod = "SiWuzz";
        String trackingNumber = "SWZ-INVALID";
        String shipmentStatus = "PENDING";

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, shipmentStatus));

        assertEquals("Invalid SiWuzz tracking number", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_JTE_ReturnsValidTrackingNumber() {
        String trackingNumber = shipmentFactory.generateTrackingNumber("JTE");

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^JTE-\\d{12}$"));
    }

    @Test
    void generateTrackingNumber_GoBek_ReturnsValidTrackingNumber() {
        String trackingNumber = shipmentFactory.generateTrackingNumber("Go-bek");

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^GBK-[A-Z0-9]{12}$"));
    }

    @Test
    void generateTrackingNumber_SiWuzz_ReturnsValidTrackingNumber() {
        String trackingNumber = shipmentFactory.generateTrackingNumber("SiWuzz");

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^SWZ-[A-Z]{12}$"));
    }

    @Test
    void generateTrackingNumber_InvalidShipmentMethod_ThrowsException() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> shipmentFactory.generateTrackingNumber("INVALID"));

        assertEquals("Invalid shipment method", exception.getMessage());
    }
}
