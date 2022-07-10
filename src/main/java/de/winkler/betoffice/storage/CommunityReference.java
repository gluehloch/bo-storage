package de.winkler.betoffice.storage;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class CommunityReference {

	protected CommunityReference() {
	}
	
	private CommunityReference(String communityShortName) {
		CommunityReference cr = new CommunityReference();
		cr.setShortName(communityShortName);
	}
	
	public static CommunityReference of(String communityShortName) {
		if (StringUtils.isBlank(communityShortName)) {
			throw new IllegalArgumentException("Community name should be defined.");
		}

		return new CommunityReference(communityShortName);
	}
	
    @NotNull
    @Column(name = "bo_shortname")
	private String shortName;

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return shortName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(shortName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommunityReference other = (CommunityReference) obj;
		return StringUtils.equalsIgnoreCase(shortName, other.shortName);
	}

}
