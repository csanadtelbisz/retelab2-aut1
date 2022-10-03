package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AdRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public Ad save(Ad ad) {
		return em.merge(ad);
	}

	public List<Ad> findAll() {
		return em.createQuery("SELECT a FROM Ad a", Ad.class).getResultList();
	}

	public Ad findById(long id) {
		return em.find(Ad.class, id);
	}

	public List<Ad> findByPriceRange(int min, int max) {
		return em.createQuery("SELECT a FROM Ad a WHERE ?1 <= a.price and a.price <= ?2", Ad.class)
				.setParameter(1, min)
				.setParameter(2, max)
				.getResultList();
	}

	@Transactional
	public void deleteById(long id) {
		Ad ad = findById(id);
		em.remove(ad);
	}
}
