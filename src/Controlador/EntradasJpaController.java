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
import Entidades.Almacenes;
import Entidades.Bienes;
import Entidades.Entradas;
import Entidades.Proveedores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class EntradasJpaController implements Serializable {

    public EntradasJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entradas entradas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacenes almacen = entradas.getAlmacen();
            if (almacen != null) {
                almacen = em.getReference(almacen.getClass(), almacen.getCodAlmacen());
                entradas.setAlmacen(almacen);
            }
            Bienes item = entradas.getItem();
            if (item != null) {
                item = em.getReference(item.getClass(), item.getCodItem());
                entradas.setItem(item);
            }
            Proveedores proveedor = entradas.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getNit());
                entradas.setProveedor(proveedor);
            }
            em.persist(entradas);
            if (almacen != null) {
                almacen.getEntradasList().add(entradas);
                almacen = em.merge(almacen);
            }
            if (item != null) {
                item.getEntradasList().add(entradas);
                item = em.merge(item);
            }
            if (proveedor != null) {
                proveedor.getEntradasList().add(entradas);
                proveedor = em.merge(proveedor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEntradas(entradas.getNumEntrada()) != null) {
                throw new PreexistingEntityException("Entradas " + entradas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entradas entradas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entradas persistentEntradas = em.find(Entradas.class, entradas.getNumEntrada());
            Almacenes almacenOld = persistentEntradas.getAlmacen();
            Almacenes almacenNew = entradas.getAlmacen();
            Bienes itemOld = persistentEntradas.getItem();
            Bienes itemNew = entradas.getItem();
            Proveedores proveedorOld = persistentEntradas.getProveedor();
            Proveedores proveedorNew = entradas.getProveedor();
            if (almacenNew != null) {
                almacenNew = em.getReference(almacenNew.getClass(), almacenNew.getCodAlmacen());
                entradas.setAlmacen(almacenNew);
            }
            if (itemNew != null) {
                itemNew = em.getReference(itemNew.getClass(), itemNew.getCodItem());
                entradas.setItem(itemNew);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getNit());
                entradas.setProveedor(proveedorNew);
            }
            entradas = em.merge(entradas);
            if (almacenOld != null && !almacenOld.equals(almacenNew)) {
                almacenOld.getEntradasList().remove(entradas);
                almacenOld = em.merge(almacenOld);
            }
            if (almacenNew != null && !almacenNew.equals(almacenOld)) {
                almacenNew.getEntradasList().add(entradas);
                almacenNew = em.merge(almacenNew);
            }
            if (itemOld != null && !itemOld.equals(itemNew)) {
                itemOld.getEntradasList().remove(entradas);
                itemOld = em.merge(itemOld);
            }
            if (itemNew != null && !itemNew.equals(itemOld)) {
                itemNew.getEntradasList().add(entradas);
                itemNew = em.merge(itemNew);
            }
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getEntradasList().remove(entradas);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getEntradasList().add(entradas);
                proveedorNew = em.merge(proveedorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entradas.getNumEntrada();
                if (findEntradas(id) == null) {
                    throw new NonexistentEntityException("The entradas with id " + id + " no longer exists.");
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
            Entradas entradas;
            try {
                entradas = em.getReference(Entradas.class, id);
                entradas.getNumEntrada();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entradas with id " + id + " no longer exists.", enfe);
            }
            Almacenes almacen = entradas.getAlmacen();
            if (almacen != null) {
                almacen.getEntradasList().remove(entradas);
                almacen = em.merge(almacen);
            }
            Bienes item = entradas.getItem();
            if (item != null) {
                item.getEntradasList().remove(entradas);
                item = em.merge(item);
            }
            Proveedores proveedor = entradas.getProveedor();
            if (proveedor != null) {
                proveedor.getEntradasList().remove(entradas);
                proveedor = em.merge(proveedor);
            }
            em.remove(entradas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entradas> findEntradasEntities() {
        return findEntradasEntities(true, -1, -1);
    }

    public List<Entradas> findEntradasEntities(int maxResults, int firstResult) {
        return findEntradasEntities(false, maxResults, firstResult);
    }

    private List<Entradas> findEntradasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entradas.class));
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

    public Entradas findEntradas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entradas.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntradasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entradas> rt = cq.from(Entradas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
