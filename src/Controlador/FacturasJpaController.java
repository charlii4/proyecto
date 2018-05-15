/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Bienes;
import Entidades.Facturas;
import Entidades.FacturasPK;
import Entidades.Solicitudes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class FacturasJpaController implements Serializable {

    public FacturasJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturas facturas) throws PreexistingEntityException, Exception {
        if (facturas.getFacturasPK() == null) {
            facturas.setFacturasPK(new FacturasPK());
        }
        facturas.getFacturasPK().setItem(facturas.getBienes().getCodItem());
        facturas.getFacturasPK().setSolicitud(facturas.getSolicitudes().getCodSolicitud());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bienes bienes = facturas.getBienes();
            if (bienes != null) {
                bienes = em.getReference(bienes.getClass(), bienes.getCodItem());
                facturas.setBienes(bienes);
            }
            Solicitudes solicitudes = facturas.getSolicitudes();
            if (solicitudes != null) {
                solicitudes = em.getReference(solicitudes.getClass(), solicitudes.getCodSolicitud());
                facturas.setSolicitudes(solicitudes);
            }
            em.persist(facturas);
            if (bienes != null) {
                bienes.getFacturasList().add(facturas);
                bienes = em.merge(bienes);
            }
            if (solicitudes != null) {
                solicitudes.getFacturasList().add(facturas);
                solicitudes = em.merge(solicitudes);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFacturas(facturas.getFacturasPK()) != null) {
                throw new PreexistingEntityException("Facturas " + facturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facturas facturas) throws NonexistentEntityException, Exception {
        facturas.getFacturasPK().setItem(facturas.getBienes().getCodItem());
        facturas.getFacturasPK().setSolicitud(facturas.getSolicitudes().getCodSolicitud());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas persistentFacturas = em.find(Facturas.class, facturas.getFacturasPK());
            Bienes bienesOld = persistentFacturas.getBienes();
            Bienes bienesNew = facturas.getBienes();
            Solicitudes solicitudesOld = persistentFacturas.getSolicitudes();
            Solicitudes solicitudesNew = facturas.getSolicitudes();
            if (bienesNew != null) {
                bienesNew = em.getReference(bienesNew.getClass(), bienesNew.getCodItem());
                facturas.setBienes(bienesNew);
            }
            if (solicitudesNew != null) {
                solicitudesNew = em.getReference(solicitudesNew.getClass(), solicitudesNew.getCodSolicitud());
                facturas.setSolicitudes(solicitudesNew);
            }
            facturas = em.merge(facturas);
            if (bienesOld != null && !bienesOld.equals(bienesNew)) {
                bienesOld.getFacturasList().remove(facturas);
                bienesOld = em.merge(bienesOld);
            }
            if (bienesNew != null && !bienesNew.equals(bienesOld)) {
                bienesNew.getFacturasList().add(facturas);
                bienesNew = em.merge(bienesNew);
            }
            if (solicitudesOld != null && !solicitudesOld.equals(solicitudesNew)) {
                solicitudesOld.getFacturasList().remove(facturas);
                solicitudesOld = em.merge(solicitudesOld);
            }
            if (solicitudesNew != null && !solicitudesNew.equals(solicitudesOld)) {
                solicitudesNew.getFacturasList().add(facturas);
                solicitudesNew = em.merge(solicitudesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FacturasPK id = facturas.getFacturasPK();
                if (findFacturas(id) == null) {
                    throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(FacturasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas facturas;
            try {
                facturas = em.getReference(Facturas.class, id);
                facturas.getFacturasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.", enfe);
            }
            Bienes bienes = facturas.getBienes();
            if (bienes != null) {
                bienes.getFacturasList().remove(facturas);
                bienes = em.merge(bienes);
            }
            Solicitudes solicitudes = facturas.getSolicitudes();
            if (solicitudes != null) {
                solicitudes.getFacturasList().remove(facturas);
                solicitudes = em.merge(solicitudes);
            }
            em.remove(facturas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facturas> findFacturasEntities() {
        return findFacturasEntities(true, -1, -1);
    }

    public List<Facturas> findFacturasEntities(int maxResults, int firstResult) {
        return findFacturasEntities(false, maxResults, firstResult);
    }

    private List<Facturas> findFacturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturas.class));
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

    public Facturas findFacturas(FacturasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturas.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturas> rt = cq.from(Facturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
