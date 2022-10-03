package hu.bme.aut.retelab2;

import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

	@Autowired
	private AdRepository adRepository;

	@Scheduled(fixedDelay = 60000)
	public void deleteExpiredAds() {
		List<Ad> ads = adRepository.findAll();
		ads.stream()
				.filter(ad -> ad.getExpiration() != null)
				.filter(ad -> ad.getExpiration().isBefore(LocalDateTime.now()))
				.forEach(ad -> adRepository.deleteById(ad.getId()));
	}
}
