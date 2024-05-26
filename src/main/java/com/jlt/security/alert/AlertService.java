package com.jlt.security.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;

    public Alert save(AlertDto newAlertDto, String author) {
        Alert alert = new Alert();
        alert.setType(newAlertDto.getType());
        alert.setMessage(newAlertDto.getMessage());
        alert.setCoords(newAlertDto.getCoords());
        alert.setAuthor(author);
        return alertRepository.save(alert);
    }

    public Optional<Alert> updateAlert(Integer id, AlertDto updatedAlertDto, String author) {
        Optional<Alert> alertOpt = alertRepository.findByIdAndAuthor(id, author);
        if (alertOpt.isPresent()) {
            Alert alert = alertOpt.get();
            alert.setType(updatedAlertDto.getType());
            alert.setMessage(updatedAlertDto.getMessage());
            alert.setCoords(updatedAlertDto.getCoords());
            alertRepository.save(alert);
        }
        return alertOpt;
    }

    public boolean deleteByIdAndAuthor(Integer id, String author) {
        Optional<Alert> alertOpt = alertRepository.findByIdAndAuthor(id, author);
        if (alertOpt.isPresent()) {
            alertRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Alert> findAllByAuthor(String author) {
        return alertRepository.findAllByAuthor(author);
    }

    public Optional<Alert> findByIdAndAuthor(Integer id, String author) {
        return alertRepository.findByIdAndAuthor(id, author);
    }

    public List<Alert> findNearbyAlerts(String author, String coords, double radius) {
        List<Alert> alerts = alertRepository.findAllByAuthor(author);
        return alerts.stream()
                .filter(alert -> haversineDistance(alert.getCoords(), coords) <= radius)
                .collect(Collectors.toList());
    }

    public void update(Alert alert) {

        alertRepository.save(alert);
    }

    private double haversineDistance(String coords1, String coords2) {
        String[] splitCoords1 = coords1.split(",");
        String[] splitCoords2 = coords2.split(",");

        double lat1 = Double.parseDouble(splitCoords1[0]);
        double lon1 = Double.parseDouble(splitCoords1[1]);
        double lat2 = Double.parseDouble(splitCoords2[0]);
        double lon2 = Double.parseDouble(splitCoords2[1]);

        // Converte graus para radianos
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Aplica a fórmula de Haversine
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Raio da Terra em quilômetros
        double r = 6371;

        // Calcula e retorna a distância
        return c * r;
    }
}