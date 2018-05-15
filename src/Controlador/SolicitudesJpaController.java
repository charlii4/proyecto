/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Responsables;
import Entidades.Facturas;
import java.util.ArrayList;
import java.util.List;
import Entidades.Ordenes;
import Entidades.Solicitudes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class SolicitudesJpaController implements Serializable {

    public SolicitudesJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Solicitudes solicitudes) throws PreexistingEntityException, Exception {
        if (solicitudes.getFacturasList() == null) {
            solicitudes.setFacturasList(new ArrayList<Facturas>());
        }
        if (solicitudes.getOrdenesList() == null) {
            solicitudes.setOrdenesList(new ArrayList<Ordenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Responsables responsable = solicitudes.getResponsable();
            if (responsable != null) {
                responsable = em.getReference(responsable.getClass(), responsable.getCodResponsable());
                solicitudes.setResponsable(responsable);
            }
            List<Facturas> attachedFacturasList = new ArrayList<Facturas>();
            for (Facturas facturasListFacturasToAttach : solicitudes.getFacturasList()) {
                facturasListFacturasToAttach = em.getReference(facturasListFacturasToAttach.getClass(), facturasListFacturasToAttach.getFacturasPK());
                attachedFacturasList.add(facturasListFacturasToAttach);
            }
            solicitudes.setFacturasList(attachedFacturasList);
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : solicitudes.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getOrdenesPK());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            solicitudes.setOrdenesList(attachedOrdenesList);
            em.persist(solicitudes);
            if (responsable != null) {
                responsable.getSolicitudesList().add(solicitudes);
                responsable = em.merge(responsable);
            }
            for (Facturas facturasListFacturas : solicitudes.getFacturasList()) {
                Solicitudes oldSolicitudesOfFacturasListFacturas = facturasListFacturas.getSolicitudes();
                facturasListFacturas.setSolicitudes(solicitudes);
                facturasListFacturas = em.merge(facturasListFacturas);
                if (oldSolicitudesOfFacturasListFacturas != null) {
                    oldSolicitudesOfFacturasListFacturas.getFacturasList().remove(facturasListFacturas);
                    oldSolicitudesOfFacturasListFacturas = em.merge(oldSolicitudesOfFacturasListFacturas);
                }
            }
            for (Ordenes ordenesListOrdenes : solicitudes.getOrdenesList()) {
                Solicitudes oldSolicitudesOfOrdenesListOrdenes = ordenesListOrdenes.getSolicitudes();
                ordenesListOrdenes.setSolicitudes(solicitudes);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
                if (oldSolicitudesOfOrdenesListOrdenes != null) {
                    oldSolicitudesOfOrdenesListOrdenes.getOrdenesList().remove(ordenesListOrdenes);
                    oldSolicitudesOfOrdenesListOrdenes = em.merge(oldSolicitudesOfOrdenesListOrdenes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSolicitudes(solicitudes.getCodSolicitud()) != null) {
                throw new PreexistingEntityException("Solicitudes " + solicitudes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Solicitudes solicitudes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitudes persistentSolicitudes = em.find(Solicitudes.class, solicitudes.getCodSolicitud());
            Responsables responsableOld = persistentSolicitudes.getResponsable();
            Responsables responsableNew = solicitudes.getResponsable();
            List<Facturas> facturasListOld = persistentSolicitudes.getFacturasList();
            List<Facturas> facturasListNew = solicitudes.getFacturasList();
            List<Ordenes> ordenesListOld = persistentSolicitudes.getOrdenesList();
            List<Ordenes> ordenesListNew = solicitudes.getOrdenesList();
            List<String> illegalOrphanMessages = null;
            for (Facturas facturasListOldFacturas : facturasListOld) {
                if (!facturasListNew.contains(facturasListOldFacturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Facturas " + facturasListOldFacturas + " since its solicitudes field is not nullable.");
                }
            }
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenes " + ordenesListOldOrdenes + " since its solicitudes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (responsableNew != null) {
                responsableNew = em.getReference(responsableNew.getClass(), responsableNew.getCodResponsable());
                solicitudes.setResponsable(responsableNew);
            }
            List<Facturas> attachedFacturasListNew = new ArrayList<Facturas>();
            for (Facturas facturasListNewFacturasToAttach : facturasListNew) {
                facturasListNewFacturasToAttach = em.getReference(facturasListNewFacturasToAttach.getClass(), facturasListNewFacturasToAttach.getFacturasPK());
                attachedFacturasListNew.add(facturasListNewFacturasToAttach);
            }
            facturasListNew = attachedFacturasListNew;
            solicitudes.setFacturasList(facturasListNew);
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getOrdenesPK());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            solicitudes.setOrdenesList(ordenesListNew);
            solicitudes = em.merge(solicitudes);
            if (responsableOld != null && !responsableOld.equals(responsableNew)) {
                responsableOld.getSolicitudesList().remove(solicitudes);
                responsableOld = em.merge(responsableOld);
            }
            if (responsableNew != null && !responsableNew.equals(responsableOld)) {
                responsableNew.getSolicitudesList().add(solicitudes);
                responsableNew = em.merge(responsableNew);
            }
            for (Facturas facturasListNewFacturas : facturasListNew) {
                if (!facturasListOld.contains(facturasListNewFacturas)) {
                    Solicitudes oldSolicitudesOfFacturasListNewFacturas = facturasListNewFacturas.getSolicitudes();
                    facturasListNewFacturas.setSolicitudes(solicitudes);
                    facturasListNewFacturas = em.merge(facturasListNewFacturas);
                    if (oldSolicitudesOfFacturasListNewFacturas != null && !oldSolicitudesOfFacturasListNewFacturas.equals(solicitudes)) {
                        oldSolicitudesOfFacturasListNewFacturas.getFacturasList().remove(facturasListNewFacturas);
                        oldSolicitudesOfFacturasListNewFacturas = em.merge(oldSolicitudesOfFacturasListNewFacturas);
                    }
                }
            }
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    Solicitudes oldSolicitudesOfOrdenesListNewOrdenes = ordenesListNewOrdenes.getSolicitudes();
                    ordenesListNewOrdenes.setSolicitudes(solicitudes);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
                    if (oldSolicitudesOfOrdenesListNewOrdenes != null && !oldSolicitudesOfOrdenesListNewOrdenes.equals(solicitudes)) {
                        oldSolicitudesOfOrdenesListNewOrdenes.getOrdenesList().remove(ordenesListNewOrdenes);
                        oldSolicitudesOfOrdenesListNewOrdenes = em.merge(oldSolicitudesOfOrdenesListNewOrdenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = solicitudes.getCodSolicitud();
                if (findSolicitudes(id) == null) {
                    throw new NonexistentEntityException("The solicitudes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitudes solicitudes;
            try {
                solicitudes = em.getReference(Solicitudes.class, id);
                solicitudes.getCodSolicitud();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The solicitudes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Facturas> facturasListOrphanCheck = solicitudes.getFacturasList();
            for (Facturas facturasListOrphanCheckFacturas : facturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Solicitudes (" + solicitudes + ") cannot be destroyed since the Facturas " + facturasListOrphanCheckFacturas + " in its facturasList field has a non-nullable solicitudes field.");
            }
            List<Ordenes> ordenesListOrphanCheck = solicitudes.getOrdenesList();
            for (Ordenes ordenesListOrphanCheckOrdenes : ordenesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Solicitudes (" + solicitudes + ") cannot be destroyed since the Ordenes " + ordenesListOrphanCheckOrdenes + " in its ordenesList field has a non-nullable solicitudes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Responsables responsable = solicitudes.getResponsable();
            if (responsable != null) {
                responsable.getSolicitudesList().remove(solicitudes);
                responsable = em.merge(responsable);
            }
            em.remove(solicitudes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Solicitudes> findSolicitudesEntities() {
        return findSolicitudesEntities(true, -1, -1);
    }

    public List<Solicitudes> findSolicitudesEntities(int maxResults, int firstResult) {
        return findSolicitudesEntities(false, maxResults, firstResult);
    }

    private List<Solicitudes> findSolicitudesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Solicitudes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Solicitudes findSolicitudes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Solicitudes.class, id);
        } finally {
            em.close();
        }
    }

    public int getSolicitudesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Solicitudes> rt = cq.from(Solicitudes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
