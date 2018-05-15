/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Entidades.Bienes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Proveedores;
import Entidades.Facturas;
import java.util.ArrayList;
import java.util.List;
import Entidades.Entradas;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class BienesJpaController implements Serializable {

    public BienesJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bienes bienes) throws PreexistingEntityException, Exception {
        if (bienes.getFacturasList() == null) {
            bienes.setFacturasList(new ArrayList<Facturas>());
        }
        if (bienes.getEntradasList() == null) {
            bienes.setEntradasList(new ArrayList<Entradas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores proveedor = bienes.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getNit());
                bienes.setProveedor(proveedor);
            }
            List<Facturas> attachedFacturasList = new ArrayList<Facturas>();
            for (Facturas facturasListFacturasToAttach : bienes.getFacturasList()) {
                facturasListFacturasToAttach = em.getReference(facturasListFacturasToAttach.getClass(), facturasListFacturasToAttach.getFacturasPK());
                attachedFacturasList.add(facturasListFacturasToAttach);
            }
            bienes.setFacturasList(attachedFacturasList);
            List<Entradas> attachedEntradasList = new ArrayList<Entradas>();
            for (Entradas entradasListEntradasToAttach : bienes.getEntradasList()) {
                entradasListEntradasToAttach = em.getReference(entradasListEntradasToAttach.getClass(), entradasListEntradasToAttach.getNumEntrada());
                attachedEntradasList.add(entradasListEntradasToAttach);
            }
            bienes.setEntradasList(attachedEntradasList);
            em.persist(bienes);
            if (proveedor != null) {
                proveedor.getBienesList().add(bienes);
                proveedor = em.merge(proveedor);
            }
            for (Facturas facturasListFacturas : bienes.getFacturasList()) {
                Bienes oldBienesOfFacturasListFacturas = facturasListFacturas.getBienes();
                facturasListFacturas.setBienes(bienes);
                facturasListFacturas = em.merge(facturasListFacturas);
                if (oldBienesOfFacturasListFacturas != null) {
                    oldBienesOfFacturasListFacturas.getFacturasList().remove(facturasListFacturas);
                    oldBienesOfFacturasListFacturas = em.merge(oldBienesOfFacturasListFacturas);
                }
            }
            for (Entradas entradasListEntradas : bienes.getEntradasList()) {
                Bienes oldItemOfEntradasListEntradas = entradasListEntradas.getItem();
                entradasListEntradas.setItem(bienes);
                entradasListEntradas = em.merge(entradasListEntradas);
                if (oldItemOfEntradasListEntradas != null) {
                    oldItemOfEntradasListEntradas.getEntradasList().remove(entradasListEntradas);
                    oldItemOfEntradasListEntradas = em.merge(oldItemOfEntradasListEntradas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBienes(bienes.getCodItem()) != null) {
                throw new PreexistingEntityException("Bienes " + bienes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bienes bienes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bienes persistentBienes = em.find(Bienes.class, bienes.getCodItem());
            Proveedores proveedorOld = persistentBienes.getProveedor();
            Proveedores proveedorNew = bienes.getProveedor();
            List<Facturas> facturasListOld = persistentBienes.getFacturasList();
            List<Facturas> facturasListNew = bienes.getFacturasList();
            List<Entradas> entradasListOld = persistentBienes.getEntradasList();
            List<Entradas> entradasListNew = bienes.getEntradasList();
            List<String> illegalOrphanMessages = null;
            for (Facturas facturasListOldFacturas : facturasListOld) {
                if (!facturasListNew.contains(facturasListOldFacturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Facturas " + facturasListOldFacturas + " since its bienes field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getNit());
                bienes.setProveedor(proveedorNew);
            }
            List<Facturas> attachedFacturasListNew = new ArrayList<Facturas>();
            for (Facturas facturasListNewFacturasToAttach : facturasListNew) {
                facturasListNewFacturasToAttach = em.getReference(facturasListNewFacturasToAttach.getClass(), facturasListNewFacturasToAttach.getFacturasPK());
                attachedFacturasListNew.add(facturasListNewFacturasToAttach);
            }
            facturasListNew = attachedFacturasListNew;
            bienes.setFacturasList(facturasListNew);
            List<Entradas> attachedEntradasListNew = new ArrayList<Entradas>();
            for (Entradas entradasListNewEntradasToAttach : entradasListNew) {
                entradasListNewEntradasToAttach = em.getReference(entradasListNewEntradasToAttach.getClass(), entradasListNewEntradasToAttach.getNumEntrada());
                attachedEntradasListNew.add(entradasListNewEntradasToAttach);
            }
            entradasListNew = attachedEntradasListNew;
            bienes.setEntradasList(entradasListNew);
            bienes = em.merge(bienes);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getBienesList().remove(bienes);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getBienesList().add(bienes);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Facturas facturasListNewFacturas : facturasListNew) {
                if (!facturasListOld.contains(facturasListNewFacturas)) {
                    Bienes oldBienesOfFacturasListNewFacturas = facturasListNewFacturas.getBienes();
                    facturasListNewFacturas.setBienes(bienes);
                    facturasListNewFacturas = em.merge(facturasListNewFacturas);
                    if (oldBienesOfFacturasListNewFacturas != null && !oldBienesOfFacturasListNewFacturas.equals(bienes)) {
                        oldBienesOfFacturasListNewFacturas.getFacturasList().remove(facturasListNewFacturas);
                        oldBienesOfFacturasListNewFacturas = em.merge(oldBienesOfFacturasListNewFacturas);
                    }
                }
            }
            for (Entradas entradasListOldEntradas : entradasListOld) {
                if (!entradasListNew.contains(entradasListOldEntradas)) {
                    entradasListOldEntradas.setItem(null);
                    entradasListOldEntradas = em.merge(entradasListOldEntradas);
                }
            }
            for (Entradas entradasListNewEntradas : entradasListNew) {
                if (!entradasListOld.contains(entradasListNewEntradas)) {
                    Bienes oldItemOfEntradasListNewEntradas = entradasListNewEntradas.getItem();
                    entradasListNewEntradas.setItem(bienes);
                    entradasListNewEntradas = em.merge(entradasListNewEntradas);
                    if (oldItemOfEntradasListNewEntradas != null && !oldItemOfEntradasListNewEntradas.equals(bienes)) {
                        oldItemOfEntradasListNewEntradas.getEntradasList().remove(entradasListNewEntradas);
                        oldItemOfEntradasListNewEntradas = em.merge(oldItemOfEntradasListNewEntradas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bienes.getCodItem();
                if (findBienes(id) == null) {
                    throw new NonexistentEntityException("The bienes with id " + id + " no longer exists.");
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
            Bienes bienes;
            try {
                bienes = em.getReference(Bienes.class, id);
                bienes.getCodItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bienes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Facturas> facturasListOrphanCheck = bienes.getFacturasList();
            for (Facturas facturasListOrphanCheckFacturas : facturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Bienes (" + bienes + ") cannot be destroyed since the Facturas " + facturasListOrphanCheckFacturas + " in its facturasList field has a non-nullable bienes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedores proveedor = bienes.getProveedor();
            if (proveedor != null) {
                proveedor.getBienesList().remove(bienes);
                proveedor = em.merge(proveedor);
            }
            List<Entradas> entradasList = bienes.getEntradasList();
            for (Entradas entradasListEntradas : entradasList) {
                entradasListEntradas.setItem(null);
                entradasListEntradas = em.merge(entradasListEntradas);
            }
            em.remove(bienes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bienes> findBienesEntities() {
        return findBienesEntities(true, -1, -1);
    }

    public List<Bienes> findBienesEntities(int maxResults, int firstResult) {
        return findBienesEntities(false, maxResults, firstResult);
    }

    private List<Bienes> findBienesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bienes.class));
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

    public Bienes findBienes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bienes.class, id);
        } finally {
            em.close();
        }
    }

    public int getBienesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bienes> rt = cq.from(Bienes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
