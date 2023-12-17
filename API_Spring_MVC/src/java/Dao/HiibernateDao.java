/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.BaseModel;
import Util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

/**
 *
 * @author Eqima
 */
public class HiibernateDao {
public static double faireSomme() {
        double somme = 0.0;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try  {
            session.beginTransaction();

         Query query = session.createQuery("SELECT SUM(prix) FROM Commande");
            somme = (Double) query.uniqueResult(); // uniqueResult car nous nous attendons à une seule valeur

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return somme;
    }
    public int save(BaseModel b) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tr = session.beginTransaction();
            session.save(b);
            tr.commit();
            return b.getId();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int update(BaseModel b) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tr = session.beginTransaction();
            session.update(b);
            tr.commit();
            return b.getId();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int delete(BaseModel b) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tr = session.beginTransaction();
            session.delete(b);
            tr.commit();
            return b.getId();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List getAllByCondition(BaseModel b, List<Criterion> condition) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tr = session.beginTransaction();
            Criteria cr = session.createCriteria(b.getClass());
            for (Criterion criterion : condition) {
                cr.add(criterion);
            }
            tr.commit();
            return cr.list();
        } catch (HibernateException e) {
            System.out.println(e.fillInStackTrace());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List getAll(BaseModel b) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tr = session.beginTransaction();
            Criteria cr = session.createCriteria(b.getClass());
            tr.commit();
            return cr.list();
        } catch (HibernateException e) {
            System.out.println(e.fillInStackTrace());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
