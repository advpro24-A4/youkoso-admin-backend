package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.factory.ShipmentFactory;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IShipmentRepository shipmentRepository;

    @Mock
    private ShipmentFactory shipmentFactory;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    private Order order;
    private Shipment shipment;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setStatus("WAITING_SHIPMENT");

        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setOrderId(order.getId());
        shipment.setShipmentMethod("JTE");
        shipment.setTrackingNumber("JTE-123456789012");
        shipment.setShipmentStatus("PENDING");
    }

    @Test
    void createShipment_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(shipmentFactory.generateTrackingNumber("JTE")).thenReturn("JTE-123456789012");
        when(shipmentFactory.createShipment(order.getId(), "JTE", "JTE-123456789012", "PENDING")).thenReturn(shipment);
        when(shipmentRepository.save(shipment)).thenReturn(shipment);
        when(orderRepository.save(order)).thenReturn(order);

        Shipment createdShipment = shipmentService.createShipment(order.getId(), "JTE");

        assertNotNull(createdShipment);
        assertEquals("JTE-123456789012", createdShipment.getTrackingNumber());
        assertEquals("JTE", createdShipment.getShipmentMethod());
        assertEquals("PENDING", createdShipment.getShipmentStatus());
        assertEquals(order.getId(), createdShipment.getOrderId());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentFactory, times(1)).generateTrackingNumber("JTE");
        verify(shipmentFactory, times(1)).createShipment(order.getId(), "JTE", "JTE-123456789012", "PENDING");
        verify(shipmentRepository, times(1)).save(shipment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createShipment_OrderNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> shipmentService.createShipment(order.getId(), "JTE"));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentFactory, never()).generateTrackingNumber(anyString());
        verify(shipmentFactory, never()).createShipment(anyLong(), anyString(), anyString(), anyString());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createShipment_ShipmentAlreadyExists() {
        order.setShipment(shipment);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> shipmentService.createShipment(order.getId(), "JTE"));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentFactory, never()).generateTrackingNumber(anyString());
        verify(shipmentFactory, never()).createShipment(anyLong(), anyString(), anyString(), anyString());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createShipment_InvalidOrderStatus() {
        order.setStatus("PROCESSING");
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> shipmentService.createShipment(order.getId(), "JTE"));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentFactory, never()).generateTrackingNumber(anyString());
        verify(shipmentFactory, never()).createShipment(anyLong(), anyString(), anyString(), anyString());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void arriveShipment_Success() {
        order.setStatus("SHIPPED");
        shipment.setShipmentStatus("SHIPPED");
        order.setShipment(shipment);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(shipmentRepository.save(shipment)).thenReturn(shipment);
        when(orderRepository.save(order)).thenReturn(order);

        Shipment arrivedShipment = shipmentService.arriveShipment(order.getId());

        assertNotNull(arrivedShipment);
        assertEquals("ARRIVED", arrivedShipment.getShipmentStatus());
        assertEquals("ARRIVED", order.getStatus());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentRepository, times(1)).save(shipment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void arriveShipment_OrderNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> shipmentService.arriveShipment(order.getId()));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void arriveShipment_ShipmentNotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        order.setShipment(null);

        assertThrows(BadRequestException.class, () -> shipmentService.arriveShipment(order.getId()));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void arriveShipment_InvalidOrderStatus() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> shipmentService.arriveShipment(order.getId()));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void arriveShipment_AlreadyArrived() {
        order.setStatus("SHIPPED");
        shipment.setShipmentStatus("ARRIVED");
        order.setShipment(shipment);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> shipmentService.arriveShipment(order.getId()));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
