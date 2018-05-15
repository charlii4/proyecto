/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Entidades.Ordenes;
import Entidades.OrdenesPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Proveedores;
import Entidades.Solicitudes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class OrdenesJpaController implements Serializable {

    public OrdenesJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ordenes ordenes) throws PreexistingEntityException, Exception {
        if (ordenes.getOrdenesPK() == null) {
            ordenes.setOrdenesPK(new OrdenesPK());
        }
        ordenes.getOrdenesPK().setSolicitud(ordenes.getSolicitudes().getCodSolicitud());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores nit = ordenes.getNit();
            if (nit != null) {
                nit = em.getReference(nit.getClass(), nit.getNit());
                ordenes.setNit(nit);
            }
            Solicitudes solicitudes = ordenes.getSolicitudes();
            if (solicitudes != null) {
                solicitudes = em.getReference(solicitudes.getClass(), solicitudes.getCodSolicitud());
                ordenes.setSolicitudes(solicitudes);
            }
            em.persist(ordenes);
            if (nit != null) {
                nit.getOrdenesList().add(ordenes);
                nit = em.merge(nit);
            }
            if (solicitudes != null) {
                solicitudes.getOrdenesList().add(ordenes);
                solicitudes = em.merge(solicitudes);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrdenes(ordenes.getOrdenesPK()) != null) {
                throw new PreexistingEntityException("Ordenes " + ordenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ordenes ordenes) throws NonexistentEntityException, Exception {
        ordenes.getOrdenesPK().setSolicitud(ordenes.getSolicitudes().getCodSolicitud());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ordenes persistentOrdenes = em.find(Ordenes.class, ordenes.getOrdenesPK());
            Proveedores nitOld = persistentOrdenes.getNit();
            Proveedores nitNew = ordenes.getNit();
            Solicitudes solicitudesOld = persistentOrdenes.getSolicitudes();
            Solicitudes solicitudesNew = ordenes.getSolicitudes();
            if (nitNew != null) {
                nitNew = em.getReference(nitNew.getClass(), nitNew.getNit());
                ordenes.setNit(nitNew);
            }
            if (solicitudesNew != null) {
                solicitudesNew = em.getReference(solicitudesNew.getClass(), solicitudesNew.getCodSolicitud());
                ordenes.setSolicitudes(solicitudesNew);
            }
            ordenes = em.merge(ordenes);
            if (nitOld != null && !nitOld.equals(nitNew)) {
                nitOld.getOrdenesList().remove(ordenes);
                nitOld = em.merge(nitOld);
            }
            if (nitNew != null && !nitNew.equals(nitOld)) {
                nitNew.getOrdenesList().add(ordenes);
                nitNew = em.merge(nitNew);
            }
            if (solicitudesOld != null && !solicitudesOld.equals(solicitudesNew)) {
                solicitudesOld.getOrdenesList().remove(ordenes);
                solicitudesOld = em.merge(solicitudesOld);
            }
            if (solicitudesNew != null && !solicitudesNew.equals(solicitudesOld)) {
                solicitudesNew.getOrdenesList().add(ordenes);
                solicitudesNew = em.merge(solicitudesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OrdenesPK id = ordenes.getOrdenesPK();
                if (findOrdenes(id) == null) {
                    throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OrdenesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ordenes ordenes;
            try {
                ordenes = em.getReference(Ordenes.class, id);
                ordenes.getOrdenesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenes with id " + id + " no longer exists.", enfe);
            }
            Proveedores nit = ordenes.getNit();
            if (nit != null) {
                nit.getOrdenesList().remove(ordenes);
                nit = em.merge(nit);
            }
            Solicitudes solicitudes = ordenes.getSolicitudes();
            if (solicitudes != null) {
                solicitudes.getOrdenesList().remove(ordenes);
                solicitudes = em.merge(solicitudes);
            }
            em.remove(ordenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ordenes> findOrdenesEntities() {
        return findOrdenesEntities(true, -1, -1);
    }

    public List<Ordenes> findOrdenesEntities(int maxResults, int firstResult) {
        return findOrdenesEntities(false, maxResults, firstResult);
    }

    private List<Ordenes> findOrdenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ordenes.class));
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

    public Ordenes findOrdenes(OrdenesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ordenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ordenes> rt = cq.from(Ordenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
