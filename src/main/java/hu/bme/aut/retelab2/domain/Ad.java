package hu.bme.aut.retelab2.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
public class Ad {

	@Id
	@GeneratedValue
	private Long id;

	private String title;

	private String description;

	private int price;

	@CreationTimestamp
	private Date createdDate;

	private String token;

	@ElementCollection
	private Set<String> tags;

	private LocalDateTime expiration = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tag) {
		this.tags = tag;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}
}
