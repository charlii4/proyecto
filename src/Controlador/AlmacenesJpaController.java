/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Entidades.Almacenes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Entradas;
import java.util.ArrayList;
import java.util.List;
import Entidades.Salidas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class AlmacenesJpaController implements Serializable {

    public AlmacenesJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Almacenes almacenes) throws PreexistingEntityException, Exception {
        if (almacenes.getEntradasList() == null) {
            almacenes.setEntradasList(new ArrayList<Entradas>());
        }
        if (almacenes.getSalidasList() == null) {
            almacenes.setSalidasList(new ArrayList<Salidas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Entradas> attachedEntradasList = new ArrayList<Entradas>();
            for (Entradas entradasListEntradasToAttach : almacenes.getEntradasList()) {
                entradasListEntradasToAttach = em.getReference(entradasListEntradasToAttach.getClass(), entradasListEntradasToAttach.getNumEntrada());
                attachedEntradasList.add(entradasListEntradasToAttach);
            }
            almacenes.setEntradasList(attachedEntradasList);
            List<Salidas> attachedSalidasList = new ArrayList<Salidas>();
            for (Salidas salidasListSalidasToAttach : almacenes.getSalidasList()) {
                salidasListSalidasToAttach = em.getReference(salidasListSalidasToAttach.getClass(), salidasListSalidasToAttach.getNumSalida());
                attachedSalidasList.add(salidasListSalidasToAttach);
            }
            almacenes.setSalidasList(attachedSalidasList);
            em.persist(almacenes);
            for (Entradas entradasListEntradas : almacenes.getEntradasList()) {
                Almacenes oldAlmacenOfEntradasListEntradas = entradasListEntradas.getAlmacen();
                entradasListEntradas.setAlmacen(almacenes);
                entradasListEntradas = em.merge(entradasListEntradas);
                if (oldAlmacenOfEntradasListEntradas != null) {
                    oldAlmacenOfEntradasListEntradas.getEntradasList().remove(entradasListEntradas);
                    oldAlmacenOfEntradasListEntradas = em.merge(oldAlmacenOfEntradasListEntradas);
                }
            }
            for (Salidas salidasListSalidas : almacenes.getSalidasList()) {
                Almacenes oldAlmacenOfSalidasListSalidas = salidasListSalidas.getAlmacen();
                salidasListSalidas.setAlmacen(almacenes);
                salidasListSalidas = em.merge(salidasListSalidas);
                if (oldAlmacenOfSalidasListSalidas != null) {
                    oldAlmacenOfSalidasListSalidas.getSalidasList().remove(salidasListSalidas);
                    oldAlmacenOfSalidasListSalidas = em.merge(oldAlmacenOfSalidasListSalidas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlmacenes(almacenes.getCodAlmacen()) != null) {
                throw new PreexistingEntityException("Almacenes " + almacenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Almacenes almacenes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacenes persistentAlmacenes = em.find(Almacenes.class, almacenes.getCodAlmacen());
            List<Entradas> entradasListOld = persistentAlmacenes.getEntradasList();
            List<Entradas> entradasListNew = almacenes.getEntradasList();
            List<Salidas> salidasListOld = persistentAlmacenes.getSalidasList();
            List<Salidas> salidasListNew = almacenes.getSalidasList();
            List<Entradas> attachedEntradasListNew = new ArrayList<Entradas>();
            for (Entradas entradasListNewEntradasToAttach : entradasListNew) {
                entradasListNewEntradasToAttach = em.getReference(entradasListNewEntradasToAttach.getClass(), entradasListNewEntradasToAttach.getNumEntrada());
                attachedEntradasListNew.add(entradasListNewEntradasToAttach);
            }
            entradasListNew = attachedEntradasListNew;
            almacenes.setEntradasList(entradasListNew);
            List<Salidas> attachedSalidasListNew = new ArrayList<Salidas>();
            for (Salidas salidasListNewSalidasToAttach : salidasListNew) {
                salidasListNewSalidasToAttach = em.getReference(salidasListNewSalidasToAttach.getClass(), salidasListNewSalidasToAttach.getNumSalida());
                attachedSalidasListNew.add(salidasListNewSalidasToAttach);
            }
            salidasListNew = attachedSalidasListNew;
            almacenes.setSalidasList(salidasListNew);
            almacenes = em.merge(almacenes);
            for (Entradas entradasListOldEntradas : entradasListOld) {
                if (!entradasListNew.contains(entradasListOldEntradas)) {
                    entradasListOldEntradas.setAlmacen(null);
                    entradasListOldEntradas = em.merge(entradasListOldEntradas);
                }
            }
            for (Entradas entradasListNewEntradas : entradasListNew) {
                if (!entradasListOld.contains(entradasListNewEntradas)) {
                    Almacenes oldAlmacenOfEntradasListNewEntradas = entradasListNewEntradas.getAlmacen();
                    entradasListNewEntradas.setAlmacen(almacenes);
                    entradasListNewEntradas = em.merge(entradasListNewEntradas);
                    if (oldAlmacenOfEntradasListNewEntradas != null && !oldAlmacenOfEntradasListNewEntradas.equals(almacenes)) {
                        oldAlmacenOfEntradasListNewEntradas.getEntradasList().remove(entradasListNewEntradas);
                        oldAlmacenOfEntradasListNewEntradas = em.merge(oldAlmacenOfEntradasListNewEntradas);
                    }
                }
            }
            for (Salidas salidasListOldSalidas : salidasListOld) {
                if (!salidasListNew.contains(salidasListOldSalidas)) {
                    salidasListOldSalidas.setAlmacen(null);
                    salidasListOldSalidas = em.merge(salidasListOldSalidas);
                }
            }
            for (Salidas salidasListNewSalidas : salidasListNew) {
                if (!salidasListOld.contains(salidasListNewSalidas)) {
                    Almacenes oldAlmacenOfSalidasListNewSalidas = salidasListNewSalidas.getAlmacen();
                    salidasListNewSalidas.setAlmacen(almacenes);
                    salidasListNewSalidas = em.merge(salidasListNewSalidas);
                    if (oldAlmacenOfSalidasListNewSalidas != null && !oldAlmacenOfSalidasListNewSalidas.equals(almacenes)) {
                        oldAlmacenOfSalidasListNewSalidas.getSalidasList().remove(salidasListNewSalidas);
                        oldAlmacenOfSalidasListNewSalidas = em.merge(oldAlmacenOfSalidasListNewSalidas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = almacenes.getCodAlmacen();
                if (findAlmacenes(id) == null) {
                    throw new NonexistentEntityException("The almacenes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacenes almacenes;
            try {
                almacenes = em.getReference(Almacenes.class, id);
                almacenes.getCodAlmacen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacenes with id " + id + " no longer exists.", enfe);
            }
            List<Entradas> entradasList = almacenes.getEntradasList();
            for (Entradas entradasListEntradas : entradasList) {
                entradasListEntradas.setAlmacen(null);
                entradasListEntradas = em.merge(entradasListEntradas);
            }
            List<Salidas> salidasList = almacenes.getSalidasList();
            for (Salidas salidasListSalidas : salidasList) {
                salidasListSalidas.setAlmacen(null);
                salidasListSalidas = em.merge(salidasListSalidas);
            }
            em.remove(almacenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Almacenes> findAlmacenesEntities() {
        return findAlmacenesEntities(true, -1, -1);
    }

    public List<Almacenes> findAlmacenesEntities(int maxResults, int firstResult) {
        return findAlmacenesEntities(false, maxResults, firstResult);
    }

    private List<Almacenes> findAlmacenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacenes.class));
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

    public Almacenes findAlmacenes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlmacenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Almacenes> rt = cq.from(Almacenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
