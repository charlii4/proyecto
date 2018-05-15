/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Entidades.Centros;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.JefesArea;
import java.util.ArrayList;
import java.util.List;
import Entidades.Empleados;
import Entidades.Directores;
import Entidades.Salidas;
import Vistas.Ppal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author charliVB
 */
public class CentrosJpaController implements Serializable {

    public CentrosJpaController() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    //Creamos metodo para la muestra en tabla de los registros
    DefaultTableModel modelo2;
    public void crearModeloCentros() {
        try {
            modelo2 = (new DefaultTableModel(
                    null, new String[]{
                        "cod_centro", "Nombre",
                        "ubicacion"}) {
                Class[] types = new Class[]{
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                };
                boolean[] canEdit = new boolean[]{
                    false, false, false
                };

                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }

                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                    return canEdit[colIndex];
                }
            });
           //Ppal.jTable2.setModel(modelo2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString() + "error2");
        }
    }
    public DefaultTableModel listarCentros(){
        crearModeloCentros();
        CentrosJpaController centrosC = new CentrosJpaController();
        try{
            Object O[]=null;
            List<Centros> c = centrosC.findCentrosEntities();
            for (int i=0; i < c.size(); i++){
                modelo2.addRow(O);
                modelo2.setValueAt(c.get(i).getCodCentro(), i, 0);
                modelo2.setValueAt(c.get(i).getNombre(), i, 1);
                modelo2.setValueAt(c.get(i).getUbicacion(), i, 2);
            }
    }catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
        return modelo2;
    }

    public void create(Centros centros) throws PreexistingEntityException, Exception {
        if (centros.getJefesAreaList() == null) {
            centros.setJefesAreaList(new ArrayList<JefesArea>());
        }
        if (centros.getEmpleadosList() == null) {
            centros.setEmpleadosList(new ArrayList<Empleados>());
        }
        if (centros.getDirectoresList() == null) {
            centros.setDirectoresList(new ArrayList<Directores>());
        }
        if (centros.getSalidasList() == null) {
            centros.setSalidasList(new ArrayList<Salidas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<JefesArea> attachedJefesAreaList = new ArrayList<JefesArea>();
            for (JefesArea jefesAreaListJefesAreaToAttach : centros.getJefesAreaList()) {
                jefesAreaListJefesAreaToAttach = em.getReference(jefesAreaListJefesAreaToAttach.getClass(), jefesAreaListJefesAreaToAttach.getCodJefe());
                attachedJefesAreaList.add(jefesAreaListJefesAreaToAttach);
            }
            centros.setJefesAreaList(attachedJefesAreaList);
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : centros.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            centros.setEmpleadosList(attachedEmpleadosList);
            List<Directores> attachedDirectoresList = new ArrayList<Directores>();
            for (Directores directoresListDirectoresToAttach : centros.getDirectoresList()) {
                directoresListDirectoresToAttach = em.getReference(directoresListDirectoresToAttach.getClass(), directoresListDirectoresToAttach.getCodDirector());
                attachedDirectoresList.add(directoresListDirectoresToAttach);
            }
            centros.setDirectoresList(attachedDirectoresList);
            List<Salidas> attachedSalidasList = new ArrayList<Salidas>();
            for (Salidas salidasListSalidasToAttach : centros.getSalidasList()) {
                salidasListSalidasToAttach = em.getReference(salidasListSalidasToAttach.getClass(), salidasListSalidasToAttach.getNumSalida());
                attachedSalidasList.add(salidasListSalidasToAttach);
            }
            centros.setSalidasList(attachedSalidasList);
            em.persist(centros);
            for (JefesArea jefesAreaListJefesArea : centros.getJefesAreaList()) {
                Centros oldCentroOfJefesAreaListJefesArea = jefesAreaListJefesArea.getCentro();
                jefesAreaListJefesArea.setCentro(centros);
                jefesAreaListJefesArea = em.merge(jefesAreaListJefesArea);
                if (oldCentroOfJefesAreaListJefesArea != null) {
                    oldCentroOfJefesAreaListJefesArea.getJefesAreaList().remove(jefesAreaListJefesArea);
                    oldCentroOfJefesAreaListJefesArea = em.merge(oldCentroOfJefesAreaListJefesArea);
                }
            }
            for (Empleados empleadosListEmpleados : centros.getEmpleadosList()) {
                Centros oldCentroOfEmpleadosListEmpleados = empleadosListEmpleados.getCentro();
                empleadosListEmpleados.setCentro(centros);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldCentroOfEmpleadosListEmpleados != null) {
                    oldCentroOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldCentroOfEmpleadosListEmpleados = em.merge(oldCentroOfEmpleadosListEmpleados);
                }
            }
            for (Directores directoresListDirectores : centros.getDirectoresList()) {
                Centros oldCentroOfDirectoresListDirectores = directoresListDirectores.getCentro();
                directoresListDirectores.setCentro(centros);
                directoresListDirectores = em.merge(directoresListDirectores);
                if (oldCentroOfDirectoresListDirectores != null) {
                    oldCentroOfDirectoresListDirectores.getDirectoresList().remove(directoresListDirectores);
                    oldCentroOfDirectoresListDirectores = em.merge(oldCentroOfDirectoresListDirectores);
                }
            }
            for (Salidas salidasListSalidas : centros.getSalidasList()) {
                Centros oldCentroOfSalidasListSalidas = salidasListSalidas.getCentro();
                salidasListSalidas.setCentro(centros);
                salidasListSalidas = em.merge(salidasListSalidas);
                if (oldCentroOfSalidasListSalidas != null) {
                    oldCentroOfSalidasListSalidas.getSalidasList().remove(salidasListSalidas);
                    oldCentroOfSalidasListSalidas = em.merge(oldCentroOfSalidasListSalidas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCentros(centros.getCodCentro()) != null) {
                throw new PreexistingEntityException("Centros " + centros + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Centros centros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centros persistentCentros = em.find(Centros.class, centros.getCodCentro());
            List<JefesArea> jefesAreaListOld = persistentCentros.getJefesAreaList();
            List<JefesArea> jefesAreaListNew = centros.getJefesAreaList();
            List<Empleados> empleadosListOld = persistentCentros.getEmpleadosList();
            List<Empleados> empleadosListNew = centros.getEmpleadosList();
            List<Directores> directoresListOld = persistentCentros.getDirectoresList();
            List<Directores> directoresListNew = centros.getDirectoresList();
            List<Salidas> salidasListOld = persistentCentros.getSalidasList();
            List<Salidas> salidasListNew = centros.getSalidasList();
            List<JefesArea> attachedJefesAreaListNew = new ArrayList<JefesArea>();
            for (JefesArea jefesAreaListNewJefesAreaToAttach : jefesAreaListNew) {
                jefesAreaListNewJefesAreaToAttach = em.getReference(jefesAreaListNewJefesAreaToAttach.getClass(), jefesAreaListNewJefesAreaToAttach.getCodJefe());
                attachedJefesAreaListNew.add(jefesAreaListNewJefesAreaToAttach);
            }
            jefesAreaListNew = attachedJefesAreaListNew;
            centros.setJefesAreaList(jefesAreaListNew);
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getCodEmpleado());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            centros.setEmpleadosList(empleadosListNew);
            List<Directores> attachedDirectoresListNew = new ArrayList<Directores>();
            for (Directores directoresListNewDirectoresToAttach : directoresListNew) {
                directoresListNewDirectoresToAttach = em.getReference(directoresListNewDirectoresToAttach.getClass(), directoresListNewDirectoresToAttach.getCodDirector());
                attachedDirectoresListNew.add(directoresListNewDirectoresToAttach);
            }
            directoresListNew = attachedDirectoresListNew;
            centros.setDirectoresList(directoresListNew);
            List<Salidas> attachedSalidasListNew = new ArrayList<Salidas>();
            for (Salidas salidasListNewSalidasToAttach : salidasListNew) {
                salidasListNewSalidasToAttach = em.getReference(salidasListNewSalidasToAttach.getClass(), salidasListNewSalidasToAttach.getNumSalida());
                attachedSalidasListNew.add(salidasListNewSalidasToAttach);
            }
            salidasListNew = attachedSalidasListNew;
            centros.setSalidasList(salidasListNew);
            centros = em.merge(centros);
            for (JefesArea jefesAreaListOldJefesArea : jefesAreaListOld) {
                if (!jefesAreaListNew.contains(jefesAreaListOldJefesArea)) {
                    jefesAreaListOldJefesArea.setCentro(null);
                    jefesAreaListOldJefesArea = em.merge(jefesAreaListOldJefesArea);
                }
            }
            for (JefesArea jefesAreaListNewJefesArea : jefesAreaListNew) {
                if (!jefesAreaListOld.contains(jefesAreaListNewJefesArea)) {
                    Centros oldCentroOfJefesAreaListNewJefesArea = jefesAreaListNewJefesArea.getCentro();
                    jefesAreaListNewJefesArea.setCentro(centros);
                    jefesAreaListNewJefesArea = em.merge(jefesAreaListNewJefesArea);
                    if (oldCentroOfJefesAreaListNewJefesArea != null && !oldCentroOfJefesAreaListNewJefesArea.equals(centros)) {
                        oldCentroOfJefesAreaListNewJefesArea.getJefesAreaList().remove(jefesAreaListNewJefesArea);
                        oldCentroOfJefesAreaListNewJefesArea = em.merge(oldCentroOfJefesAreaListNewJefesArea);
                    }
                }
            }
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    empleadosListOldEmpleados.setCentro(null);
                    empleadosListOldEmpleados = em.merge(empleadosListOldEmpleados);
                }
            }
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Centros oldCentroOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getCentro();
                    empleadosListNewEmpleados.setCentro(centros);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldCentroOfEmpleadosListNewEmpleados != null && !oldCentroOfEmpleadosListNewEmpleados.equals(centros)) {
                        oldCentroOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldCentroOfEmpleadosListNewEmpleados = em.merge(oldCentroOfEmpleadosListNewEmpleados);
                    }
                }
            }
            for (Directores directoresListOldDirectores : directoresListOld) {
                if (!directoresListNew.contains(directoresListOldDirectores)) {
                    directoresListOldDirectores.setCentro(null);
                    directoresListOldDirectores = em.merge(directoresListOldDirectores);
                }
            }
            for (Directores directoresListNewDirectores : directoresListNew) {
                if (!directoresListOld.contains(directoresListNewDirectores)) {
                    Centros oldCentroOfDirectoresListNewDirectores = directoresListNewDirectores.getCentro();
                    directoresListNewDirectores.setCentro(centros);
                    directoresListNewDirectores = em.merge(directoresListNewDirectores);
                    if (oldCentroOfDirectoresListNewDirectores != null && !oldCentroOfDirectoresListNewDirectores.equals(centros)) {
                        oldCentroOfDirectoresListNewDirectores.getDirectoresList().remove(directoresListNewDirectores);
                        oldCentroOfDirectoresListNewDirectores = em.merge(oldCentroOfDirectoresListNewDirectores);
                    }
                }
            }
            for (Salidas salidasListOldSalidas : salidasListOld) {
                if (!salidasListNew.contains(salidasListOldSalidas)) {
                    salidasListOldSalidas.setCentro(null);
                    salidasListOldSalidas = em.merge(salidasListOldSalidas);
                }
            }
            for (Salidas salidasListNewSalidas : salidasListNew) {
                if (!salidasListOld.contains(salidasListNewSalidas)) {
                    Centros oldCentroOfSalidasListNewSalidas = salidasListNewSalidas.getCentro();
                    salidasListNewSalidas.setCentro(centros);
                    salidasListNewSalidas = em.merge(salidasListNewSalidas);
                    if (oldCentroOfSalidasListNewSalidas != null && !oldCentroOfSalidasListNewSalidas.equals(centros)) {
                        oldCentroOfSalidasListNewSalidas.getSalidasList().remove(salidasListNewSalidas);
                        oldCentroOfSalidasListNewSalidas = em.merge(oldCentroOfSalidasListNewSalidas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = centros.getCodCentro();
                if (findCentros(id) == null) {
                    throw new NonexistentEntityException("The centros with id " + id + " no longer exists.");
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
            Centros centros;
            try {
                centros = em.getReference(Centros.class, id);
                centros.getCodCentro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The centros with id " + id + " no longer exists.", enfe);
            }
            List<JefesArea> jefesAreaList = centros.getJefesAreaList();
            for (JefesArea jefesAreaListJefesArea : jefesAreaList) {
                jefesAreaListJefesArea.setCentro(null);
                jefesAreaListJefesArea = em.merge(jefesAreaListJefesArea);
            }
            List<Empleados> empleadosList = centros.getEmpleadosList();
            for (Empleados empleadosListEmpleados : empleadosList) {
                empleadosListEmpleados.setCentro(null);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
            }
            List<Directores> directoresList = centros.getDirectoresList();
            for (Directores directoresListDirectores : directoresList) {
                directoresListDirectores.setCentro(null);
                directoresListDirectores = em.merge(directoresListDirectores);
            }
            List<Salidas> salidasList = centros.getSalidasList();
            for (Salidas salidasListSalidas : salidasList) {
                salidasListSalidas.setCentro(null);
                salidasListSalidas = em.merge(salidasListSalidas);
            }
            em.remove(centros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Centros> findCentrosEntities() {
        return findCentrosEntities(true, -1, -1);
    }

    public List<Centros> findCentrosEntities(int maxResults, int firstResult) {
        return findCentrosEntities(false, maxResults, firstResult);
    }

    private List<Centros> findCentrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Centros.class));
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

    public Centros findCentros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Centros.class, id);
        } finally {
            em.close();
        }
    }

    public int getCentrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Centros> rt = cq.from(Centros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
