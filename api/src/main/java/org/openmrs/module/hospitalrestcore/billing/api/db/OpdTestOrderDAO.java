/**
 * 
 */
package org.openmrs.module.hospitalrestcore.billing.api.db;

import java.util.Date;
import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.hospitalrestcore.api.db.SingleClassDAO;
import org.openmrs.module.hospitalrestcore.billing.OpdTestOrder;

/**
 * @author Ghanshyam
 *
 */
public interface OpdTestOrderDAO extends SingleClassDAO {

	public OpdTestOrder getOpdTestOrderById(Integer opdOrderId) throws DAOException;

	public List<OpdTestOrder> getOpdTestOrder(Patient patient, Date creationDate) throws DAOException;

	public List<OpdTestOrder> getOpdTestOrderByEncounter(Encounter encounter) throws DAOException;

}
