package hu.bme.aut.retelab2.controller;

import hu.bme.aut.retelab2.EmailService;
import hu.bme.aut.retelab2.SecretGenerator;
import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.domain.Subscription;
import hu.bme.aut.retelab2.repository.AdRepository;
import hu.bme.aut.retelab2.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ads")
public class AdController {

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private EmailService emailService;

	@PostMapping
	public Ad create(@RequestBody Ad ad) {
		ad.setId(null);
		ad.setCreatedDate(null);
		ad.setToken(SecretGenerator.generate(6));
		return adRepository.save(ad);
	}

	@GetMapping
	public List<Ad> getAll() {
		List<Ad> ads = adRepository.findAll();
		ads.forEach(ad -> ad.setToken(null));
		return ads;
	}

	@GetMapping("price")
	public List<Ad> getAdsByPriceRange(
			@RequestParam(required = false, defaultValue = "0") int min,
			@RequestParam(required = false, defaultValue = "10000000") int max
	) {
		return adRepository.findByPriceRange(min, max);
	}

	@PutMapping
	public ResponseEntity<Ad> update(@RequestBody Ad ad) {
		Ad savedAd = adRepository.findById(ad.getId());
		if (savedAd == null || !savedAd.getToken().equals(ad.getToken())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		ad.setCreatedDate(savedAd.getCreatedDate());
		savedAd = adRepository.save(ad);

		// Send email
		List<Subscription> subscriptions = subscriptionRepository.findByAd(savedAd);
		Ad finalSavedAd = savedAd;
		subscriptions.forEach(subscription -> {
		/*
			emailService.sendSimpleMessage(
				subscription.getEmail(),
				"Ad details changed",
				"Dear " + subscription.getName() + ",\n\n" +
					"Details of Ad#" + finalSavedAd.getId() + " has changed.\n\n" +
					"Title: " + finalSavedAd.getTitle() + "\n" +
					"Description: " + finalSavedAd.getDescription() + "\n" +
					"Tags: " + (finalSavedAd.getTags() == null ? "-" : String.join(", ", finalSavedAd.getTags())) + "\n\n" +
					"Best regards\nAd Service"
			)
		 */
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("recipient", subscription.getName());
			parameters.put("adId", finalSavedAd.getId());
			parameters.put("adTitle", finalSavedAd.getTitle());
			parameters.put("adDescription", finalSavedAd.getDescription());
			parameters.put("adTags", finalSavedAd.getTags() == null ? "-" : String.join(", ", finalSavedAd.getTags()));
			emailService.sendMessageUsingThymeleafTemplate(subscription.getEmail(), "Ad details changed", parameters);
		});

		return ResponseEntity.ok(savedAd);
	}

	@GetMapping("{tag}")
	public List<Ad> getByTag(@PathVariable String tag) {
		return adRepository.findAll().stream()
				.filter(ad -> {
					if (ad.getTags().contains(tag)) {
						ad.setToken(null);
						return true;
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
	}


	@PostMapping("{id}/subscribe")
	public Subscription subscribe(@PathVariable Long id, @RequestBody Subscription subscription) {
		subscription.setAd(adRepository.findById(id));
		return subscriptionRepository.save(subscription);
	}

}