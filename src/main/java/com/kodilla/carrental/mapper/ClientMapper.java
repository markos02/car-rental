package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.domain.ClientDto;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientMapper {

    private final OrderRepository orderRepository;

    public Client mapToClient(ClientDto clientDto) {

        List<Order> orders = clientDto.getOrdersIds().stream()
                .map(i -> orderRepository.findById(i).get())
                .collect(Collectors.toList());

        return new Client(
                clientDto.getClientId(),
                clientDto.getFirstname(),
                clientDto.getLastname(),
                orders
        );
    }

    public ClientDto mapToClientDto(Client client) {

        List<Integer> orders = client.getOrders().stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());

        return new ClientDto(
                client.getClientId(),
                client.getFirstname(),
                client.getLastname(),
                orders
        );
    }

    public List<ClientDto> mapToClientDtoList(List<Client> clients) {
        return clients.stream()
                .map(this::mapToClientDto)
                .collect(Collectors.toList());
    }
}
