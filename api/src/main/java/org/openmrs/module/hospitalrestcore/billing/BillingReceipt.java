/**
 *  Copyright 2010 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Hospital-core module.
 *
 *  Hospital-core module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Hospital-core module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Hospital-core module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.hospitalrestcore.billing;

import java.io.Serializable;
import java.util.Date;

public class BillingReceipt implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4573526140331707301L;
	
	private Integer billingReceiptId;
	
	private Date paidDate;

	public Integer getBillingReceiptId() {
		return billingReceiptId;
	}

	public void setBillingReceiptId(Integer billingReceiptId) {
		this.billingReceiptId = billingReceiptId;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	
}
