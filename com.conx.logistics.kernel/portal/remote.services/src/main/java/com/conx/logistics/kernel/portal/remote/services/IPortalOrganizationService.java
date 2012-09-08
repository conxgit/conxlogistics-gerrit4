package com.conx.logistics.kernel.portal.remote.services;

import java.util.Set;

import com.conx.logistics.mdm.domain.organization.Organization;

public interface IPortalOrganizationService {
	public Set<Organization> getOrganizationsByCompanyId(String companyId);
	
	public Organization provideOrganization(String portalOrganizationId);	
}
