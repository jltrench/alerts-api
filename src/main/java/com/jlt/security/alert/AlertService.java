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

    public Alert save(Alert alertDao) {
        Alert alert = new Alert();
        alert.setType(alertDao.getType());
        alert.setMessage(alertDao.getMessage());
        alert.setCoords(alertDao.getCoords());
        alert.setAuthor(alertDao.getAuthor());

        return alertRepository.save(alert);
    }

    public void deleteById(Integer id) {
        alertRepository.deleteById(id);
    }

    public Optional<Alert> findById(Integer id) {
        return alertRepository.findById(id);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    public void deleteAll() {
        alertRepository.deleteAll();
    }

    public List<Alert> findAllByAuthor(String author) {
        return alertRepository.findAllByAuthor(author);
    }

    public Optional<Alert> findByIdAndAuthor(Integer id, String author) {
        return alertRepository.findByIdAndAuthor(id, author);
    }

    public void deleteAllByAuthor(String author) {
        List<Alert> alerts = alertRepository.findAllByAuthor(author);
        alerts.forEach(alert -> alertRepository.deleteById(alert.getId()));
    }

    public Alert save(AlertDao newAlert) {
        Alert alert = new Alert();
        alert.setType(newAlert.getType());
        alert.setMessage(newAlert.getMessage());
        alert.setCoords(newAlert.getCoords());
        alert.setAuthor(newAlert.getAuthor());
        return alertRepository.save(alert);
    }

    public List<Alert> findNearbyAlerts(String author, String coords, double radius) {
        List<Alert> alerts = alertRepository.findAllByAuthor(author);
        return alerts.stream()
                .filter(alert -> haversineDistance(alert.getCoords(), coords) <= radius)
                .collect(Collectors.toList());
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