/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.hospitalrestcore;

import java.math.RoundingMode;
import java.util.Currency;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either
 * started or stopped.
 */
public class HospitalRestCoreActivator implements ModuleActivator {

	protected Log log = LogFactory.getLog(getClass());

	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Hospital Rest Core Module");
	}

	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Hospital Rest Core Module refreshed");
	}

	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Hospital Rest Core Module");
	}

	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("Hospital Rest Core Module started");
		Money.init(Currency.getInstance("INR"), RoundingMode.HALF_EVEN);
	}

	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Hospital Rest Core Module");
	}

	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Hospital Rest Core Module stopped");
	}

}
