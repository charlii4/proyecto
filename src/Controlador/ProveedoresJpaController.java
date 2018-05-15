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
import Entidades.Bienes;
import java.util.ArrayList;
import java.util.List;
import Entidades.Ordenes;
import Entidades.Entradas;
import Entidades.Proveedores;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class ProveedoresJpaController implements Serializable {

    public ProveedoresJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedores proveedores) throws PreexistingEntityException, Exception {
        if (proveedores.getBienesList() == null) {
            proveedores.setBienesList(new ArrayList<Bienes>());
        }
        if (proveedores.getOrdenesList() == null) {
            proveedores.setOrdenesList(new ArrayList<Ordenes>());
        }
        if (proveedores.getEntradasList() == null) {
            proveedores.setEntradasList(new ArrayList<Entradas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Bienes> attachedBienesList = new ArrayList<Bienes>();
            for (Bienes bienesListBienesToAttach : proveedores.getBienesList()) {
                bienesListBienesToAttach = em.getReference(bienesListBienesToAttach.getClass(), bienesListBienesToAttach.getCodItem());
                attachedBienesList.add(bienesListBienesToAttach);
            }
            proveedores.setBienesList(attachedBienesList);
            List<Ordenes> attachedOrdenesList = new ArrayList<Ordenes>();
            for (Ordenes ordenesListOrdenesToAttach : proveedores.getOrdenesList()) {
                ordenesListOrdenesToAttach = em.getReference(ordenesListOrdenesToAttach.getClass(), ordenesListOrdenesToAttach.getOrdenesPK());
                attachedOrdenesList.add(ordenesListOrdenesToAttach);
            }
            proveedores.setOrdenesList(attachedOrdenesList);
            List<Entradas> attachedEntradasList = new ArrayList<Entradas>();
            for (Entradas entradasListEntradasToAttach : proveedores.getEntradasList()) {
                entradasListEntradasToAttach = em.getReference(entradasListEntradasToAttach.getClass(), entradasListEntradasToAttach.getNumEntrada());
                attachedEntradasList.add(entradasListEntradasToAttach);
            }
            proveedores.setEntradasList(attachedEntradasList);
            em.persist(proveedores);
            for (Bienes bienesListBienes : proveedores.getBienesList()) {
                Proveedores oldProveedorOfBienesListBienes = bienesListBienes.getProveedor();
                bienesListBienes.setProveedor(proveedores);
                bienesListBienes = em.merge(bienesListBienes);
                if (oldProveedorOfBienesListBienes != null) {
                    oldProveedorOfBienesListBienes.getBienesList().remove(bienesListBienes);
                    oldProveedorOfBienesListBienes = em.merge(oldProveedorOfBienesListBienes);
                }
            }
            for (Ordenes ordenesListOrdenes : proveedores.getOrdenesList()) {
                Proveedores oldNitOfOrdenesListOrdenes = ordenesListOrdenes.getNit();
                ordenesListOrdenes.setNit(proveedores);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
                if (oldNitOfOrdenesListOrdenes != null) {
                    oldNitOfOrdenesListOrdenes.getOrdenesList().remove(ordenesListOrdenes);
                    oldNitOfOrdenesListOrdenes = em.merge(oldNitOfOrdenesListOrdenes);
                }
            }
            for (Entradas entradasListEntradas : proveedores.getEntradasList()) {
                Proveedores oldProveedorOfEntradasListEntradas = entradasListEntradas.getProveedor();
                entradasListEntradas.setProveedor(proveedores);
                entradasListEntradas = em.merge(entradasListEntradas);
                if (oldProveedorOfEntradasListEntradas != null) {
                    oldProveedorOfEntradasListEntradas.getEntradasList().remove(entradasListEntradas);
                    oldProveedorOfEntradasListEntradas = em.merge(oldProveedorOfEntradasListEntradas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedores(proveedores.getNit()) != null) {
                throw new PreexistingEntityException("Proveedores " + proveedores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedores proveedores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores persistentProveedores = em.find(Proveedores.class, proveedores.getNit());
            List<Bienes> bienesListOld = persistentProveedores.getBienesList();
            List<Bienes> bienesListNew = proveedores.getBienesList();
            List<Ordenes> ordenesListOld = persistentProveedores.getOrdenesList();
            List<Ordenes> ordenesListNew = proveedores.getOrdenesList();
            List<Entradas> entradasListOld = persistentProveedores.getEntradasList();
            List<Entradas> entradasListNew = proveedores.getEntradasList();
            List<String> illegalOrphanMessages = null;
            for (Bienes bienesListOldBienes : bienesListOld) {
                if (!bienesListNew.contains(bienesListOldBienes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bienes " + bienesListOldBienes + " since its proveedor field is not nullable.");
                }
            }
            for (Entradas entradasListOldEntradas : entradasListOld) {
                if (!entradasListNew.contains(entradasListOldEntradas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entradas " + entradasListOldEntradas + " since its proveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Bienes> attachedBienesListNew = new ArrayList<Bienes>();
            for (Bienes bienesListNewBienesToAttach : bienesListNew) {
                bienesListNewBienesToAttach = em.getReference(bienesListNewBienesToAttach.getClass(), bienesListNewBienesToAttach.getCodItem());
                attachedBienesListNew.add(bienesListNewBienesToAttach);
            }
            bienesListNew = attachedBienesListNew;
            proveedores.setBienesList(bienesListNew);
            List<Ordenes> attachedOrdenesListNew = new ArrayList<Ordenes>();
            for (Ordenes ordenesListNewOrdenesToAttach : ordenesListNew) {
                ordenesListNewOrdenesToAttach = em.getReference(ordenesListNewOrdenesToAttach.getClass(), ordenesListNewOrdenesToAttach.getOrdenesPK());
                attachedOrdenesListNew.add(ordenesListNewOrdenesToAttach);
            }
            ordenesListNew = attachedOrdenesListNew;
            proveedores.setOrdenesList(ordenesListNew);
            List<Entradas> attachedEntradasListNew = new ArrayList<Entradas>();
            for (Entradas entradasListNewEntradasToAttach : entradasListNew) {
                entradasListNewEntradasToAttach = em.getReference(entradasListNewEntradasToAttach.getClass(), entradasListNewEntradasToAttach.getNumEntrada());
                attachedEntradasListNew.add(entradasListNewEntradasToAttach);
            }
            entradasListNew = attachedEntradasListNew;
            proveedores.setEntradasList(entradasListNew);
            proveedores = em.merge(proveedores);
            for (Bienes bienesListNewBienes : bienesListNew) {
                if (!bienesListOld.contains(bienesListNewBienes)) {
                    Proveedores oldProveedorOfBienesListNewBienes = bienesListNewBienes.getProveedor();
                    bienesListNewBienes.setProveedor(proveedores);
                    bienesListNewBienes = em.merge(bienesListNewBienes);
                    if (oldProveedorOfBienesListNewBienes != null && !oldProveedorOfBienesListNewBienes.equals(proveedores)) {
                        oldProveedorOfBienesListNewBienes.getBienesList().remove(bienesListNewBienes);
                        oldProveedorOfBienesListNewBienes = em.merge(oldProveedorOfBienesListNewBienes);
                    }
                }
            }
            for (Ordenes ordenesListOldOrdenes : ordenesListOld) {
                if (!ordenesListNew.contains(ordenesListOldOrdenes)) {
                    ordenesListOldOrdenes.setNit(null);
                    ordenesListOldOrdenes = em.merge(ordenesListOldOrdenes);
                }
            }
            for (Ordenes ordenesListNewOrdenes : ordenesListNew) {
                if (!ordenesListOld.contains(ordenesListNewOrdenes)) {
                    Proveedores oldNitOfOrdenesListNewOrdenes = ordenesListNewOrdenes.getNit();
                    ordenesListNewOrdenes.setNit(proveedores);
                    ordenesListNewOrdenes = em.merge(ordenesListNewOrdenes);
                    if (oldNitOfOrdenesListNewOrdenes != null && !oldNitOfOrdenesListNewOrdenes.equals(proveedores)) {
                        oldNitOfOrdenesListNewOrdenes.getOrdenesList().remove(ordenesListNewOrdenes);
                        oldNitOfOrdenesListNewOrdenes = em.merge(oldNitOfOrdenesListNewOrdenes);
                    }
                }
            }
            for (Entradas entradasListNewEntradas : entradasListNew) {
                if (!entradasListOld.contains(entradasListNewEntradas)) {
                    Proveedores oldProveedorOfEntradasListNewEntradas = entradasListNewEntradas.getProveedor();
                    entradasListNewEntradas.setProveedor(proveedores);
                    entradasListNewEntradas = em.merge(entradasListNewEntradas);
                    if (oldProveedorOfEntradasListNewEntradas != null && !oldProveedorOfEntradasListNewEntradas.equals(proveedores)) {
                        oldProveedorOfEntradasListNewEntradas.getEntradasList().remove(entradasListNewEntradas);
                        oldProveedorOfEntradasListNewEntradas = em.merge(oldProveedorOfEntradasListNewEntradas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedores.getNit();
                if (findProveedores(id) == null) {
                    throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.");
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
            Proveedores proveedores;
            try {
                proveedores = em.getReference(Proveedores.class, id);
                proveedores.getNit();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Bienes> bienesListOrphanCheck = proveedores.getBienesList();
            for (Bienes bienesListOrphanCheckBienes : bienesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedores (" + proveedores + ") cannot be destroyed since the Bienes " + bienesListOrphanCheckBienes + " in its bienesList field has a non-nullable proveedor field.");
            }
            List<Entradas> entradasListOrphanCheck = proveedores.getEntradasList();
            for (Entradas entradasListOrphanCheckEntradas : entradasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedores (" + proveedores + ") cannot be destroyed since the Entradas " + entradasListOrphanCheckEntradas + " in its entradasList field has a non-nullable proveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Ordenes> ordenesList = proveedores.getOrdenesList();
            for (Ordenes ordenesListOrdenes : ordenesList) {
                ordenesListOrdenes.setNit(null);
                ordenesListOrdenes = em.merge(ordenesListOrdenes);
            }
            em.remove(proveedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedores> findProveedoresEntities() {
        return findProveedoresEntities(true, -1, -1);
    }

    public List<Proveedores> findProveedoresEntities(int maxResults, int firstResult) {
        return findProveedoresEntities(false, maxResults, firstResult);
    }

    private List<Proveedores> findProveedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedores.class));
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

    public Proveedores findProveedores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedores.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedores> rt = cq.from(Proveedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
