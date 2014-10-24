package com.ewebstore.entity;

import com.ewebstore.model.ContactInformationQueryModel;

/**
 * The ContactInformation class is an entity encapsulating necessary information
 * on contacts.
 * 
 * @author ewebstore.com
 *
 */
public class ContactInformation {
	private String name = ContactInformationQueryModel.getShopName();
	private String address = ContactInformationQueryModel.getAddress();
	private String district = ContactInformationQueryModel.getDistrict();
	private String mobile = ContactInformationQueryModel.getMobile();
	private String fax = ContactInformationQueryModel.getFax();
	private String email = ContactInformationQueryModel.getEmail();
	private String facebookLink = ContactInformationQueryModel
			.getFacebookLink();
	private String twitterLink = ContactInformationQueryModel.getTwitterLink();
	private String googleplusLink = ContactInformationQueryModel
			.getGoogleplusLink();
	private String youtubeLink = ContactInformationQueryModel.getYoutubeLink();

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getDistrict() {
		return district;
	}

	public String getMobile() {
		return mobile;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public String getGoogleplusLink() {
		return googleplusLink;
	}

	public String getYoutubeLink() {
		return youtubeLink;
	}
}
