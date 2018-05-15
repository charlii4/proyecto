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
import Entidades.JefesArea;
import Entidades.Empleados;
import Entidades.Responsables;
import java.util.ArrayList;
import java.util.List;
import Entidades.Solicitudes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author charliVB
 */
public class ResponsablesJpaController implements Serializable {

    public ResponsablesJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Responsables responsables) throws PreexistingEntityException, Exception {
        if (responsables.getEmpleadosList() == null) {
            responsables.setEmpleadosList(new ArrayList<Empleados>());
        }
        if (responsables.getSolicitudesList() == null) {
            responsables.setSolicitudesList(new ArrayList<Solicitudes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JefesArea jefe = responsables.getJefe();
            if (jefe != null) {
                jefe = em.getReference(jefe.getClass(), jefe.getCodJefe());
                responsables.setJefe(jefe);
            }
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : responsables.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            responsables.setEmpleadosList(attachedEmpleadosList);
            List<Solicitudes> attachedSolicitudesList = new ArrayList<Solicitudes>();
            for (Solicitudes solicitudesListSolicitudesToAttach : responsables.getSolicitudesList()) {
                solicitudesListSolicitudesToAttach = em.getReference(solicitudesListSolicitudesToAttach.getClass(), solicitudesListSolicitudesToAttach.getCodSolicitud());
                attachedSolicitudesList.add(solicitudesListSolicitudesToAttach);
            }
            responsables.setSolicitudesList(attachedSolicitudesList);
            em.persist(responsables);
            if (jefe != null) {
                jefe.getResponsablesList().add(responsables);
                jefe = em.merge(jefe);
            }
            for (Empleados empleadosListEmpleados : responsables.getEmpleadosList()) {
                Responsables oldResponsableOfEmpleadosListEmpleados = empleadosListEmpleados.getResponsable();
                empleadosListEmpleados.setResponsable(responsables);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldResponsableOfEmpleadosListEmpleados != null) {
                    oldResponsableOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldResponsableOfEmpleadosListEmpleados = em.merge(oldResponsableOfEmpleadosListEmpleados);
                }
            }
            for (Solicitudes solicitudesListSolicitudes : responsables.getSolicitudesList()) {
                Responsables oldResponsableOfSolicitudesListSolicitudes = solicitudesListSolicitudes.getResponsable();
                solicitudesListSolicitudes.setResponsable(responsables);
                solicitudesListSolicitudes = em.merge(solicitudesListSolicitudes);
                if (oldResponsableOfSolicitudesListSolicitudes != null) {
                    oldResponsableOfSolicitudesListSolicitudes.getSolicitudesList().remove(solicitudesListSolicitudes);
                    oldResponsableOfSolicitudesListSolicitudes = em.merge(oldResponsableOfSolicitudesListSolicitudes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findResponsables(responsables.getCodResponsable()) != null) {
                throw new PreexistingEntityException("Responsables " + responsables + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Responsables responsables) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Responsables persistentResponsables = em.find(Responsables.class, responsables.getCodResponsable());
            JefesArea jefeOld = persistentResponsables.getJefe();
            JefesArea jefeNew = responsables.getJefe();
            List<Empleados> empleadosListOld = persistentResponsables.getEmpleadosList();
            List<Empleados> empleadosListNew = responsables.getEmpleadosList();
            List<Solicitudes> solicitudesListOld = persistentResponsables.getSolicitudesList();
            List<Solicitudes> solicitudesListNew = responsables.getSolicitudesList();
            if (jefeNew != null) {
                jefeNew = em.getReference(jefeNew.getClass(), jefeNew.getCodJefe());
                responsables.setJefe(jefeNew);
            }
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            responsables.setEmpleadosList(empleadosListNew);
            List<Solicitudes> attachedSolicitudesListNew = new ArrayList<Solicitudes>();
            for (Solicitudes solicitudesListNewSolicitudesToAttach : solicitudesListNew) {
                solicitudesListNewSolicitudesToAttach = em.getReference(solicitudesListNewSolicitudesToAttach.getClass(), solicitudesListNewSolicitudesToAttach.getCodSolicitud());
                attachedSolicitudesListNew.add(solicitudesListNewSolicitudesToAttach);
            }
            solicitudesListNew = attachedSolicitudesListNew;
            responsables.setSolicitudesList(solicitudesListNew);
            responsables = em.merge(responsables);
            if (jefeOld != null && !jefeOld.equals(jefeNew)) {
                jefeOld.getResponsablesList().remove(responsables);
                jefeOld = em.merge(jefeOld);
            }
            if (jefeNew != null && !jefeNew.equals(jefeOld)) {
                jefeNew.getResponsablesList().add(responsables);
                jefeNew = em.merge(jefeNew);
            }
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    empleadosListOldEmpleados.setResponsable(null);
                    empleadosListOldEmpleados = em.merge(empleadosListOldEmpleados);
                }
            }
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Responsables oldResponsableOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getResponsable();
                    empleadosListNewEmpleados.setResponsable(responsables);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldResponsableOfEmpleadosListNewEmpleados != null && !oldResponsableOfEmpleadosListNewEmpleados.equals(responsables)) {
                        oldResponsableOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldResponsableOfEmpleadosListNewEmpleados = em.merge(oldResponsableOfEmpleadosListNewEmpleados);
                    }
                }
            }
            for (Solicitudes solicitudesListOldSolicitudes : solicitudesListOld) {
                if (!solicitudesListNew.contains(solicitudesListOldSolicitudes)) {
                    solicitudesListOldSolicitudes.setResponsable(null);
                    solicitudesListOldSolicitudes = em.merge(solicitudesListOldSolicitudes);
                }
            }
            for (Solicitudes solicitudesListNewSolicitudes : solicitudesListNew) {
                if (!solicitudesListOld.contains(solicitudesListNewSolicitudes)) {
                    Responsables oldResponsableOfSolicitudesListNewSolicitudes = solicitudesListNewSolicitudes.getResponsable();
                    solicitudesListNewSolicitudes.setResponsable(responsables);
                    solicitudesListNewSolicitudes = em.merge(solicitudesListNewSolicitudes);
                    if (oldResponsableOfSolicitudesListNewSolicitudes != null && !oldResponsableOfSolicitudesListNewSolicitudes.equals(responsables)) {
                        oldResponsableOfSolicitudesListNewSolicitudes.getSolicitudesList().remove(solicitudesListNewSolicitudes);
                        oldResponsableOfSolicitudesListNewSolicitudes = em.merge(oldResponsableOfSolicitudesListNewSolicitudes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = responsables.getCodResponsable();
                if (findResponsables(id) == null) {
                    throw new NonexistentEntityException("The responsables with id " + id + " no longer exists.");
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
            Responsables responsables;
            try {
                responsables = em.getReference(Responsables.class, id);
                responsables.getCodResponsable();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The responsables with id " + id + " no longer exists.", enfe);
            }
            JefesArea jefe = responsables.getJefe();
            if (jefe != null) {
                jefe.getResponsablesList().remove(responsables);
                jefe = em.merge(jefe);
            }
            List<Empleados> empleadosList = responsables.getEmpleadosList();
            for (Empleados empleadosListEmpleados : empleadosList) {
                empleadosListEmpleados.setResponsable(null);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
            }
            List<Solicitudes> solicitudesList = responsables.getSolicitudesList();
            for (Solicitudes solicitudesListSolicitudes : solicitudesList) {
                solicitudesListSolicitudes.setResponsable(null);
                solicitudesListSolicitudes = em.merge(solicitudesListSolicitudes);
            }
            em.remove(responsables);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Responsables> findResponsablesEntities() {
        return findResponsablesEntities(true, -1, -1);
    }

    public List<Responsables> findResponsablesEntities(int maxResults, int firstResult) {
        return findResponsablesEntities(false, maxResults, firstResult);
    }

    private List<Responsables> findResponsablesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Responsables.class));
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

    public Responsables findResponsables(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Responsables.class, id);
        } finally {
            em.close();
        }
    }

    public int getResponsablesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Responsables> rt = cq.from(Responsables.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
