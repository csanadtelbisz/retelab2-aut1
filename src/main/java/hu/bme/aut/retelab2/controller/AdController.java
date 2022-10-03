package hu.bme.aut.retelab2.controller;

import hu.bme.aut.retelab2.SecretGenerator;
import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ads")
public class AdController {

	@Autowired
	private AdRepository adRepository;

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

	@GetMapping("a")
	public List<Ad> getAll2() {
		return adRepository.findAll();
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
		return ResponseEntity.ok(savedAd);
	}

	@GetMapping("{tag}")
	public List<Ad> getByTag(@PathVariable String tag) {
		return adRepository.findAll().stream()
				.filter(ad -> {
					if(ad.getTags().contains(tag)){
						ad.setToken(null);
						return true;
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
	}

}