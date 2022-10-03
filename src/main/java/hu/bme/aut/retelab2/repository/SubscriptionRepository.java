package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.domain.Subscription;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SubscriptionRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public Subscription save(Subscription subscription) {
		return em.merge(subscription);
	}

	public List<Subscription> findByAd(Ad ad) {
		return em.createQuery("SELECT s FROM Subscription s WHERE s.ad = ?1", Subscription.class)
				.setParameter(1, ad)
				.getResultList();
	}
}
