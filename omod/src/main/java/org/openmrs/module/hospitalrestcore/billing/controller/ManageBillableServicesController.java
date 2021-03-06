/**
 * 
 */
package org.openmrs.module.hospitalrestcore.billing.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalrestcore.api.HospitalRestCoreService;
import org.openmrs.module.hospitalrestcore.billing.BillableService;
import org.openmrs.module.hospitalrestcore.billing.BillableServiceConfigurationDetails;
import org.openmrs.module.hospitalrestcore.billing.ServiceDetails;
import org.openmrs.module.hospitalrestcore.billing.ServicesDetails;
import org.openmrs.module.hospitalrestcore.concept.ConceptNode;
import org.openmrs.module.hospitalrestcore.concept.TestTree;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Ghanshyam
 *
 */
@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + "/services")
public class ManageBillableServicesController extends BaseRestController {

	@RequestMapping(value = "/billable", method = RequestMethod.GET)
	public void getServicesPrice(HttpServletResponse response, HttpServletRequest request)
			throws ResponseException, JsonGenerationException, JsonMappingException, IOException, ParseException {

		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();

		HospitalRestCoreService hospitalRestCoreService = Context.getService(HospitalRestCoreService.class);
		List<BillableService> services = hospitalRestCoreService.getAllServices();
		Set<Concept> priceCategorySet = new HashSet<Concept>();
		for (BillableService ser : services) {
			priceCategorySet.add(ser.getPriceCategoryConcept());
		}

		Map<String, Map<String, BillableServiceConfigurationDetails>> priceCategoryMap = new LinkedHashMap<String, Map<String, BillableServiceConfigurationDetails>>();

		for (Concept priceCategory : priceCategorySet) {
			List<BillableService> servicesByPriceCategory = hospitalRestCoreService
					.getServicesByPriceCategory(priceCategory);
			Map<String, BillableServiceConfigurationDetails> servicesPriceMap = new LinkedHashMap<String, BillableServiceConfigurationDetails>();
			for (BillableService ser : servicesByPriceCategory) {
				BillableServiceConfigurationDetails bscd = new BillableServiceConfigurationDetails();
				bscd.setPrice(ser.getPrice());
				bscd.setEnable(ser.getEnable());
				servicesPriceMap.put(ser.getServiceConcept().getUuid(), bscd);
			}
			priceCategoryMap.put(priceCategory.getUuid(), servicesPriceMap);
		}

		new ObjectMapper().writeValue(out, priceCategoryMap);
	}

	@RequestMapping(value = "/billable", method = RequestMethod.POST)
	public ResponseEntity<Void> manageBillableServices(HttpServletResponse response, HttpServletRequest request,
			@Valid @RequestBody ServicesDetails servicesDetails)
			throws ResponseException, JsonGenerationException, JsonMappingException, IOException, ParseException {
		HttpSession httpSession = request.getSession();

		ConceptService conceptService = Context.getService(ConceptService.class);
		Concept rootServiceconcept = conceptService.getConceptByName("SERVICES ORDERED");

		HospitalRestCoreService hospitalRestCoreService = Context.getService(HospitalRestCoreService.class);
		List<BillableService> services = hospitalRestCoreService.getAllServices();

		Map<Integer, BillableService> mapServices = new LinkedHashMap<Integer, BillableService>();

		for (BillableService ser : services) {
			mapServices.put(ser.getServiceConcept().getId(), ser);
		}

		// servicesDetails
		List<ServiceDetails> servicesList = servicesDetails.getServicesDetails();

		for (ServiceDetails serviceDetails : servicesList) {
			Concept serviceConcept = conceptService.getConceptByUuid(serviceDetails.getServiceConUuid());
			Concept priceCategoryConcept = conceptService.getConceptByUuid(serviceDetails.getPriceCategoryConUuid());
			BillableService service = hospitalRestCoreService
					.getServicesByServiceConceptAndPriceCategory(serviceConcept, priceCategoryConcept);
			if (service == null) {
				if (serviceConcept != null) {
					service = new BillableService();
					service.setServiceConcept(serviceConcept);
					service.setName(serviceConcept.getName().getName());
					if (serviceConcept.getShortNameInLocale(Locale.ENGLISH) != null) {
						service.setShortName(serviceConcept.getShortNameInLocale(Locale.ENGLISH).getName());
					}
					if (rootServiceconcept != null) {
						TestTree tree = new TestTree(rootServiceconcept);
						ConceptNode node = tree.findNode(serviceConcept);
						if (node != null) {
							while (!node.getParent().equals(tree.getRootLab())) {
								node = node.getParent();
							}
							service.setServiceCategoryConcept(node.getConcept());
						}
					}
					service.setPriceCategoryConcept(priceCategoryConcept);
					if (serviceDetails.getPrice() != null) {
						service.setPrice(serviceDetails.getPrice());
					}
					service.setEnable(serviceDetails.getEnable());
					mapServices.put(serviceConcept.getId(), service);
					hospitalRestCoreService.saveBillableService(service);
				}
			} else {
				service.setName(serviceConcept.getName().getName());
				if (serviceConcept.getShortNameInLocale(Locale.ENGLISH) != null) {
					service.setShortName(serviceConcept.getShortNameInLocale(Locale.ENGLISH).getName());
				}
				if (rootServiceconcept != null) {
					TestTree tree = new TestTree(rootServiceconcept);
					ConceptNode node = tree.findNode(serviceConcept);
					if (node != null) {
						while (!node.getParent().equals(tree.getRootLab())) {
							node = node.getParent();
						}
						service.setServiceCategoryConcept(node.getConcept());
					}
				}
				service.setPriceCategoryConcept(priceCategoryConcept);
				if (serviceDetails.getPrice() != null) {
					service.setPrice(serviceDetails.getPrice());
				}
				service.setEnable(serviceDetails.getEnable());
				mapServices.remove(serviceConcept.getId());
				mapServices.put(serviceConcept.getId(), service);
				hospitalRestCoreService.saveBillableService(service);
			}
		}
		// hospitalRestCoreService.saveBillableService(mapServices.values());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

}
